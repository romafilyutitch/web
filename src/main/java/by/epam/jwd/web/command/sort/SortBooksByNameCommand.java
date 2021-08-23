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
 * Class performs books sorting by their names.
 * Retrieves all books from service and
 * make sort
 *
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class SortBooksByNameCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(SortBooksByNameCommand.class);
    private final BookService bookService = BookService.getInstance();
    private final CommentService commentService = CommentService.getInstance();
    private static final String COMMAND_REQUESTED_MESSAGE = "Sort books by name command was requested";
    private static final String COMMAND_EXECUTED_MESSAGE = "Sort books by name command was executed";
    private static final String REQUEST_ATTRIBUTE_BOOKS_KEY = "books";
    private static final String REQUEST_ATTRIBUTE_COMMENTS_KEY = "comments";
    private static final String REQUEST_ATTRIBUTE_MESSAGE_KEY = "message";
    private static final String BOOKS_SORTED_MESSAGE_KEY = "book.sort.name.sorted";

    private SortBooksByNameCommand() {
    }

    /**
     * Returns class instance that contains in inner class.
     *
     * @return class instance
     */
    public static SortBooksByNameCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Performs books sorting by their names and returns
     * main page path for forward.
     *
     * @param request request that need to be execute
     * @return main page path for forward.
     */
    @Override
    public String execute(HttpServletRequest request) {
        logger.info(COMMAND_REQUESTED_MESSAGE);
        final List<Book> allBooks = bookService.findAll();
        final List<Book> nameSortedBooks = bookService.sortByName(allBooks);
        final List<Comment> comments = commentService.findByBooks(nameSortedBooks);
        request.setAttribute(REQUEST_ATTRIBUTE_BOOKS_KEY, nameSortedBooks);
        request.setAttribute(REQUEST_ATTRIBUTE_COMMENTS_KEY, comments);
        request.setAttribute(REQUEST_ATTRIBUTE_MESSAGE_KEY, MessageManager.getMessage(BOOKS_SORTED_MESSAGE_KEY));
        logger.info(COMMAND_EXECUTED_MESSAGE);
        return PathManager.getMainPagePath();
    }

    /**
     * Class encapsulates single outer class instance. Singleton pattern variation
     */
    private static class Singleton {
        private static final SortBooksByNameCommand INSTANCE = new SortBooksByNameCommand();
    }
}
