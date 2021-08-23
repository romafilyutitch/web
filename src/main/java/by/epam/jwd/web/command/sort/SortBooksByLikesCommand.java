package by.epam.jwd.web.command.sort;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Comment;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.resource.PathManager;
import by.epam.jwd.web.service.BookService;
import by.epam.jwd.web.service.CommentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Performs sort books by like amount in descending order
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class SortBooksByLikesCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(SortBooksByLikesCommand.class);
    private final BookService bookService = BookService.getInstance();
    private final CommentService commentService = CommentService.getInstance();
    private static final String COMMAND_WAS_REQUESTED_MESSAGE = "Sort books by likes command was requested";
    private static final String COMMAND_WAS_EXECUTED_MESSAGE = "Sort book by likes comment was executed";
    private static final String REQUEST_ATTRIBUTE_BOOKS_KEY = "books";
    private static final String REQUEST_ATTRIBUTE_COMMENTS_KEY = "comments";
    private static final String REQUEST_ATTRIBUTE_MESSAGE_KEY = "message";
    private static final String BOOKS_WERE_SORED_MESSAGE_KEY = "book.sort.like.sorted";
    private static final String BOOKS_SORTED_MESSAGE_KEY = BOOKS_WERE_SORED_MESSAGE_KEY;

    private SortBooksByLikesCommand() {}

    public static SortBooksByLikesCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        logger.info(COMMAND_WAS_REQUESTED_MESSAGE);
        final List<Book> allBooks = bookService.findAll();
        final List<Book> sortedBooks = bookService.sortByLikes(allBooks);
        final List<Comment> comments = commentService.findByBooks(allBooks);
        request.setAttribute(REQUEST_ATTRIBUTE_BOOKS_KEY, sortedBooks);
        request.setAttribute(REQUEST_ATTRIBUTE_COMMENTS_KEY, comments);
        request.setAttribute(REQUEST_ATTRIBUTE_MESSAGE_KEY, MessageManager.getMessage(BOOKS_SORTED_MESSAGE_KEY));
        logger.info(COMMAND_WAS_EXECUTED_MESSAGE);
        return PathManager.getMainPagePath();
    }

    private static class Singleton {
        private static final SortBooksByLikesCommand INSTANCE = new SortBooksByLikesCommand();
    }
}
