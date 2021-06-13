package by.epam.jwd.web.command;

import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class AddCopyCommand implements ActionCommand {

    public static final String ID = "id";
    public static final String COMMAND_RESULT = "commandResult";
    public static final String RESULT_MESSAGE = "copy of book was added";

    private AddCopyCommand() {
    }

    public static AddCopyCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        final String bookId = request.getParameter(ID);
        final long parsedId = Long.parseLong(bookId);
        try {
            ServiceFactory.getInstance().getBookService().addOneCopy(parsedId);
            request.getSession().setAttribute(COMMAND_RESULT, RESULT_MESSAGE);
            return null;
        } catch (ServiceException e) {
            request.getSession().setAttribute(COMMAND_RESULT, e.getMessage());
            return null;
        }
    }

    private static class Singleton {
        private static final AddCopyCommand INSTANCE = new AddCopyCommand();
    }
}
