package by.epam.jwd.web.service;

import by.epam.jwd.web.exception.ChangeLoginException;
import by.epam.jwd.web.exception.LoginUserException;
import by.epam.jwd.web.exception.RegisterException;
import by.epam.jwd.web.exception.SubscriptionException;
import by.epam.jwd.web.model.Subscription;
import by.epam.jwd.web.model.User;

import java.time.LocalDate;
import java.util.List;

public interface UserService {

    List<User> findAllUsers();

    User registerUser(User user) throws RegisterException;

    User loginUser(User user) throws LoginUserException;

    void deleteUser(Long userId);

    User findById(Long userId);

    User promoteUserRole(Long userId);

    User demoteUserRole(Long userId);

    User setSubscription(Long userId, Subscription newSubscription);

    User changeLogin(Long userId, String newLogin);

    User changePassword(Long userId, String newPassword);
}
