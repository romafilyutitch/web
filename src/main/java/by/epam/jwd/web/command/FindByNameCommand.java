package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.service.ServiceException;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;

public class FindByNameCommand implements ActionCommand {

    public static final String NAME = "name";
    public static final String BOOKS = "books";
    public static final String MAIN_JSP_PATH = "WEB-INF/jsp/main.jsp";
    public static final String ERROR = "error";

    private FindByNameCommand() {
    }

    public static FindByNameCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        final String name = request.getParameter(NAME);
        try {
            final Book book = ServiceFactory.getInstance().getBookService().findByName(name);
            request.setAttribute(BOOKS, Collections.singletonList(book));
            return MAIN_JSP_PATH;
        } catch (ServiceException e) {
            request.setAttribute(ERROR, e.getMessage());
            return MAIN_JSP_PATH;
        }
    }

    private static class Singleton {
        private static final FindByNameCommand INSTANCE = new FindByNameCommand();
    }
}
