package by.epam.jwd.web.service;

import by.epam.jwd.web.exception.UserWithLoginExistsException;
import by.epam.jwd.web.exception.WrongLoginException;
import by.epam.jwd.web.exception.InvalidSubscriptionException;
import by.epam.jwd.web.exception.WrongPasswordException;
import by.epam.jwd.web.model.Subscription;
import by.epam.jwd.web.model.User;

import java.util.Optional;

/**
 * Service interface for service layer that defines {@link User} service behavior.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public interface UserService extends Service<User> {
    /**
     * Makes usr login when new user wants to login.
     * @param user user that need to be logged in.
     * @return logged in user.
     * @throws WrongLoginException when user with passed login does not exists
     * @throws WrongPasswordException when user entered password is not right
     */
    User login(User user) throws WrongLoginException, WrongPasswordException;

    /**
     * Finds saved user by passed login.
     * @param login login of user that need to be found.
     * @return found user in optional if there is user with passed login in database
     * or empty optional otherwise.
     */
    Optional<User> findByLogin(String login);

    /**
     * Makes user role promotion of passed user.
     * @param user whose role need to promote.
     */
    void promoteRole(User user);

    /**
     * Makes user role demotion of passed user.
     * @param user whose role need to demote.
     */
    void demoteRole(User user);

    /**
     * Set subscription to passed user.
     * @param user to who need set subscription.
     * @param newSubscription subscription that need to be set to user.
     */
    void setSubscription(User user, Subscription newSubscription);

    /**
     * Makes user login change of passed user.
     * @param user user whose login need to be changed.
     * @param newLogin new login that need to set to passed user.
     * @return user with changed login.
     * @throws UserWithLoginExistsException if another user with passed login exists
     */
    User changeLogin(User user, String newLogin) throws UserWithLoginExistsException;

    /**
     * Makes user password change of passed user.
     * @param user user whose password need to be changed.
     * @param newPassword new password that need to be set to passed user.
     * @return user with changed password.
     */
    User changePassword(User user, String newPassword);
}
