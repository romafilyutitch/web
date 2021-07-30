package by.epam.jwd.web.command;

import by.epam.jwd.web.exception.NoUserWithLoginException;
import by.epam.jwd.web.exception.WrongPasswordException;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.model.UserRole;
import by.epam.jwd.web.service.ServiceFactory;
import by.epam.jwd.web.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;

public class LoginCommand implements ActionCommand {
    private final UserService userService = ServiceFactory.getInstance().getUserService();

    private static final String REQUEST_ERROR_ATTRIBUTE_KEY = "error";
    private static final String REQUEST_LOGIN_PARAMETER_KEY = "login";
    private static final String REQUEST_PASSWORD_PARAMETER_KEY = "password";
    private static final String SESSION_USER_ATTRIBUTE_KEY = "user";
    private static final String SESSION_CURRENT_DATE_ATTRIBUTE_KEY = "currentDate";
    private static final String WRONG_PASSWORD_LOCALIZATION_MESSAGE_KEY = "wrongPassword";
    private static final String NO_LOGIN_LOCALIZATION_MESSAGE_KEY = "noLogin";
    private static final String REQUEST_SUCCESS_ATTRIBUTE_KEY = "success";
    private static final String USER_WAS_LOGGED_IN_LOCALIZATION_MESSAGE_KEY = "userWasLoggedIn";

    private static final String RESULT_PATH = "WEB-INF/jsp/login.jsp";

    private LoginCommand() {
    }

    public static LoginCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        String login = request.getParameter(REQUEST_LOGIN_PARAMETER_KEY);
        String password = request.getParameter(REQUEST_PASSWORD_PARAMETER_KEY);
        final User user = new User(login, password);
        try {
            final User savedUser = userService.login(user);
            final HttpSession session = request.getSession();
            session.setAttribute(SESSION_USER_ATTRIBUTE_KEY, savedUser);
            session.setAttribute(SESSION_CURRENT_DATE_ATTRIBUTE_KEY, LocalDate.now());
            request.setAttribute(REQUEST_SUCCESS_ATTRIBUTE_KEY, USER_WAS_LOGGED_IN_LOCALIZATION_MESSAGE_KEY);
        } catch (NoUserWithLoginException e) {
            request.setAttribute(REQUEST_ERROR_ATTRIBUTE_KEY, NO_LOGIN_LOCALIZATION_MESSAGE_KEY);
        } catch (WrongPasswordException e) {
            request.setAttribute(REQUEST_ERROR_ATTRIBUTE_KEY, WRONG_PASSWORD_LOCALIZATION_MESSAGE_KEY);
        }
        return new CommandResult() {
            @Override
            public String getResultPath() {
                return RESULT_PATH;
            }

            @Override
            public boolean isRedirect() {
                return false;
            }
        };
    }

    private static class Singleton {
        private static final LoginCommand INSTANCE = new LoginCommand();
    }
}
