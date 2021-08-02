package by.epam.jwd.web.command;

import by.epam.jwd.web.exception.InvalidSubscriptionException;
import by.epam.jwd.web.model.Subscription;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.resource.ConfigurationManager;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.service.ServiceFactory;
import by.epam.jwd.web.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class SetSubscriptionCommand implements ActionCommand {
    private final UserService userService = ServiceFactory.getInstance().getUserService();

    private static final String REQUEST_USER_ID_PARAMETER_KEY = "id";
    private static final String REQUEST_START_DATE_PARAMETER_KEY = "start_date";
    private static final String REQUEST_END_DATE_PARAMETER_KEY = "end_date";
    private static final String REQUEST_MESSAGE_ATTRIBUTE_KEY = "message";
    private static final String SUBSCRIPTION_REGISTERED_MESSAGE_KEY = "subscription.registered";
    private static final String INVALID_SUBSCRIPTION_MESSAGE_KEY = "subscription.invalid";

    private static final String LOCAL_DATE_PARSE_PATTERN = "dd.MM.yyyy";

    private SetSubscriptionCommand() {
    }

    public static SetSubscriptionCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        final Subscription subscriptionFromRequest = buildSubscriptionFromRequest(request);
        final Long userId = Long.valueOf(request.getParameter(REQUEST_USER_ID_PARAMETER_KEY));
        final User foundUser = userService.findById(userId);
        try {
            userService.setSubscription(foundUser, subscriptionFromRequest);
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(SUBSCRIPTION_REGISTERED_MESSAGE_KEY));
        } catch (InvalidSubscriptionException e) {
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(INVALID_SUBSCRIPTION_MESSAGE_KEY));
        }
        return ConfigurationManager.getShowUsersCommand();
    }

    private Subscription buildSubscriptionFromRequest(HttpServletRequest request) {
        final String startDate = request.getParameter(REQUEST_START_DATE_PARAMETER_KEY);
        final String endDate = request.getParameter(REQUEST_END_DATE_PARAMETER_KEY);
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(LOCAL_DATE_PARSE_PATTERN).withLocale(Locale.getDefault());
        return new Subscription(LocalDate.parse(startDate, formatter), LocalDate.parse(endDate, formatter));
    }

    private static class Singleton {
        private static final SetSubscriptionCommand INSTANCE = new SetSubscriptionCommand();
    }
}
