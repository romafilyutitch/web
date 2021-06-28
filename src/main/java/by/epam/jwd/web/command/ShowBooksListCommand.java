package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ShowBooksListCommand implements ActionCommand {

    private static final String REQUEST_PAGE_PARAMETER_KEY = "page";
    private static final String REQUEST_GENRES_ATTRIBUTE_KEY = "genres";
    private static final String REQUEST_BOOKS_ATTRIBUTE_KEY = "books";
    private static final String REQUEST_PAGE_AMOUNT_ATTRIBUTE_KEY = "pagesAmount";
    private static final String REQUEST_CURRENT_PAGE_NUMBER_ATTRIBUTE_KEY = "currentPageNumber";

    private static final String RESULT_PATH = "WEB-INF/jsp/books.jsp";

    private ShowBooksListCommand() {
    }

    public static ShowBooksListCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        int currentPageNumber = 1;
        final String pageParameter = request.getParameter(REQUEST_PAGE_PARAMETER_KEY);
        if (pageParameter != null) {
            currentPageNumber = Integer.parseInt(pageParameter);
        }
        final List<Book> currentPage = ServiceFactory.getInstance().getBookService().findPage(currentPageNumber);
        final int pagesAmount = ServiceFactory.getInstance().getBookService().getPagesAmount();
        final Genre[] genres = Genre.values();
        request.setAttribute(REQUEST_GENRES_ATTRIBUTE_KEY, genres);
        request.setAttribute(REQUEST_BOOKS_ATTRIBUTE_KEY, currentPage);
        request.setAttribute(REQUEST_PAGE_AMOUNT_ATTRIBUTE_KEY, pagesAmount);
        request.setAttribute(REQUEST_CURRENT_PAGE_NUMBER_ATTRIBUTE_KEY, currentPageNumber);
        return new CommandResult() {
            @Override
            public String getResultPath() {
                return RESULT_PATH;
            }

            @Override
            public boolean isRedirect() {
                return false;
            }
        };
    }

    private static class Singleton {
        private static final ShowBooksListCommand INSTANCE = new ShowBooksListCommand();
    }
}
