package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.service.ServiceException;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class ReadCommand implements ActionCommand {

    public static final String ID = "id";
    public static final String BOOK = "book";
    public static final String READ_JSP_PATH = "WEB-INF/jsp/read.jsp";
    public static final String ERROR = "error";
    public static final String MAIN_COMMAND_CONTROLLER = "controller?command=main";

    private ReadCommand() {
    }

    public static ReadCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        final long id = Long.parseLong(request.getParameter(ID));
        try {
            final Book bookToRead = ServiceFactory.getInstance().getBookService().findById(id);
            request.setAttribute(BOOK, bookToRead);
            return READ_JSP_PATH;
        } catch (ServiceException e) {
            request.setAttribute(ERROR, e.getMessage());
            return MAIN_COMMAND_CONTROLLER;
        }
    }

    private static class Singleton {
        private static final ReadCommand INSTANCE = new ReadCommand();
    }
}
