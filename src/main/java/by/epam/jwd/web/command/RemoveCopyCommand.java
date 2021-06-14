package by.epam.jwd.web.command;

import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class RemoveCopyCommand implements ActionCommand {

    public static final String ID = "id";
    public static final String COMMAND_RESULT = "commandResult";
    public static final String RESULT_MESSAGE = "copy of book %s was removed";

    private RemoveCopyCommand() {
    }

    public static RemoveCopyCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        final Long id = Long.valueOf(request.getParameter(ID));
        final Book book = ServiceFactory.getInstance().getBookService().removeOneCopy(id);
        request.getSession().setAttribute(COMMAND_RESULT, String.format(RESULT_MESSAGE, book.getName()));
        return null;
    }

    private static class Singleton {
        private static final RemoveCopyCommand INSTANCE = new RemoveCopyCommand();
    }
}
