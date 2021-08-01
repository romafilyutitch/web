package by.epam.jwd.web.service;

import by.epam.jwd.web.exception.UserWithLoginExistsException;
import by.epam.jwd.web.exception.WrongLoginException;
import by.epam.jwd.web.exception.InvalidSubscriptionException;
import by.epam.jwd.web.exception.WrongPasswordException;
import by.epam.jwd.web.model.Subscription;
import by.epam.jwd.web.model.User;

import java.util.Optional;

public interface UserService extends Service<User> {

    User login(User user) throws WrongLoginException, WrongPasswordException;

    Optional<User> findByLogin(String login);

    void promoteRole(User user);

    void demoteRole(User user);

    void setSubscription(User user, Subscription newSubscription) throws InvalidSubscriptionException;

    User changeLogin(User user, String newLogin) throws UserWithLoginExistsException;

    User changePassword(User user, String newPassword);
}
