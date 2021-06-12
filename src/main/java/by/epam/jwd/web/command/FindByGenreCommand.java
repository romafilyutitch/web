package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.service.ServiceException;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class FindByGenreCommand implements ActionCommand {

    public static final String NAME = "name";
    public static final String BOOKS = "books";
    public static final String MAIN_JSP_PATH = "WEB-INF/jsp/main.jsp";
    public static final String ERROR = "error";

    private FindByGenreCommand() {
    }

    public static FindByGenreCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        final String genreName = request.getParameter(NAME);
        try {
            final List<Book> byGenre = ServiceFactory.getInstance().getBookService().findByGenre(genreName);
            request.setAttribute(BOOKS, byGenre);
            return MAIN_JSP_PATH;
        } catch (ServiceException e) {
            request.setAttribute(ERROR, e.getMessage());
            return MAIN_JSP_PATH;
        }
    }

    private static class Singleton {
        private static final FindByGenreCommand INSTANCE = new FindByGenreCommand();
    }
}
