package by.epam.jwd.web.command;

import by.epam.jwd.web.model.User;
import by.epam.jwd.web.resource.ConfigurationManager;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.service.ServiceFactory;
import by.epam.jwd.web.service.UserService;
import by.epam.jwd.web.validation.Validation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

public class RegisterCommand implements ActionCommand {
    private final UserService userService = ServiceFactory.getInstance().getUserService();
    private final Validation<User> userValidation = Validation.getUserValidation();

    private static final String REQUEST_LOGIN_PARAMETER_KEY = "login";
    private static final String REQUEST_PASSWORD_PARAMETER_KEY = "password";
    private static final String REQUEST_MESSAGE_ATTRIBUTE_KEY = "message";
    private static final String SESSION_USER_ATTRIBUTE_KEY = "user";
    private static final String USER_WITH_ENTERED_LOGIN_EXITS_MESSAGE_KEY = "user.register.exists";
    private static final String USER_WAS_REGISTERED_MESSAGE_KEY = "user.registered";

    private RegisterCommand() {
    }

    public static RegisterCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        final HttpSession currentSession = request.getSession();
        final String login = request.getParameter(REQUEST_LOGIN_PARAMETER_KEY);
        final String password = request.getParameter(REQUEST_PASSWORD_PARAMETER_KEY);
        final User user = new User(login, password);
        final List<String> validationMessages = userValidation.validate(user);
        if (!validationMessages.isEmpty()) {
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, validationMessages);
            return ConfigurationManager.getRegisterPagePath();
        }
        final Optional<User> optionalUserByLogin = userService.findByLogin(login);
        if (optionalUserByLogin.isPresent()) {
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(USER_WITH_ENTERED_LOGIN_EXITS_MESSAGE_KEY));
        } else {
            final User registeredUser = userService.save(user);
            currentSession.setAttribute(SESSION_USER_ATTRIBUTE_KEY, registeredUser);
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(USER_WAS_REGISTERED_MESSAGE_KEY));
        }
        return ConfigurationManager.getRegisterPagePath();
    }

    private static class Singleton {
        private static final RegisterCommand INSTANCE = new RegisterCommand();
    }
}
