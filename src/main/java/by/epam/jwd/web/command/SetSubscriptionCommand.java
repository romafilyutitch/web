package by.epam.jwd.web.command;

import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.exception.ValidationException;
import by.epam.jwd.web.model.Subscription;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.ServiceFactory;
import by.epam.jwd.web.validator.SubscriptionValidator;

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
    public String execute(HttpServletRequest request) {
        final Long id = Long.valueOf(request.getParameter(ID));
        final String startDate = request.getParameter(START_DATE);
        final String endDate = request.getParameter(END_DATE);
        final Subscription subscription = new Subscription(LocalDate.parse(startDate), LocalDate.parse(endDate));
        try {
            SubscriptionValidator.getInstance().validate(subscription);
            final User user = ServiceFactory.getInstance().getUserService().setSubscription(id, subscription);
            request.getSession().setAttribute(COMMAND_RESULT, String.format(RESULT_MESSAGE, user.getLogin()));
        } catch (ValidationException e) {
            request.getSession().setAttribute(COMMAND_RESULT, e.getMessage());
        }
        return null;
    }

    private static class Singleton {
        private static final SetSubscriptionCommand INSTANCE = new SetSubscriptionCommand();
    }
}
