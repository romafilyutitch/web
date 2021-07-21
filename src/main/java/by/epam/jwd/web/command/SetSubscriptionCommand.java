package by.epam.jwd.web.command;

import by.epam.jwd.web.exception.InvalidSubscriptionException;
import by.epam.jwd.web.model.Subscription;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.ServiceFactory;
import by.epam.jwd.web.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

public class SetSubscriptionCommand implements ActionCommand {
    private final UserService userService = ServiceFactory.getInstance().getUserService();

    private static final String REQUEST_USER_ID_PARAMETER_KEY = "id";
    private static final String REQUEST_START_DATE_PARAMETER_KEY = "start_date";
    private static final String REQUEST_END_DATE_PARAMETER_KEY = "end_date";
    private static final String SESSION_SUCCESS_ATTRIBUTE_KEY = "success";
    private static final String SESSION_FAIL_ATTRIBUTE_KEY = "fail";
    private static final String WRONG_SUBSCRIPTION_LOCALIZATION_MESSAGE_KEY = "wrongSubscription";
    private static final String SUBSCRIPTION_WAS_SET_LOCALIZATION_MESSAGE_KEY = "subscriptionSet";

    private static final String RESULT_PATH = "index.jsp";

    private SetSubscriptionCommand() {
    }

    public static SetSubscriptionCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final Long id = Long.valueOf(request.getParameter(REQUEST_USER_ID_PARAMETER_KEY));
        final User foundUser = userService.findById(id);
        final String startDate = request.getParameter(REQUEST_START_DATE_PARAMETER_KEY);
        final String endDate = request.getParameter(REQUEST_END_DATE_PARAMETER_KEY);
        final Subscription subscription = new Subscription(LocalDate.parse(startDate), LocalDate.parse(endDate));
        try {
            userService.setSubscription(foundUser, subscription);
            request.getSession().setAttribute(SESSION_SUCCESS_ATTRIBUTE_KEY, SUBSCRIPTION_WAS_SET_LOCALIZATION_MESSAGE_KEY);
        } catch (InvalidSubscriptionException e) {
            request.getSession().setAttribute(SESSION_FAIL_ATTRIBUTE_KEY, WRONG_SUBSCRIPTION_LOCALIZATION_MESSAGE_KEY);
        }
        return new CommandResult() {
            @Override
            public String getResultPath() {
                return RESULT_PATH;
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
