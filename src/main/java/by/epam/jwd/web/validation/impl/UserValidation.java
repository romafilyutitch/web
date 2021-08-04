package by.epam.jwd.web.validation.impl;

import by.epam.jwd.web.model.User;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.validation.Validation;

import java.util.ArrayList;
import java.util.List;

public class UserValidation implements Validation<User> {
    private static final String INVALID_USER_LOGIN_MESSAGE_KEY = "user.validation.login.invalid";
    private static final String INVALID_USER_PASSWORD_MESSAGE_KEY = "user.validation.password.invalid";
    private static final String REGEX_PATTERN = "\\w{1,10}";


    private UserValidation() {
    }

    public static UserValidation getInstance() {
        return Singleton.INSTANCE;
    }

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

    private static class Singleton {
        private static final UserValidation INSTANCE = new UserValidation();
    }
}
