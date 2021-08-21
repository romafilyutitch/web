package by.epam.jwd.web.validation;

import by.epam.jwd.web.model.Subscription;
import by.epam.jwd.web.resource.MessageManager;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Validation for subscription entity. Validates subscription instance
 * and form invalid message list if entity is invalid. If invalid message
 * list is empty that means that entity is valid.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
class SubscriptionValidation implements Validation<Subscription> {

    private static final String INVALID_SUBSCRIPTION_DATES_RANGE_MESSAGE_KEY = "subscription.validation.range.invalid";

    private SubscriptionValidation() {
    }

    /**
     * Gets single class instance from nested class.
     * @return class instance.
     */
    public static SubscriptionValidation getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Validates subscription instance. If start date is after end date
     * that means that subscription dates range is invalid and.
     * Subscription is valid if its start date is before its end date.
     * @param subscription that need to be validated.
     * @return invalid messages list. If invalid messages list is
     * empty that means that entity is valid.
     */
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

    /**
     * Nested class that encapsulates single {@link SubscriptionValidation} instance.
     * Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final SubscriptionValidation INSTANCE = new SubscriptionValidation();
    }
}
