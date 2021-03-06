package by.epam.jwd.web.command.action.user;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.resource.PathManager;
import by.epam.jwd.web.service.UserService;
import by.epam.jwd.web.service.UserWithLoginExistsException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Executes command that is change saved {@link User} login.
 *
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class ChangeLoginCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(ChangeLoginCommand.class);
    private final UserService userService = UserService.getInstance();
    private static final String COMMAND_REQUESTED_MESSAGE = "Change login command was requested";
    private static final String COMMAND_EXECUTED_MESSAGE = "Change login command was executed";
    private static final String REQUEST_USER_LOGIN_PARAMETER_KEY = "login";
    private static final String REQUEST_MESSAGE_ATTRIBUTE_KEY = "message";
    private static final String SESSION_USER_ATTRIBUTE_KEY = "user";
    private static final String USER_LOGIN_CHANGED_MESSAGE_KEY = "user.login.changed";
    private static final String USER_LOGIN_EXISTS_MESSAGE_KEY = "user.login.exists";

    private ChangeLoginCommand() {
    }

    /**
     * Gets single class instance from nested class.
     *
     * @return class.
     */
    public static ChangeLoginCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Changes saved {@link User} login.
     * Request must contain new login.
     *
     * @param request request that need to be execute.
     * @return account page path for forward.
     */
    @Override
    public String execute(HttpServletRequest request) {
        logger.info(COMMAND_REQUESTED_MESSAGE);
        final HttpSession session = request.getSession();
        final String login = request.getParameter(REQUEST_USER_LOGIN_PARAMETER_KEY);
        final User user = (User) session.getAttribute(SESSION_USER_ATTRIBUTE_KEY);
        try {
            final User userWithChangedLogin = userService.changeLogin(user, login);
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(USER_LOGIN_CHANGED_MESSAGE_KEY));
            session.setAttribute(SESSION_USER_ATTRIBUTE_KEY, userWithChangedLogin);
        } catch (UserWithLoginExistsException e) {
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(USER_LOGIN_EXISTS_MESSAGE_KEY));
        }
        logger.info(COMMAND_EXECUTED_MESSAGE);
        return PathManager.getAccountPagePath();
    }

    /**
     * Nested class that encapsulates single {@link ChangeLoginCommand} instance.
     * Singleton pattern variation.
     *
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final ChangeLoginCommand INSTANCE = new ChangeLoginCommand();
    }
}
