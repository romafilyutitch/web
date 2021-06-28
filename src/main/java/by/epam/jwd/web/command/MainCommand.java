package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class MainCommand implements ActionCommand {

    private static final String REQUEST_BOOKS_ATTRIBUTE_KEY = "books";
    private static final String REQUEST_PAGE_PARAMETER_KEY = "page";
    private static final String REQUEST_CURRENT_PAGE_NUMBER_ATTRIBUTE_KEY = "currentPageNumber";
    private static final String REQUEST_PAGES_AMOUNT_ATTRIBUTE_KEY = "pagesAmount";

    private static final String RESULT_PATH = "WEB-INF/jsp/main.jsp";

    private MainCommand() {
    }

    public static MainCommand getInstance() {
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
        request.setAttribute(REQUEST_BOOKS_ATTRIBUTE_KEY, currentPage);
        request.setAttribute(REQUEST_CURRENT_PAGE_NUMBER_ATTRIBUTE_KEY, currentPageNumber);
        request.setAttribute(REQUEST_PAGES_AMOUNT_ATTRIBUTE_KEY, pagesAmount);
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
        private static final MainCommand INSTANCE = new MainCommand();
    }
}
