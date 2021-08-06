package by.epam.jwd.web.command.action.user;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.resource.PathManager;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.service.api.ServiceFactory;
import by.epam.jwd.web.service.api.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Executes command that is change saved {@link User} password in database table.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class ChangePasswordCommand implements ActionCommand {
    private final UserService userService = ServiceFactory.getInstance().getUserService();

    private static final String REQUEST_USER_PASSWORD_PARAMETER_KEY = "password";
    private static final String REQUEST_MESSAGE_ATTRIBUTE_KEY = "message";
    private static final String USER_PASSWORD_WAS_CHANGED_MESSAGE_KEY = "user.password.changed";
    private static final String SESSION_USER_ATTRIBUTE_KEY = "user";

    private ChangePasswordCommand() {}

    /**
     * Gets single class instance from nested class.
     * @return class instance.
     */
    public static ChangePasswordCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Changes saved {@link User} password.
     * Request must have new password to change password.
     * @param request request that need to be execute.
     * @return account page path for forward.
     */
    @Override
    public String execute(HttpServletRequest request) {
        final HttpSession session = request.getSession();
        final User user = (User) session.getAttribute(SESSION_USER_ATTRIBUTE_KEY);
        final String newPassword = request.getParameter(REQUEST_USER_PASSWORD_PARAMETER_KEY);
        final User userWithChangedPassword = userService.changePassword(user, newPassword);
        request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(USER_PASSWORD_WAS_CHANGED_MESSAGE_KEY));
        session.setAttribute(SESSION_USER_ATTRIBUTE_KEY, userWithChangedPassword);
        return PathManager.getAccountPagePath();
    }

    /**
     * Nested class that encapsulates single {@link ChangePasswordCommand} instance.
     * Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final ChangePasswordCommand INSTANCE = new ChangePasswordCommand();
    }
}
