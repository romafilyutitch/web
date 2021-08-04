package by.epam.jwd.web.command.action.user;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.exception.WrongLoginException;
import by.epam.jwd.web.exception.WrongPasswordException;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.resource.ConfigurationManager;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.service.ServiceFactory;
import by.epam.jwd.web.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;

public class LoginCommand implements ActionCommand {
    private final UserService userService = ServiceFactory.getInstance().getUserService();

    private static final String REQUEST_LOGIN_PARAMETER_KEY = "login";
    private static final String REQUEST_PASSWORD_PARAMETER_KEY = "password";
    private static final String REQUEST_MESSAGE_ATTRIBUTE_KEY = "message";
    private static final String SESSION_USER_ATTRIBUTE_KEY = "user";
    private static final String SESSION_CURRENT_DATE_ATTRIBUTE_KEY = "currentDate";
    private static final String USER_WAS_LOGGED_IN_MESSAGE_KEY = "user.login.loggedIn";
    private static final String WRONG_LOGIN_MESSAGE_KEY = "user.login.wrongLogin";
    private static final String WRONG_PASSWORD_MESSAGE_KEY = "user.login.wrongPassword";

    private LoginCommand() {
    }

    public static LoginCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        final String login = request.getParameter(REQUEST_LOGIN_PARAMETER_KEY);
        final String password = request.getParameter(REQUEST_PASSWORD_PARAMETER_KEY);
        final User user = new User(login, password);
        try {
            final User savedUser = userService.login(user);
            final HttpSession session = request.getSession();
            session.setAttribute(SESSION_USER_ATTRIBUTE_KEY, savedUser);
            session.setAttribute(SESSION_CURRENT_DATE_ATTRIBUTE_KEY, LocalDate.now());
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(USER_WAS_LOGGED_IN_MESSAGE_KEY));
        } catch (WrongLoginException e) {
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(WRONG_LOGIN_MESSAGE_KEY));
        } catch (WrongPasswordException e) {
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(WRONG_PASSWORD_MESSAGE_KEY));
        }
        return ConfigurationManager.getLoginPagePath();
    }

    private static class Singleton {
        private static final LoginCommand INSTANCE = new LoginCommand();
    }
}
