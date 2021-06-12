package by.epam.jwd.web.command;

import by.epam.jwd.web.service.ServiceException;
import by.epam.jwd.web.service.SimpleUserService;

import javax.servlet.http.HttpServletRequest;

public class DemoteRoleCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request) {
        final String id = request.getParameter("id");
        final long parsedLong = Long.parseLong(id);
        try {
            SimpleUserService.getInstance().demoteUserRole(parsedLong);
            request.getSession().setAttribute("commandResult", "role for user was demoted");
            return null;
        } catch (ServiceException e) {
            request.getSession().setAttribute("commandResult", e.getMessage());
            return null;
        }
    }
}
