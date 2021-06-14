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
    public String execute(HttpServletRequest request) {
        final List<Book> all = ServiceFactory.getInstance().getBookService().findAllBooks();
        request.setAttribute(BOOKS, all);
        return MAIN_JSP_PATH;
    }

    private static class Singleton {
        private static final MainCommand INSTANCE = new MainCommand();
    }
}
