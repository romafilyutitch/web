package by.epam.jwd.web.validator;

import by.epam.jwd.web.exception.ValidationException;
import by.epam.jwd.web.model.User;

public class UserValidator implements Validator<User> {

    private UserValidator() {
    }

    public static UserValidator getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public void validate(User validatedObject) throws ValidationException {
        if (validatedObject.getLogin().isEmpty()) {
            throw new ValidationException("Login is empty");
        }
        if (validatedObject.getPassword().isEmpty()) {
            throw new ValidationException("Password is empty");
        }
    }

    private static class Singleton {
        private static final UserValidator INSTANCE = new UserValidator();
    }
}
