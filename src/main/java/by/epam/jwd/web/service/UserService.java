package by.epam.jwd.web.service;

import by.epam.jwd.web.exception.LoginException;
import by.epam.jwd.web.exception.RegisterException;
import by.epam.jwd.web.model.Subscription;
import by.epam.jwd.web.model.User;

import java.util.List;

public interface UserService extends Service<User> {

    User loginUser(User user) throws LoginException;

    User promoteUserRole(Long userId);

    User demoteUserRole(Long userId);

    User setSubscription(Long userId, Subscription newSubscription);

    User changeLogin(Long userId, String newLogin) throws LoginException;

    User changePassword(Long userId, String newPassword);
}
