package by.epam.jwd.web.command;

import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.ServiceException;
import by.epam.jwd.web.service.SimpleUserService;

import javax.servlet.http.HttpServletRequest;

public class ShowSetSubscriptionPageCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request) {
        final long id = Long.parseLong(request.getParameter("id"));
        try {
            final User user = SimpleUserService.getInstance().findById(id);
            request.setAttribute("user", user);
            return "WEB-INF/jsp/subscription.jsp";
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            return "controller?command=show_users";
        }
    }
}
