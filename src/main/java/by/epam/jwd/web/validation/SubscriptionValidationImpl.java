package by.epam.jwd.web.validation;

import by.epam.jwd.web.model.Subscription;

import java.time.LocalDate;

public class SubscriptionValidationImpl implements SubscriptionValidation {

    private SubscriptionValidationImpl() {}

    public static SubscriptionValidationImpl getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public boolean isValidForSetSubscriptionToUser(Subscription subscription) {
        if (subscription == null) {
            return false;
        }
        final LocalDate startDate = subscription.getStartDate();
        final LocalDate endDate = subscription.getEndDate();
        return startDate != null && endDate != null && startDate.isBefore(endDate);
    }

    private static class Singleton {
        private static final SubscriptionValidationImpl INSTANCE = new SubscriptionValidationImpl();
    }

}
