package by.epam.jwd.web.command.action.book.find;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Comment;
import by.epam.jwd.web.model.Genre;
import by.epam.jwd.web.resource.ConfigurationManager;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.service.BookService;
import by.epam.jwd.web.service.CommentService;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class FindFictionCommand implements ActionCommand {
    private final BookService bookService = ServiceFactory.getInstance().getBookService();
    private final CommentService commentService = ServiceFactory.getInstance().getCommentService();

    private static final String REQUEST_BOOKS_ATTRIBUTE_KEY = "books";
    private static final String REQUEST_COMMENTS_ATTRIBUTE_KEY = "comments";
    private static final String REQUEST_MESSAGE_ATTRIBUTE_KEY = "message";
    private static final String FICTION_BOOKS_WERE_FOUND_MESSAGE_KEY = "book.find.fiction.found";
    private static final String FICTION_BOOKS_WERE_NOT_FOUND_MESSAGE_KEY = "book.find.fiction.notFound";

    private FindFictionCommand() {
    }

    public static FindFictionCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        final List<Book> fictionBooks = bookService.findByGenre(Genre.FICTION);
        if (fictionBooks.isEmpty()) {
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(FICTION_BOOKS_WERE_NOT_FOUND_MESSAGE_KEY));
        } else {
            request.setAttribute(REQUEST_BOOKS_ATTRIBUTE_KEY, fictionBooks);
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(FICTION_BOOKS_WERE_FOUND_MESSAGE_KEY));
            final List<Comment> comments = findComments(fictionBooks);
            request.setAttribute(REQUEST_COMMENTS_ATTRIBUTE_KEY, comments);
        }
        return ConfigurationManager.getMainPagePath();
    }

    private List<Comment> findComments(List<Book> books) {
        final ArrayList<Comment> comments = new ArrayList<>();
        for (Book book : books) {
            comments.addAll(commentService.findByBook(book));
        }
        return comments;
    }

    private static class Singleton {
        private static final FindFictionCommand INSTANCE = new FindFictionCommand();
    }
}
