package by.epam.jwd.web.command.navigation;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Comment;
import by.epam.jwd.web.resource.PathManager;
import by.epam.jwd.web.service.BookService;
import by.epam.jwd.web.service.CommentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Executes command that is forward to main page.
 *
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class MainCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(MainCommand.class);
    private final BookService bookService = BookService.getInstance();
    private final CommentService commentService = CommentService.getInstance();
    private static final String COMMAND_REQUESTED_MESSAGE = "Main command was requested";
    private static final String COMMAND_EXECUTED_MESSAGE = "Main command was executed";
    private static final String REQUEST_BOOKS_ATTRIBUTE_KEY = "books";
    private static final String REQUEST_PAGE_PARAMETER_KEY = "page";
    private static final String REQUEST_COMMENTS_ATTRIBUTE_KEY = "comments";
    private static final String REQUEST_CURRENT_PAGE_NUMBER_ATTRIBUTE_KEY = "currentPageNumber";
    private static final String REQUEST_PAGES_AMOUNT_ATTRIBUTE_KEY = "pagesAmount";

    private MainCommand() {
    }

    /**
     * Gets single class instance from nested class.
     *
     * @return class instance.
     */
    public static MainCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Gets first books page from service and their comments, sets language to english if
     * language is not set and returns main page path to forward.
     *
     * @param request request that need to be execute.
     * @return main page path for forward.
     */
    @Override
    public String execute(HttpServletRequest request) {
        logger.info(COMMAND_REQUESTED_MESSAGE);
        final String pageParameter = request.getParameter(REQUEST_PAGE_PARAMETER_KEY);
        int currentPageNumber = pageParameter == null ? 1 : Integer.parseInt(pageParameter);
        final List<Book> currentPage = bookService.findPage(currentPageNumber);
        final List<Comment> comments = findComments(currentPage);
        final int pagesAmount = bookService.getPagesAmount();
        request.setAttribute(REQUEST_COMMENTS_ATTRIBUTE_KEY, comments);
        request.setAttribute(REQUEST_BOOKS_ATTRIBUTE_KEY, currentPage);
        request.setAttribute(REQUEST_CURRENT_PAGE_NUMBER_ATTRIBUTE_KEY, currentPageNumber);
        request.setAttribute(REQUEST_PAGES_AMOUNT_ATTRIBUTE_KEY, pagesAmount);
        logger.info(COMMAND_EXECUTED_MESSAGE);
        return PathManager.getMainPagePath();
    }

    private List<Comment> findComments(List<Book> books) {
        final List<Comment> comments = new ArrayList<>();
        for (Book book : books) {
            comments.addAll(commentService.findByBook(book));
        }
        return comments;
    }

    /**
     * Nested class that encapsulates single {@link MainCommand} instance.
     * Singleton pattern variation.
     *
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final MainCommand INSTANCE = new MainCommand();
    }
}
