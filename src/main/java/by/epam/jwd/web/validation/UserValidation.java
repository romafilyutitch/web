package by.epam.jwd.web.validation;

import by.epam.jwd.web.model.User;

public interface UserValidation {

    boolean isValidForLogin(User user);

    boolean isValidForRegister(User user);

    boolean isValidForChangeLogin(User user);
}
