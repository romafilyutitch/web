package by.epam.jwd.web.command.action.user;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.resource.PathManager;
import by.epam.jwd.web.service.UserService;
import by.epam.jwd.web.validation.Validation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

/**
 * Executes command that is validation user register data and register new user.
 *
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class RegisterCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(RegisterCommand.class);
    private final UserService userService = UserService.getInstance();
    private final Validation<User> userValidation = Validation.getUserValidation();
    private static final String COMMAND_REQUESTED_MESSAGE = "Register command was requested";
    private static final String COMMAND_EXECUTED_MESSAGE = "Register command was executed";
    private static final String INVALID_USER_MESSAGE = "Can't register user. Invalid user was get from request";
    private static final String REQUEST_LOGIN_PARAMETER_KEY = "login";
    private static final String REQUEST_PASSWORD_PARAMETER_KEY = "password";
    private static final String REQUEST_MESSAGE_ATTRIBUTE_KEY = "message";
    private static final String SESSION_USER_ATTRIBUTE_KEY = "user";
    private static final String USER_WITH_ENTERED_LOGIN_EXITS_MESSAGE_KEY = "user.register.exists";
    private static final String USER_WAS_REGISTERED_MESSAGE_KEY = "user.registered";

    private RegisterCommand() {
    }

    /**
     * Gets single class instance from nested class.
     *
     * @return class instance.
     */
    public static RegisterCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Validates register user data and register new user
     * if data is valid or not register user otherwise.
     *
     * @param request request that need to be execute.
     * @return register page path for forward.
     */
    @Override
    public String execute(HttpServletRequest request) {
        logger.info(COMMAND_REQUESTED_MESSAGE);
        final User userFromRequest = buildUserFromRequest(request);
        final List<String> validationMessages = userValidation.validate(userFromRequest);
        if (validationMessages.isEmpty()) {
            final Optional<User> optionalUserByLogin = userService.findByLogin(userFromRequest.getLogin());
            if (optionalUserByLogin.isPresent()) {
                request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(USER_WITH_ENTERED_LOGIN_EXITS_MESSAGE_KEY));
            } else {
                final User registeredUser = userService.save(userFromRequest);
                final HttpSession currentSession = request.getSession();
                currentSession.setAttribute(SESSION_USER_ATTRIBUTE_KEY, registeredUser);
                request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(USER_WAS_REGISTERED_MESSAGE_KEY));
            }
        } else {
            logger.info(INVALID_USER_MESSAGE);
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, validationMessages);
        }
        logger.info(COMMAND_EXECUTED_MESSAGE);
        return PathManager.getRegisterPagePath();
    }

    private User buildUserFromRequest(HttpServletRequest request) {
        final String login = request.getParameter(REQUEST_LOGIN_PARAMETER_KEY);
        final String password = request.getParameter(REQUEST_PASSWORD_PARAMETER_KEY);
        return new User(login, password);
    }

    /**
     * Nested class that encapsulates {@link RegisterCommand} instance.
     * Singleton pattern variation.
     *
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final RegisterCommand INSTANCE = new RegisterCommand();
    }
}
