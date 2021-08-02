package by.epam.jwd.web.validation;

import by.epam.jwd.web.model.DbEntity;
import by.epam.jwd.web.model.User;

public class UserLoginValidationStrategy<T extends DbEntity> implements ValidationStrategy<User> {
    @Override
    public boolean validate(User user) {
        final String userLogin = user.getLogin();
        return userLogin.matches("\\w{1,10}");
    }
}
