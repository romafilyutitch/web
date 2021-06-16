package by.epam.jwd.web.validator;

import by.epam.jwd.web.exception.ValidationException;
import by.epam.jwd.web.model.Order;

public class OrderValidator implements Validator<Order> {

    private OrderValidator() {}

    public static OrderValidator getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public void validate(Order validatedObject) throws ValidationException {
        if (validatedObject.getUser().getId() == null) {
            throw new ValidationException("User doesn't exist");
        }
        if (validatedObject.getBook().getId() == null) {
            throw new ValidationException("Book doesn't exist");
        }
    }

    private static class Singleton {
        private static final OrderValidator INSTANCE = new OrderValidator();
    }
}
