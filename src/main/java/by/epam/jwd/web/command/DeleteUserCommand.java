package by.epam.jwd.web.command;

import by.epam.jwd.web.service.ServiceException;
import by.epam.jwd.web.service.SimpleUserService;

import javax.servlet.http.HttpServletRequest;

public class DeleteUserCommand implements ActionCommand {

    @Override
    public String execute(HttpServletRequest request) {
        final String id = request.getParameter("id");
        final long parsedId = Long.parseLong(id);
        try {
            SimpleUserService.getInstance().deleteUser(parsedId);
            request.getSession().setAttribute("commandResult", "user was deleted");
            return null;
        } catch (ServiceException e) {
            request.getSession().setAttribute("commandResult", e.getMessage());
            return null;
        }
    }
}
