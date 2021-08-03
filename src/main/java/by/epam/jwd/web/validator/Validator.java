package by.epam.jwd.web.validator;

import by.epam.jwd.web.model.DbEntity;

public interface Validator<T extends DbEntity> {
    boolean validate(T entity);

    static UserValidator getUserValidator() {
        return UserValidator.getInstance();
    }

    static SubscriptionValidator getSubscriptionValidator() {
        return SubscriptionValidator.getInstance();
    }

    static BookValidator getBookValidator() {
        return BookValidator.getInstance();
    }
}
