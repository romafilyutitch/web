package by.epam.jwd.web.validation;

import by.epam.jwd.web.model.Subscription;

public interface SubscriptionValidation {
    boolean isValidForSetSubscriptionToUser(Subscription subscription);

    static SubscriptionValidation getInstance() {
        return SubscriptionValidationImpl.getInstance();
    }
}
