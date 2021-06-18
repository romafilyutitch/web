package by.epam.jwd.web.command;

import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class DeleteUserCommand implements ActionCommand {

    public static final String ID = "id";
    public static final String COMMAND_RESULT = "commandResult";
    public static final String RESULT_MESSAGE = "your account was deleted";

    private DeleteUserCommand() {
    }

    public static DeleteUserCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final Long id = Long.valueOf(request.getParameter("id"));
        ServiceFactory.getInstance().getUserService().deleteUser(id);
        request.getSession().invalidate();
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
        private static final DeleteUserCommand INSTANCE = new DeleteUserCommand();
    }
}
