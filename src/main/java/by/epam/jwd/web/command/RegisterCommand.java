package by.epam.jwd.web.command;

import by.epam.jwd.web.model.User;
import by.epam.jwd.web.model.UserRole;
import by.epam.jwd.web.service.ServiceFactory;
import by.epam.jwd.web.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class RegisterCommand implements ActionCommand {
    private final UserService userService = ServiceFactory.getInstance().getUserService();

    private static final String REQUEST_LOGIN_PARAMETER_KEY = "login";
    private static final String REQUEST_PASSWORD_PARAMETER_KEY = "password";
    private static final String REQUEST_ERROR_ATTRIBUTE_KEY = "error";
    private static final String SESSION_USER_ATTRIBUTE_KEY = "user";
    private static final String LOGIN_EXISTS_LOCALIZATION_MESSAGE_KEY = "loginExists";

    private static final String SUCCESS_RESULT_PATH = "index.jsp";
    private static final String ERROR_RESULT_PATH = "WEB-INF/jsp/register.jsp";

    private RegisterCommand() {
    }

    public static RegisterCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final String login = request.getParameter(REQUEST_LOGIN_PARAMETER_KEY);
        final String password = request.getParameter(REQUEST_PASSWORD_PARAMETER_KEY);
        final User user = new User(login, password, UserRole.READER, null);
        final Optional<User> optionalUserByLogin = userService.findByLogin(login);
        if (optionalUserByLogin.isPresent()) {
            request.setAttribute(REQUEST_ERROR_ATTRIBUTE_KEY, LOGIN_EXISTS_LOCALIZATION_MESSAGE_KEY);
            return new CommandResult() {
                @Override
                public String getResultPath() {
                    return ERROR_RESULT_PATH;
                }

                @Override
                public boolean isRedirect() {
                    return false;
                }
            };
        } else {
            final User registeredUser = userService.register(user);
            request.getSession().setAttribute(SESSION_USER_ATTRIBUTE_KEY, registeredUser);
            return new CommandResult() {
                @Override
                public String getResultPath() {
                    return SUCCESS_RESULT_PATH;
                }

                @Override
                public boolean isRedirect() {
                    return true;
                }
            };
        }
    }

    private static class Singleton {
        private static final RegisterCommand INSTANCE = new RegisterCommand();
    }
}
