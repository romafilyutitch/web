package by.epam.jwd.web.validator;

import by.epam.jwd.web.model.User;

public class UserValidator implements Validator<User> {
    private static final String VALIDATION_REGEX_PATTERN = "\\w{1,10}";

    private UserValidator() {}

    public static UserValidator getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public boolean validate(User user) {
        final String userLogin = user.getLogin();
        final String userPassword = user.getPassword();
        return userLogin.matches(VALIDATION_REGEX_PATTERN) && userPassword.matches(VALIDATION_REGEX_PATTERN);
    }

    private static class Singleton {
        private static final UserValidator INSTANCE = new UserValidator();
    }
}
