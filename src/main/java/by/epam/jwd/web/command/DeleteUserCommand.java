package by.epam.jwd.web.command;

import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class DeleteUserCommand implements ActionCommand {

    private static final String REQUEST_USER_ID_PARAMETER_KEY = "id";

    private static final String RESULT_PATH = "index.jsp";

    private DeleteUserCommand() {
    }

    public static DeleteUserCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final Long id = Long.valueOf(request.getParameter(REQUEST_USER_ID_PARAMETER_KEY));
        ServiceFactory.getInstance().getUserService().delete(id);
        request.getSession().invalidate();
        return new CommandResult() {
            @Override
            public String getResultPath() {
                return RESULT_PATH;
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
