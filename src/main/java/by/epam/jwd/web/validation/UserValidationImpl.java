package by.epam.jwd.web.validation;

import at.favre.lib.crypto.bcrypt.BCrypt;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.ServiceFactory;
import by.epam.jwd.web.service.UserService;

import java.util.Optional;

public class UserValidationImpl implements UserValidation {
    private UserService userService = ServiceFactory.getInstance().getUserService();
    private BCrypt.Verifyer verifyer = BCrypt.verifyer();

    @Override
    public boolean isValidForLogin(User user) {
        if (user == null) {
            return false;
        }
        final String userLogin = user.getLogin();
        final String userPassword = user.getPassword();
        if (userLogin == null || userPassword == null || userLogin.isEmpty() || userPassword.isEmpty()) {
            return false;
        }
        final Optional<User> optionalUserFoundByLogin = userService.findByLogin(userLogin);
        if (!optionalUserFoundByLogin.isPresent()) {
            return false;
        }
        final User userFoundByLogin = optionalUserFoundByLogin.get();
        final BCrypt.Result verifyResult = verifyer.verify(userPassword.toCharArray(), userFoundByLogin.getPassword().toCharArray());
        return verifyResult.verified;
    }

    @Override
    public boolean isValidForRegister(User user) {
        if (user == null) {
            return false;
        }
        final String userLogin = user.getLogin();
        final String userPassword = user.getPassword();
        if (userLogin == null || userPassword == null || userLogin.isEmpty() || userPassword.isEmpty()) {
            return false;
        }
        final Optional<User> optionalUserFoundByLogin = userService.findByLogin(userLogin);
        return !optionalUserFoundByLogin.isPresent();
    }

    @Override
    public boolean isValidForChangeLogin(User user) {
        if (user == null) {
            return false;
        }
        final String userLogin = user.getLogin();
        final String userPassword = user.getPassword();
        if (userLogin == null || userPassword == null || userLogin.isEmpty() || userPassword.isEmpty()) {
            return false;
        }
        final Optional<User> optionalUserFoundByLogin = userService.findByLogin(userLogin);
        return !optionalUserFoundByLogin.isPresent();
    }

    private boolean isLoginAndPasswordPresent(User user) {
        if (user == null) {
            return false;
        }
        final String userLogin = user.getLogin();
        final String userPassword = user.getPassword();
        return userLogin != null && userPassword != null && !userLogin.isEmpty() && !userPassword.isEmpty();
    }
}
