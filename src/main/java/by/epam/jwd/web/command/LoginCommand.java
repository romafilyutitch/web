package by.epam.jwd.web.command;

import by.epam.jwd.web.exception.NoLoginException;
import by.epam.jwd.web.exception.WrongPasswordException;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;

public class LoginCommand implements ActionCommand {

    private static final String REQUEST_ERROR_ATTRIBUTE_KEY = "error";
    private static final String REQUEST_LOGIN_PARAMETER_KEY = "login";
    private static final String REQUEST_PASSWORD_PARAMETER_KEY = "password";

    private static final String SESSION_USER_ATTRIBUTE_KEY = "user";
    private static final String SESSION_CURRENT_DATE_ATTRIBUTE_KEY = "currentDate";

    private static final String SUCCESS_RESULT_PATH = "index.jsp";
    private static final String ERROR_RESULT_PATH = "WEB-INF/jsp/login.jsp";

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
            final User savedUser = ServiceFactory.getInstance().getUserService().loginUser(user);
            final HttpSession session = request.getSession();
            session.setAttribute(SESSION_USER_ATTRIBUTE_KEY, savedUser);
            session.setAttribute(SESSION_CURRENT_DATE_ATTRIBUTE_KEY, LocalDate.now());
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
        } catch (NoLoginException e) {
            request.setAttribute(REQUEST_ERROR_ATTRIBUTE_KEY, "noLogin");
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
        } catch (WrongPasswordException e) {
            request.setAttribute(REQUEST_ERROR_ATTRIBUTE_KEY, "wrongPassword");
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
        private static final LoginCommand INSTANCE = new LoginCommand();
    }
}
