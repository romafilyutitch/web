package by.epam.jwd.web.command;

import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.SimpleUserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ShowOrdersListCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request) {
        final List<User> allUsers = SimpleUserService.getInstance().findAll();
        request.setAttribute("users", allUsers);
        return "WEB-INF/jsp/orders.jsp";
    }
}
