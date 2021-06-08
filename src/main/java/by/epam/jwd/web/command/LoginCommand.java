package by.epam.jwd.web.command;

import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.ServiceException;
import by.epam.jwd.web.service.SimpleUserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LoginCommand implements ActionCommand{
    private final SimpleUserService userService = SimpleUserService.getInstance();

    @Override
    public String execute(HttpServletRequest request) {
        String login = request.getParameter("login");
        String pass = request.getParameter("password");
        final User user = new User(login, pass);
        try {
            final User savedUser = userService.login(user);
            final HttpSession session = request.getSession();
            session.setAttribute("user", savedUser);
            return "index.jsp";
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            return "WEB-INF/jsp/login.jsp";
        }
    }
}
