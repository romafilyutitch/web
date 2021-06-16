package by.epam.jwd.web.command;

import by.epam.jwd.web.exception.LoginException;
import by.epam.jwd.web.exception.ValidationException;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.ServiceFactory;
import by.epam.jwd.web.validator.UserValidator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LoginCommand implements ActionCommand {

    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";
    public static final String USER = "user";
    public static final String ERROR = "error";
    public static final String LOGIN_JSP_PATH = "WEB-INF/jsp/login.jsp";

    private LoginCommand() {
    }

    public static LoginCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        String login = request.getParameter(LOGIN);
        String password = request.getParameter(PASSWORD);
        final User user = new User(login ,password);
        try {
            UserValidator.getInstance().validate(user);
            final User savedUser = ServiceFactory.getInstance().getUserService().loginUser(user);
            final HttpSession session = request.getSession();
            session.setAttribute(USER, savedUser);
            return null;
        } catch (ValidationException | LoginException e) {
            request.setAttribute(ERROR, e.getMessage());
            return LOGIN_JSP_PATH;
        }
    }

    private static class Singleton {
        private static final LoginCommand INSTANCE = new LoginCommand();
    }
}
