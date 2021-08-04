package by.epam.jwd.web.validation;

import by.epam.jwd.web.model.Subscription;
import by.epam.jwd.web.resource.MessageManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class SubscriptionValidation implements Validation<Subscription> {

    private static final String INVALID_SUBSCRIPTION_DATES_RANGE_MESSAGE_KEY = "subscription.validation.range.invalid";

    private SubscriptionValidation() {
    }

    static SubscriptionValidation getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public List<String> validate(Subscription subscription) {
        final List<String> messages = new ArrayList<>();
        final LocalDate startDate = subscription.getStartDate();
        final LocalDate endDate = subscription.getEndDate();
        if (startDate == null || endDate == null || startDate.isAfter(endDate)) {
            messages.add(MessageManager.getMessage(INVALID_SUBSCRIPTION_DATES_RANGE_MESSAGE_KEY));
        }
        return messages;
    }

    private static class Singleton {
        private static final SubscriptionValidation INSTANCE = new SubscriptionValidation();
    }
}
