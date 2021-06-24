package by.epam.jwd.web.command;

import by.epam.jwd.web.exception.LoginException;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;

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
    public CommandResult execute(HttpServletRequest request) {
        String login = request.getParameter("login");
        String password = request.getParameter("password");
        final User user = new User(login ,password);
        try {
            final User savedUser = ServiceFactory.getInstance().getUserService().loginUser(user);
            final HttpSession session = request.getSession();
            session.setAttribute(USER, savedUser);
            session.setAttribute("currentDate", LocalDate.now());
            return new CommandResult() {
                @Override
                public String getResultPath() {
                    return "index.jsp";
                }

                @Override
                public boolean isRedirect() {
                    return true;
                }
            };
        } catch (LoginException e) {
            request.setAttribute(ERROR, e.getMessage());
            return new CommandResult() {
                @Override
                public String getResultPath() {
                    return "WEB-INF/jsp/login.jsp";
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
