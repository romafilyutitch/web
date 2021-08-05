package by.epam.jwd.web.command.action.subscription;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.exception.InvalidSubscriptionException;
import by.epam.jwd.web.model.Subscription;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.resource.CommandManager;
import by.epam.jwd.web.resource.PathManager;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.service.ServiceFactory;
import by.epam.jwd.web.service.UserService;
import by.epam.jwd.web.validation.Validation;
import by.epam.jwd.web.validation.ValidationFactory;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Executes command that is set {@link Subscription} to definite {@link User}.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class SetSubscriptionCommand implements ActionCommand {
    private final UserService userService = ServiceFactory.getInstance().getUserService();
    private final Validation<Subscription> subscriptionValidation = ValidationFactory.getInstance().getSubscriptionValidation();

    private static final String REQUEST_USER_ID_PARAMETER_KEY = "id";
    private static final String REQUEST_START_DATE_PARAMETER_KEY = "start_date";
    private static final String REQUEST_END_DATE_PARAMETER_KEY = "end_date";
    private static final String REQUEST_MESSAGE_ATTRIBUTE_KEY = "message";
    private static final String SUBSCRIPTION_REGISTERED_MESSAGE_KEY = "subscription.registered";
    private static final String INVALID_SUBSCRIPTION_MESSAGE_KEY = "subscription.invalid";

    private static final String LOCAL_DATE_PARSE_PATTERN = "dd.MM.yyyy";

    private SetSubscriptionCommand() {
    }

    /**
     * Gets single class instance from nested class.
     * @return class instance.
     */
    public static SetSubscriptionCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Sets subscription do definite in request {@link User}.
     * Request must contain id of user that need to set {@link Subscription}
     * and valid {@link Subscription} start date and end date.
     * @param request request that need to be execute.
     * @return show users command for forward.
     */
    @Override
    public String execute(HttpServletRequest request) {
        final Subscription subscriptionFromRequest = buildSubscriptionFromRequest(request);
        final List<String> validationMessages = subscriptionValidation.validate(subscriptionFromRequest);
        if (!validationMessages.isEmpty()) {
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, validationMessages);
            return CommandManager.getCommand("show.users");
        }
        final Long userId = Long.valueOf(request.getParameter(REQUEST_USER_ID_PARAMETER_KEY));
        final User foundUser = userService.findById(userId);
        userService.setSubscription(foundUser, subscriptionFromRequest);
        request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(SUBSCRIPTION_REGISTERED_MESSAGE_KEY));
        return CommandManager.getCommand("show.users");
    }


    private Subscription buildSubscriptionFromRequest(HttpServletRequest request) {
        final String startDate = request.getParameter(REQUEST_START_DATE_PARAMETER_KEY);
        final String endDate = request.getParameter(REQUEST_END_DATE_PARAMETER_KEY);
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(LOCAL_DATE_PARSE_PATTERN).withLocale(Locale.getDefault());
        return new Subscription(LocalDate.parse(startDate, formatter), LocalDate.parse(endDate, formatter));
    }

    /**
     * Nested class that encapsulated single {@link SetSubscriptionCommand} instance.
     * Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final SetSubscriptionCommand INSTANCE = new SetSubscriptionCommand();
    }
}
