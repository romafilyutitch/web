package by.epam.jwd.web.validation;

import by.epam.jwd.web.model.DbEntity;
import by.epam.jwd.web.model.Subscription;

import java.time.LocalDate;

public class SubscriptionRangeStrategy<T extends DbEntity> implements ValidationStrategy<Subscription> {
    @Override
    public boolean validate(Subscription subscription) {
        final LocalDate startDate = subscription.getStartDate();
        final LocalDate endDate = subscription.getEndDate();
        return startDate.isAfter(endDate) || startDate.isEqual(endDate);
    }
}
