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
        final List<Book> firstPage = ServiceFactory.getInstance().getBookService().findPage(1);
        final int pagesAmount = ServiceFactory.getInstance().getBookService().getPagesAmount();
        request.setAttribute(BOOKS, firstPage);
        request.setAttribute("currentPage", 1);
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
