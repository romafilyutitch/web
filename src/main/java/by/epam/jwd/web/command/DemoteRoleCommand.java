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
            return "controller?command=show_users";
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            return "controller?command=show_users";
        }
    }
}
