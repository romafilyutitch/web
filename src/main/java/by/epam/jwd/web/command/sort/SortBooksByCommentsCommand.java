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

public class SortBooksByCommentsCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(SortBooksByCommentsCommand.class);
    private final BookService bookService = BookService.getInstance();
    private final CommentService commentService = CommentService.getInstance();
    private static final String COMMAND_REQUESTED_MESSAGE = "Sort books by comment command was requested";
    private static final String COMMAND_EXECUTED_MESSAGE = "Sort books by comment command was executed";
    private static final String REQUEST_ATTRIBUTE_BOOKS_KEY = "books";
    private static final String REQUEST_ATTRIBUTE_COMMENTS_KEY = "comments";
    private static final String REQUEST_ATTRIBUTE_MESSAGE_KEY = "message";
    private static final String BOOKS_SORTED_MESSAGE = "book.sort.comment.sorted";

    private SortBooksByCommentsCommand() {}

    public static SortBooksByCommentsCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        logger.info(COMMAND_REQUESTED_MESSAGE);
        final List<Book> allBooks = bookService.findAll();
        final List<Comment> booksComments = commentService.findByBooks(allBooks);
        final List<Book> sortedBooks = bookService.sortByComments(allBooks);
        request.setAttribute(REQUEST_ATTRIBUTE_BOOKS_KEY, sortedBooks);
        request.setAttribute(REQUEST_ATTRIBUTE_COMMENTS_KEY, booksComments);
        request.setAttribute(REQUEST_ATTRIBUTE_MESSAGE_KEY, MessageManager.getMessage(BOOKS_SORTED_MESSAGE));
        logger.info(COMMAND_EXECUTED_MESSAGE);
        return PathManager.getMainPagePath();
    }

    private static class Singleton {
        private static final SortBooksByCommentsCommand INSTANCE = new SortBooksByCommentsCommand();
    }
}
