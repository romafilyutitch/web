package by.epam.jwd.web.command;

import by.epam.jwd.web.service.ServiceException;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class SetSubscriptionCommand implements ActionCommand {

    public static final String ID = "id";
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";
    public static final String COMMAND_RESULT = "commandResult";
    public static final String RESULT_MESSAGE = "subscription for user was set";

    private SetSubscriptionCommand() {
    }

    public static SetSubscriptionCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        final String id = request.getParameter(ID);
        final String startDate = request.getParameter(START_DATE);
        final String end_date = request.getParameter(END_DATE);
        final long parsedLong = Long.parseLong(id);
        try {
            ServiceFactory.getInstance().getUserService().setSubscription(parsedLong, startDate, end_date);
            request.getSession().setAttribute(COMMAND_RESULT, RESULT_MESSAGE);
            return null;
        } catch (ServiceException e) {
            request.getSession().setAttribute(COMMAND_RESULT, e.getMessage());
            return null;
        }
    }

    private static class Singleton {
        private static final SetSubscriptionCommand INSTANCE = new SetSubscriptionCommand();
    }
}
