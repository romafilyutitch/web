package by.epam.jwd.web.validator;

import by.epam.jwd.web.exception.ValidationException;
import by.epam.jwd.web.model.BookOrder;

public class OrderValidator implements Validator<BookOrder> {

    private OrderValidator() {}

    public static OrderValidator getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public void validate(BookOrder validatedObject) throws ValidationException {
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
