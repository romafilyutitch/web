package by.epam.jwd.web.validator;

import by.epam.jwd.web.exception.ValidationException;
import by.epam.jwd.web.model.Subscription;

public class SubscriptionValidator implements Validator<Subscription> {

    private SubscriptionValidator() {}

    public static SubscriptionValidator getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public void validate(Subscription validatedObject) throws ValidationException {
        if (validatedObject.getStartDate().isBefore(validatedObject.getEndDate())) {
            throw new ValidationException("Start date is after end date");
        }
        if (validatedObject.getEndDate().isBefore(validatedObject.getStartDate())) {
            throw new ValidationException("End date is before start date");
        }
    }

    private static class Singleton {
        private static final SubscriptionValidator INSTANCE = new SubscriptionValidator();
    }
}
