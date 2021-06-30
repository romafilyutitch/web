package by.epam.jwd.web.service;

import by.epam.jwd.web.exception.LoginExistsException;
import by.epam.jwd.web.exception.NoLoginException;
import by.epam.jwd.web.exception.SubscriptionException;
import by.epam.jwd.web.exception.WrongPasswordException;
import by.epam.jwd.web.model.Subscription;
import by.epam.jwd.web.model.User;

public interface UserService extends Service<User> {

    User loginUser(User user) throws NoLoginException, WrongPasswordException;

    User promoteUserRole(Long userId);

    User demoteUserRole(Long userId);

    User setSubscription(Long userId, Subscription newSubscription) throws SubscriptionException;

    User changeLogin(Long userId, String newLogin) throws LoginExistsException;

    User changePassword(Long userId, String newPassword);
}
