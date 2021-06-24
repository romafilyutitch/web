package by.epam.jwd.web.command;

import by.epam.jwd.web.exception.SubscriptionException;
import by.epam.jwd.web.model.Subscription;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

public class SetSubscriptionCommand implements ActionCommand {

    public static final String ID = "id";
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";
    public static final String COMMAND_RESULT = "commandResult";
    public static final String RESULT_MESSAGE = "subscription for user %s was set";

    private SetSubscriptionCommand() {
    }

    public static SetSubscriptionCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final Long id = Long.valueOf(request.getParameter("id"));
        final String startDate = request.getParameter("start_date");
        final String endDate = request.getParameter("end_date");
        final Subscription subscription = new Subscription(LocalDate.parse(startDate), LocalDate.parse(endDate));
        try {
            final User user = ServiceFactory.getInstance().getUserService().setSubscription(id, subscription);
            request.getSession().setAttribute("success", String.format("Subscription for user %s was set", user.getLogin()));
        } catch (SubscriptionException e) {
            request.getSession().setAttribute("fail", e.getMessage());
        }
        return new CommandResult() {
            @Override
            public String getResultPath() {
                return "index.jsp";
            }

            @Override
            public boolean isRedirect() {
                return true;
            }
        };
    }

    private static class Singleton {
        private static final SetSubscriptionCommand INSTANCE = new SetSubscriptionCommand();
    }
}
