package by.epam.jwd.web.command;

import by.epam.jwd.web.exception.UserWithLoginExistsException;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.ServiceFactory;
import by.epam.jwd.web.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ChangeLoginCommand implements ActionCommand {
    private final UserService userService = ServiceFactory.getInstance().getUserService();

    private static final String REQUEST_USER_LOGIN_PARAMETER_KEY = "login";
    private static final String REQUEST_ERROR_ATTRIBUTE_KEY = "error";
    private static final String SESSION_USER_ATTRIBUTE_KEY = "user";
    private static final String SESSION_SUCCESS_ATTRIBUTE_KEY = "success";
    private static final String LOGIN_CHANGED_LOCALIZATION_MESSAGE_KEY = "loginChanged";
    private static final String LOGIN_EXISTS_LOCALIZATION_MESSAGE_KEY = "loginExists";

    private static final String SUCCESS_RESULT_PATH = "index.jsp";
    private static final String ERROR_RESULT_PATH = "WEB-INF/jsp/account.jsp";

    private ChangeLoginCommand() {
    }

    public static ChangeLoginCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final String login = request.getParameter(REQUEST_USER_LOGIN_PARAMETER_KEY);
        final HttpSession session = request.getSession();
        final User user = (User) session.getAttribute(SESSION_USER_ATTRIBUTE_KEY);
        try {
            final User userWithChangedLogin = userService.changeLogin(user, login);
            session.setAttribute(SESSION_SUCCESS_ATTRIBUTE_KEY, LOGIN_CHANGED_LOCALIZATION_MESSAGE_KEY);
            session.setAttribute(SESSION_USER_ATTRIBUTE_KEY, userWithChangedLogin);
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
        } catch (UserWithLoginExistsException e) {
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
        }
    }

    private static class Singleton {
        private static final ChangeLoginCommand INSTANCE = new ChangeLoginCommand();
    }
}
