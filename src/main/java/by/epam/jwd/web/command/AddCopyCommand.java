package by.epam.jwd.web.command;

import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class AddCopyCommand implements ActionCommand {

    public static final String ID = "id";
    public static final String COMMAND_RESULT = "commandResult";
    public static final String RESULT_MESSAGE = "copy of book %s was added";

    private AddCopyCommand() {
    }

    public static AddCopyCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final Long id = Long.valueOf(request.getParameter("id"));
        final Book book = ServiceFactory.getInstance().getBookService().addOneCopy(id);
        request.getSession().setAttribute("success", String.format("copy of book %s was added", book.getName()));
        return new CommandResult() {
            @Override
            public String getResultPath() {
                return "index.jsp";
            }

            @Override
            public boolean isRedirect() {
                return true;
            }
        };
    }

    private static class Singleton {
        private static final AddCopyCommand INSTANCE = new AddCopyCommand();
    }
}
