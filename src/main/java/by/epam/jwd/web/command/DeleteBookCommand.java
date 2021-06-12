package by.epam.jwd.web.command;

import by.epam.jwd.web.service.ServiceException;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class DeleteBookCommand implements ActionCommand {

    public static final String ID = "id";
    public static final String COMMAND_RESULT = "commandResult";
    public static final String RESULT_MESSAGE = "book was deleted";


    private DeleteBookCommand() {
    }

    public static DeleteBookCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        final long parsedId = Long.parseLong(request.getParameter(ID));
        try {
            ServiceFactory.getInstance().getBookService().deleteBook(parsedId);
            request.getSession().setAttribute(COMMAND_RESULT, RESULT_MESSAGE);
            return null;
        } catch (ServiceException e) {
            request.getSession().setAttribute(COMMAND_RESULT, e.getMessage());
            return null;
        }
    }

    private static class Singleton {
        private static final DeleteBookCommand INSTANCE = new DeleteBookCommand();
    }
}
