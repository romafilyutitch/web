package by.epam.jwd.web.command.action.user;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.resource.PathManager;
import by.epam.jwd.web.service.UserService;
import by.epam.jwd.web.service.WrongLoginException;
import by.epam.jwd.web.service.WrongPasswordException;
import by.epam.jwd.web.validation.Validation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

/**
 * Executes command that is validation login data and login user.
 *
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class LoginCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(LoginCommand.class);
    private final UserService userService = UserService.getInstance();
    private final Validation<User> userValidation = Validation.getUserValidation();
    private static final String COMMAND_REQUESTED_MESSAGE = "Login command was requested";
    private static final String COMMAND_EXECUTED_MESSAGE = "Login command was executed";
    private static final String INVALID_USER_MESSAGE = "Can't login user. Invalid user was get from request";
    private static final String REQUEST_LOGIN_PARAMETER_KEY = "login";
    private static final String REQUEST_PASSWORD_PARAMETER_KEY = "password";
    private static final String REQUEST_MESSAGE_ATTRIBUTE_KEY = "message";
    private static final String SESSION_USER_ATTRIBUTE_KEY = "user";
    private static final String USER_WAS_LOGGED_IN_MESSAGE_KEY = "user.login.loggedIn";
    private static final String WRONG_LOGIN_MESSAGE_KEY = "user.login.wrongLogin";
    private static final String WRONG_PASSWORD_MESSAGE_KEY = "user.login.wrongPassword";

    private LoginCommand() {
    }

    /**
     * Gets single class instance from nested class.
     *
     * @return class instance.
     */
    public static LoginCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Validates user login data an make user login.
     * Request must have user login and user password.
     * Don't login user if user login data is invalid.
     *
     * @param request request that need to be execute.
     * @return login page path for forward.
     */
    @Override
    public String execute(HttpServletRequest request) {
        logger.info(COMMAND_REQUESTED_MESSAGE);
        final User userFromRequest = buildUserFromRequest(request);
        final List<String> validateMessages = userValidation.validate(userFromRequest);
        if (validateMessages.isEmpty()) {
            try {
                final User savedUser = userService.login(userFromRequest);
                final HttpSession session = request.getSession();
                session.setAttribute(SESSION_USER_ATTRIBUTE_KEY, savedUser);
                request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(USER_WAS_LOGGED_IN_MESSAGE_KEY));
            } catch (WrongLoginException e) {
                request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(WRONG_LOGIN_MESSAGE_KEY));
            } catch (WrongPasswordException e) {
                request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(WRONG_PASSWORD_MESSAGE_KEY));
            }
        } else {
            logger.info(INVALID_USER_MESSAGE);
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, validateMessages);
            return PathManager.getLoginPagePath();
        }
        logger.info(COMMAND_EXECUTED_MESSAGE);
        return PathManager.getLoginPagePath();
    }

    private User buildUserFromRequest(HttpServletRequest request) {
        final String login = request.getParameter(REQUEST_LOGIN_PARAMETER_KEY);
        final String password = request.getParameter(REQUEST_PASSWORD_PARAMETER_KEY);
        return new User(login, password);
    }

    /**
     * Nested class that encapsulates single {@link LoginCommand} instance.
     * Singleton pattern variation.
     *
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final LoginCommand INSTANCE = new LoginCommand();
    }
}
