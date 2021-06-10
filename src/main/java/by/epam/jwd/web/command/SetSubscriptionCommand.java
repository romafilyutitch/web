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
            return "controller?command=show_users";
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            return "controller?command=show_set_subscription_page";
        }
    }
}
