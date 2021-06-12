package by.epam.jwd.web.command;

import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ShowUsersListCommand implements ActionCommand {

    public static final String USERS = "users";
    public static final String USERS_JSP_PATH = "WEB-INF/jsp/users.jsp";

    private ShowUsersListCommand() {
    }

    public static ShowUsersListCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        final List<User> allUsers = ServiceFactory.getInstance().getUserService().findAll();
        request.setAttribute(USERS, allUsers);
        return USERS_JSP_PATH;
    }

    private static class Singleton {
        private static final ShowUsersListCommand INSTANCE = new ShowUsersListCommand();
    }
}
