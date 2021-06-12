package by.epam.jwd.web.command;

import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.ServiceException;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class ShowSetSubscriptionPageCommand implements ActionCommand {

    public static final String ID = "id";
    public static final String USER = "user";
    public static final String SUBSCRIPTION_JSP_PATH = "WEB-INF/jsp/subscription.jsp";
    public static final String ERROR = "error";
    public static final String SHOW_USERS_CONTROLLER = "controller?command=show_users";

    private ShowSetSubscriptionPageCommand() {
    }

    public static ShowSetSubscriptionPageCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        final long id = Long.parseLong(request.getParameter(ID));
        try {
            final User user = ServiceFactory.getInstance().getUserService().findById(id);
            request.setAttribute(USER, user);
            return SUBSCRIPTION_JSP_PATH;
        } catch (ServiceException e) {
            request.setAttribute(ERROR, e.getMessage());
            return SHOW_USERS_CONTROLLER;
        }
    }

    private static class Singleton {
        private static final ShowSetSubscriptionPageCommand INSTANCE = new ShowSetSubscriptionPageCommand();
    }
}
