package by.epam.jwd.web.command;

import by.epam.jwd.web.dao.UserDao;
import by.epam.jwd.web.service.ServiceException;
import by.epam.jwd.web.service.SimpleUserService;

import javax.servlet.http.HttpServletRequest;

public class SetSubscriptionCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request) {
        final String id = request.getParameter("id");
        final String startDate = request.getParameter("start_date");
        final String end_date = request.getParameter("end_date");
        final long parsedLong = Long.parseLong(id);
        try {
            SimpleUserService.getInstance().setSubscription(parsedLong, startDate, end_date);
            request.getSession().setAttribute("commandResult", "subscription for user was set");
            return null;
        } catch (ServiceException e) {
            request.getSession().setAttribute("commandResult", e.getMessage());
            return null;
        }
    }
}
