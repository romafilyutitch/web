package by.epam.jwd.web.command;

import by.epam.jwd.web.model.User;
import by.epam.jwd.web.resource.ConfigurationManager;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.service.ServiceFactory;
import by.epam.jwd.web.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ChangePasswordCommand implements ActionCommand {
    private final UserService userService = ServiceFactory.getInstance().getUserService();

    private static final String REQUEST_USER_PASSWORD_PARAMETER_KEY = "password";
    private static final String REQUEST_MESSAGE_ATTRIBUTE_KEY = "message";
    private static final String USER_PASSWORD_WAS_CHANGED_MESSAGE_KEY = "user.password.changed";
    private static final String SESSION_USER_ATTRIBUTE_KEY = "user";

    private ChangePasswordCommand() {
    }

    public static ChangePasswordCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        final HttpSession session = request.getSession();
        final User user = (User) session.getAttribute(SESSION_USER_ATTRIBUTE_KEY);
        final String newPassword = request.getParameter(REQUEST_USER_PASSWORD_PARAMETER_KEY);
        final User userWithChangedPassword = userService.changePassword(user, newPassword);
        request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(USER_PASSWORD_WAS_CHANGED_MESSAGE_KEY));
        session.setAttribute(SESSION_USER_ATTRIBUTE_KEY, userWithChangedPassword);
        return ConfigurationManager.getAccountPagePath();
    }

    private static class Singleton {
        private static final ChangePasswordCommand INSTANCE = new ChangePasswordCommand();
    }
}
