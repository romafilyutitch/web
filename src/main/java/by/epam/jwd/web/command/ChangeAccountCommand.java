package by.epam.jwd.web.command;

import by.epam.jwd.web.model.User;
import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class ChangeAccountCommand implements ActionCommand {

    public static final String USER = "user";
    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";
    public static final String COMMAND_RESULT = "commandResult";
    public static final String RESULT_MESSAGE = "user data was changed";


    private ChangeAccountCommand() {
    }

    public static ChangeAccountCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER);
        final String newLogin = request.getParameter(LOGIN);
        final String newPassword = request.getParameter(PASSWORD);
        if (newLogin != null && !newLogin.isEmpty()) {
            user = ServiceFactory.getInstance().getUserService().changeLogin(user.getId(), newLogin);
        }
        if (newPassword != null && !newPassword.isEmpty()) {
            user = ServiceFactory.getInstance().getUserService().changePassword(user.getId(), newPassword);
        }
        request.getSession().setAttribute(USER, user);
        request.getSession().setAttribute(COMMAND_RESULT, RESULT_MESSAGE);
        return null;
    }

    private static class Singleton {
        private static final ChangeAccountCommand INSTANCE = new ChangeAccountCommand();
    }
}
