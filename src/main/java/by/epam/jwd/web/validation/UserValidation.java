package by.epam.jwd.web.validation;

import by.epam.jwd.web.model.User;
import by.epam.jwd.web.resource.MessageManager;

import java.util.ArrayList;
import java.util.List;

/**
 * User validation class that makes user validation.
 * Makes invalid messages if entity is invalid and adds them in
 * invalid messages list. If invalid messages lit is empty that means
 * that entity is valid.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
class UserValidation implements Validation<User> {
    private static final String INVALID_USER_LOGIN_MESSAGE_KEY = "user.validation.login.invalid";
    private static final String INVALID_USER_PASSWORD_MESSAGE_KEY = "user.validation.password.invalid";
    private static final String REGEX_PATTERN = "\\w{1,10}";


    private UserValidation() {
    }

    /**
     * Gets single class instance from nested class.
     * @return class instance.
     */
    public static UserValidation getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Validates user instance. Makes invalid messages if instance is
     * invalid and puts adds it to invalid messages list. If invalid messages
     * list is empty that means that entity is valid.
     * User is valid if it has login and password that consists of
     * 10 or less english characters.
     * @param user that need to be validated.
     * @return invalid message list. If invalid messages list
     * if empty that means that entity is valid.
     */
    @Override
    public List<String> validate(User user) {
        final List<String> messages = new ArrayList<>();
        final String userLogin = user.getLogin();
        final String userPassword = user.getPassword();
        if (userLogin == null || userLogin.isEmpty() || !userLogin.matches(REGEX_PATTERN)) {
            messages.add(MessageManager.getMessage(INVALID_USER_LOGIN_MESSAGE_KEY));
        }
        if (userPassword == null || userPassword.isEmpty() || !userPassword.matches(REGEX_PATTERN)) {
            messages.add(MessageManager.getMessage(INVALID_USER_PASSWORD_MESSAGE_KEY));
        }
        return messages;
    }

    /**
     * Nested class that encapsulates single {@link UserValidation} instance.
     * Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final UserValidation INSTANCE = new UserValidation();
    }
}
