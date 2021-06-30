package by.epam.jwd.web.command;

import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ChangePasswordCommand implements ActionCommand {

    private static final String REQUEST_USER_PASSWORD_PARAMETER_KEY = "password";
    private static final String REQUEST_USER_ID_PARAMETER_KEY = "id";

    private static final String SESSION_SUCCESS_ATTRIBUTE_KEY = "success";
    private static final String SUCCESS_MESSAGE = "Password was changed";
    private static final String SESSION_USER_ATTRIBUTE_KEY = "user";

    private static final String RESULT_PATH = "index.jsp";

    private ChangePasswordCommand() {
    }

    public static ChangePasswordCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final String newPassword = request.getParameter(REQUEST_USER_PASSWORD_PARAMETER_KEY);
        final Long userId = Long.valueOf(request.getParameter(REQUEST_USER_ID_PARAMETER_KEY));
        final User userWithChangedPassword = ServiceFactory.getInstance().getUserService().changePassword(userId, newPassword);
        final HttpSession session = request.getSession();
        session.setAttribute(SESSION_SUCCESS_ATTRIBUTE_KEY, "passwordChanged");
        session.setAttribute(SESSION_USER_ATTRIBUTE_KEY, userWithChangedPassword);
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
        private static final ChangePasswordCommand INSTANCE = new ChangePasswordCommand();
    }
}
