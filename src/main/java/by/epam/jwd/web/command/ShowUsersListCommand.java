package by.epam.jwd.web.command;

import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.SimpleUserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ShowUsersListCommand implements ActionCommand {
    private static final SimpleUserService USER_SERVICE = SimpleUserService.getInstance();
    @Override
    public String execute(HttpServletRequest request) {
        final List<User> allUsers = USER_SERVICE.findAll();
        request.setAttribute("users", allUsers);
        return "WEB-INF/jsp/users.jsp";
    }
}
