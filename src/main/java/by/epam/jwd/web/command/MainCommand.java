package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class MainCommand implements ActionCommand {

    public static final String BOOKS = "books";
    public static final String MAIN_JSP_PATH = "WEB-INF/jsp/main.jsp";

    private MainCommand() {
    }

    public static MainCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        int currentPageNumber = 1;
        final String pageParameter = request.getParameter("page");
        if (pageParameter != null) {
            currentPageNumber = Integer.parseInt(pageParameter);
        }
        final List<Book> currentPage = ServiceFactory.getInstance().getBookService().findPage(currentPageNumber);
        final int pagesAmount = ServiceFactory.getInstance().getBookService().getPagesAmount();
        request.setAttribute(BOOKS, currentPage);
        request.setAttribute("currentPageNumber", currentPageNumber);
        request.setAttribute("pagesAmount", pagesAmount);
        return new CommandResult() {
            @Override
            public String getResultPath() {
                return "WEB-INF/jsp/main.jsp";
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
