package by.epam.jwd.web.command;

import by.epam.jwd.web.exception.UserWithLoginExistsException;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.service.ServiceFactory;
import by.epam.jwd.web.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ChangeLoginCommand implements ActionCommand {
    private final UserService userService = ServiceFactory.getInstance().getUserService();

    private static final String REQUEST_USER_LOGIN_PARAMETER_KEY = "login";
    private static final String REQUEST_MESSAGE_ATTRIBUTE_KEY = "message";
    private static final String SESSION_USER_ATTRIBUTE_KEY = "user";
    private static final String USER_LOGIN_CHANGED_MESSAGE_KEY = "user.login.changed";
    private static final String USER_LOGIN_EXISTS_MESSAGE_KEY = "user.login.exists";

    private static final String RESULT_PATH = "WEB-INF/jsp/account.jsp";

    private ChangeLoginCommand() {
    }

    public static ChangeLoginCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
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
        return RESULT_PATH;
    }

    private static class Singleton {
        private static final ChangeLoginCommand INSTANCE = new ChangeLoginCommand();
    }
}
