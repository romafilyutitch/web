package by.epam.jwd.web.command;

import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.ServiceException;
import by.epam.jwd.web.service.SimpleUserService;
import by.epam.jwd.web.service.UserService;

import javax.servlet.http.HttpServletRequest;

public class RegisterCommand implements ActionCommand {
    private static final UserService USER_SERVICE = SimpleUserService.getInstance();

    @Override
    public String execute(HttpServletRequest request) {
        final String login = request.getParameter("login");
        final String password = request.getParameter("password");
        try {
            final User createdUser = USER_SERVICE.createUser(login, password);
            request.setAttribute("user", createdUser);
            return null;
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            return "WEB-INF/jsp/register.jsp";
        }
    }
}
