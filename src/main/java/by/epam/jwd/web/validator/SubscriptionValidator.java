package by.epam.jwd.web.validator;

import by.epam.jwd.web.model.Subscription;

import java.time.LocalDate;

public class SubscriptionValidator implements Validator<Subscription> {

    private SubscriptionValidator() {}

    public static SubscriptionValidator getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public boolean validate(Subscription subscription) {
        final LocalDate startDate = subscription.getStartDate();
        final LocalDate endDate = subscription.getEndDate();
        return startDate.isBefore(endDate) || startDate.isEqual(endDate);
    }

    private static class Singleton {
        private static final SubscriptionValidator INSTANCE = new SubscriptionValidator();
    }
}
