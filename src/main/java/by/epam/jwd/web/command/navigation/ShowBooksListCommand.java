package by.epam.jwd.web.command.navigation;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;
import by.epam.jwd.web.resource.ConfigurationManager;
import by.epam.jwd.web.service.BookService;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Executes command that is get books and forward to books page.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class ShowBooksListCommand implements ActionCommand {
    private final BookService bookService = ServiceFactory.getInstance().getBookService();

    private static final String REQUEST_PAGE_PARAMETER_KEY = "page";
    private static final String REQUEST_GENRES_ATTRIBUTE_KEY = "genres";
    private static final String REQUEST_BOOKS_ATTRIBUTE_KEY = "books";
    private static final String REQUEST_PAGE_AMOUNT_ATTRIBUTE_KEY = "pagesAmount";
    private static final String REQUEST_CURRENT_PAGE_NUMBER_ATTRIBUTE_KEY = "currentPageNumber";

    private ShowBooksListCommand() {
    }

    /**
     * Gets single class instance from nested class.
     * @return class instance.
     */
    public static ShowBooksListCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Get books page from service if page number is passed or first page
     * otherwise and returns books page to forward.
     * @param request request that need to be execute.
     * @return books page for forward.
     */
    @Override
    public String execute(HttpServletRequest request) {
        int currentPageNumber = 1;
        final String pageParameter = request.getParameter(REQUEST_PAGE_PARAMETER_KEY);
        if (pageParameter != null) {
            currentPageNumber = Integer.parseInt(pageParameter);
        }
        final List<Book> currentPage = bookService.findPage(currentPageNumber);
        final int pagesAmount = bookService.getPagesAmount();
        final Genre[] genres = Genre.values();
        request.setAttribute(REQUEST_GENRES_ATTRIBUTE_KEY, genres);
        request.setAttribute(REQUEST_BOOKS_ATTRIBUTE_KEY, currentPage);
        request.setAttribute(REQUEST_PAGE_AMOUNT_ATTRIBUTE_KEY, pagesAmount);
        request.setAttribute(REQUEST_CURRENT_PAGE_NUMBER_ATTRIBUTE_KEY, currentPageNumber);
        return ConfigurationManager.getBooksPagePath();
    }

    /**
     * Nested class that encapsulates single {@link ShowBooksListCommand} instance.
     * Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final ShowBooksListCommand INSTANCE = new ShowBooksListCommand();
    }
}
