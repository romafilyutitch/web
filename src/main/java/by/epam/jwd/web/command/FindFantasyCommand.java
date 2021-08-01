package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Comment;
import by.epam.jwd.web.model.Genre;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.service.BookService;
import by.epam.jwd.web.service.CommentService;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

public class FindFantasyCommand implements ActionCommand {
    private final BookService bookService = ServiceFactory.getInstance().getBookService();
    private final CommentService commentService = ServiceFactory.getInstance().getCommentService();

    private static final String REQUEST_BOOKS_ATTRIBUTE_KEY = "books";
    private static final String REQUEST_MESSAGE_ATTRIBUTE_KEY = "message";
    private static final String REQUEST_COMMENTS_ATTRIBUTE_KEY = "comments";
    private static final String FANTASY_BOOKS_WERE_FOUND_MESSAGE_KEY = "book.find.fantasy.found";
    private static final String FANTASY_BOOKS_WERE_NOT_FOUND_MESSAGE_KEY = "book.find.fantasy.notFound";

    private static final String RESULT_PATH = "WEB-INF/jsp/main.jsp";

    private FindFantasyCommand() {
    }

    public static FindFantasyCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        final List<Book> fantasyBooks = bookService.findByGenre(Genre.FANTASY);
        if (fantasyBooks.isEmpty()) {
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(FANTASY_BOOKS_WERE_NOT_FOUND_MESSAGE_KEY));
        } else {
            request.setAttribute(REQUEST_BOOKS_ATTRIBUTE_KEY, fantasyBooks);
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(FANTASY_BOOKS_WERE_FOUND_MESSAGE_KEY));
            final List<Comment> comments = findComments(fantasyBooks);
            request.setAttribute(REQUEST_COMMENTS_ATTRIBUTE_KEY, comments);
        }
        return RESULT_PATH;
    }

    private List<Comment> findComments(List<Book> books) {
        final List<Comment> comments = new ArrayList<>();
        for (Book book : books) {
            comments.addAll(commentService.findByBook(book));
        }
        return comments;
    }

    private static class Singleton {
        private static final FindFantasyCommand INSTANCE = new FindFantasyCommand();
    }
}
