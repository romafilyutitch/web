package by.epam.jwd.web.command;

import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.ServiceFactory;
import by.epam.jwd.web.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ChangePasswordCommand implements ActionCommand {
    private final UserService userService = ServiceFactory.getInstance().getUserService();

    private static final String SESSION_USER_ATTRIBUTE_KEY = "user";
    private static final String REQUEST_USER_PASSWORD_PARAMETER_KEY = "password";
    private static final String SESSION_SUCCESS_ATTRIBUTE_KEY = "success";
    private static final String PASSWORD_CHANGED_LOCALIZATION_MESSAGE_KEY = "passwordChanged";

    private static final String RESULT_PATH = "index.jsp";

    private ChangePasswordCommand() {
    }

    public static ChangePasswordCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final String newPassword = request.getParameter(REQUEST_USER_PASSWORD_PARAMETER_KEY);
        final HttpSession session = request.getSession();
        final User user = (User) session.getAttribute(SESSION_USER_ATTRIBUTE_KEY);
        final User userWithChangedPassword = userService.changePassword(user, newPassword);
        session.setAttribute(SESSION_SUCCESS_ATTRIBUTE_KEY, PASSWORD_CHANGED_LOCALIZATION_MESSAGE_KEY);
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
