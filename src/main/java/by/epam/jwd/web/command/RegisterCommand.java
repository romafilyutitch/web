package by.epam.jwd.web.command;

import by.epam.jwd.web.exception.RegisterException;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.model.UserRole;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class RegisterCommand implements ActionCommand {
    private static final String REQUEST_LOGIN_PARAMETER_KEY = "login";
    private static final String REQUEST_PASSWORD_PARAMETER_KEY = "password";
    private static final String REQUEST_ERROR_ATTRIBUTE_KEY = "error";

    private static final String SESSION_USER_ATTRIBUTE_KEY = "user";

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
        try {
            final User registeredUser = ServiceFactory.getInstance().getUserService().register(user);
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
        } catch (RegisterException e) {
            request.setAttribute(REQUEST_ERROR_ATTRIBUTE_KEY, "loginExists");
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
        }
    }

    private static class Singleton {
        private static final RegisterCommand INSTANCE = new RegisterCommand();
    }
}
