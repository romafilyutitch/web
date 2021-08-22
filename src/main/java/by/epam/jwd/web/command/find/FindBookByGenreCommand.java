package by.epam.jwd.web.command.find;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Comment;
import by.epam.jwd.web.model.Genre;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.resource.PathManager;
import by.epam.jwd.web.service.BookService;
import by.epam.jwd.web.service.CommentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class FindBookByGenreCommand implements ActionCommand {
    private final static Logger logger = LogManager.getLogger(FindBookByGenreCommand.class);
    private final BookService bookService = BookService.getInstance();
    private final CommentService commentService = CommentService.getInstance();

    private FindBookByGenreCommand() {
    }

    public static FindBookByGenreCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        logger.info("Find book by genre command was requested");
        final String genreParameter = request.getParameter("genre");
        final Genre genre = Genre.valueOf(genreParameter.toUpperCase());
        final List<Book> books = bookService.findByGenre(genre);
        if (books.isEmpty()) {
            request.setAttribute("message", MessageManager.getMessage("book.find.genre.notFound"));
        } else {
            final List<Comment> comments = findComments(books);
            request.setAttribute("books", books);
            request.setAttribute("comments", comments);
            request.setAttribute("message", MessageManager.getMessage("book.find.genre.found"));
        }
        logger.info("Find book by genre command was executed");
        return PathManager.getMainPagePath();
    }

    private List<Comment> findComments(List<Book> books) {
        final List<Comment> comments = new ArrayList<>();
        for (Book book : books) {
            comments.addAll(commentService.findByBook(book));
        }
        return comments;
    }

    private static class Singleton {
        private static final FindBookByGenreCommand INSTANCE = new FindBookByGenreCommand();
    }
}
