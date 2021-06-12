package by.epam.jwd.web.command;

import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.ServiceException;
import by.epam.jwd.web.service.SimpleUserService;

import javax.servlet.http.HttpServletRequest;

public class ChangeAccountCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        final String newLogin = request.getParameter("login");
        final String newPassword = request.getParameter("password");

        try {
            if (newLogin != null && !newLogin.isEmpty()) {
                user = SimpleUserService.getInstance().changeLogin(user.getId(), newLogin);
            }
            if (newPassword != null && !newPassword.isEmpty()) {
                user = SimpleUserService.getInstance().changePassword(user.getId(), newPassword);
            }
            request.getSession().setAttribute("user", user);
            return "controller?command=show_account";
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            return "controller?command=show_account";
        }
    }
}
