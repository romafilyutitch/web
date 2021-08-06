package by.epam.jwd.web.service.impl;

import at.favre.lib.crypto.bcrypt.BCrypt;
import by.epam.jwd.web.dao.DAOFactory;
import by.epam.jwd.web.dao.SubscriptionDao;
import by.epam.jwd.web.dao.UserDao;
import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.exception.UserWithLoginExistsException;
import by.epam.jwd.web.exception.WrongLoginException;
import by.epam.jwd.web.exception.WrongPasswordException;
import by.epam.jwd.web.model.Subscription;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.model.UserRole;
import by.epam.jwd.web.service.api.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for user service interface.
 * Makes all operations related with user.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class SimpleUserService implements UserService {
    private static final Logger logger = LogManager.getLogger(SimpleUserService.class);

    private final UserDao userDao = DAOFactory.getInstance().getUserDao();
    private final SubscriptionDao subscriptionDao = DAOFactory.getInstance().getSubscriptionDao();

    private final BCrypt.Hasher hasher = BCrypt.withDefaults();
    private final BCrypt.Verifyer verifyer = BCrypt.verifyer();

    private static final String ALL_USERS_WERE_FOUND_MESSAGE = "All users was found size = %d";
    private static final String USER_WITH_LOGIN_DOES_NOT_EXIST_MESSAGE = "User with entered login %s does not exist";
    private static final String WRONG_PASSWORD_WAS_ENTERED_MESSAGE = "Wrong password was entered";
    private static final String PAGE_OF_USERS_WAS_FOUND_MESSAGE = "Page of users number %d was found size = %d";
    private static final String USER_WITH_LOGIN_ALREADY_EXISTS_MESSAGE = "User with login %s already exists";
    private static final String USER_WAS_SAVED_MESSAGE = "User was saved in database %s";
    private static final String USER_BY_ID_WAS_NOT_FOUND_MESSAGE = "Saved user with id %d was not found";
    private static final String USER_BY_ID_WAS_FOUND_MESSAGE = "User was found by id %d %s";
    private static final String USER_ROLE_WAS_PROMOTED_MESSAGE = "Role was promoted to %s for user %s";
    private static final String USER_ROLE_WAS_DEMOTED_MESSAGE = "Role was demoted to %s for user %s";
    private static final String USER_WAS_DELETED_MESSAGE = "User with id %d was deleted";
    private static final String SUBSCRIPTION_WAS_SET_MESSAGE = "New subscription was set to user %s";
    private static final String LOGIN_WAS_CHANGED_MESSAGE = "New login was set to user %s";
    private static final String PASSWORD_WAS_CHANGED_MESSAGE = "New password was set to user %s";
    private static final String USER_WAS_LOGGED_IN_MESSAGE = "User was logged in %s";
    private static final String USER_BY_LOGIN_WAS_NOT_FOUND_MESSAGE = "User by login %s was not found";
    private static final String USER_BY_LOGIN_WAS_FOUND_MESSAGE = "User by login %s was found %s";

    private SimpleUserService() {
    }

    /**
     * Gets single class instance from nested class.
     * @return class instance.
     */
    public static SimpleUserService getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Finds and returns result of find all saved users.
     * @return all saved users collection.
     */
    @Override
    public List<User> findAll() {
        final List<User> allUsers = userDao.findAll();
        Collections.sort(allUsers);
        logger.info(String.format(ALL_USERS_WERE_FOUND_MESSAGE, allUsers.size()));
        return allUsers;
    }

    /**
     * Finds and returns result of find user that has passed login.
     * @param login login of user that need to be found.
     * @return found user if there is user that has passed login or
     * empty optional otherwise.
     */
    @Override
    public Optional<User> findByLogin(String login) {
        final Optional<User> optionalUser = userDao.findUserByLogin(login);
        if (optionalUser.isPresent()) {
            logger.info(String.format(USER_BY_LOGIN_WAS_FOUND_MESSAGE, login, optionalUser.get()));
        } else {
            logger.info(String.format(USER_BY_LOGIN_WAS_NOT_FOUND_MESSAGE, login));
        }
        return optionalUser;
    }

    /**
     * Makes saved user login.
     * @param user user that need to be logged in.
     * @return user that has been logged in.
     * @throws WrongLoginException when there is no user with passed login.
     * @throws WrongPasswordException when wrong password was entered.
     */
    @Override
    public User login(User user) throws WrongLoginException, WrongPasswordException {
        final Optional<User> optionalUser = userDao.findUserByLogin(user.getLogin());
        if (!optionalUser.isPresent()) {
            logger.error(String.format(USER_WITH_LOGIN_DOES_NOT_EXIST_MESSAGE, user.getLogin()));
            throw new WrongLoginException();
        }
        User foundUser = optionalUser.get();
        final BCrypt.Result verifyResult = verifyer.verify(user.getPassword().toCharArray(), foundUser.getPassword().toCharArray());
        if (!verifyResult.verified) {
            logger.error(WRONG_PASSWORD_WAS_ENTERED_MESSAGE);
            throw new WrongPasswordException();
        }
        logger.info(String.format(USER_WAS_LOGGED_IN_MESSAGE, foundUser.getLogin()));
        return foundUser;
    }

    /**
     * Finds users on passed page.
     * @throws IllegalArgumentException when passed page number is negative or
     * passed page number is greater then pages amount.
     * @param currentPage number entities page that need to be found.
     * @return users on passed page.
     */
    @Override
    public List<User> findPage(int currentPage) {
        if (currentPage <= 0 || currentPage > getPagesAmount()) {
            throw new IllegalArgumentException();
        }
        final List<User> foundPage = userDao.findPage(currentPage);
        Collections.sort(foundPage);
        logger.info(String.format(PAGE_OF_USERS_WAS_FOUND_MESSAGE, currentPage, foundPage.size()));
        return foundPage;
    }

    /**
     * Calculates current saved users page amount.
     * @return saved users pages amount.
     */
    @Override
    public int getPagesAmount() {
        return userDao.getPagesAmount();
    }

    /**
     * Saved user and assigns genrates id to saved user.
     * @param user that need to be saved.
     * @return saved user with assigned id.
     */
    @Override
    public User save(User user) {
        final String encryptedPassword = hasher.hashToString(BCrypt.MIN_COST, user.getPassword().toCharArray());
        final User savedUser = userDao.save(new User(user.getLogin(), encryptedPassword, UserRole.READER, null));
        logger.info(String.format(USER_WAS_SAVED_MESSAGE, savedUser));
        return savedUser;
    }

    /**
     * Finds and returns result of find saved user by passed id.
     * @throws ServiceException when saved user in not found by id.
     * @param userId of found user.
     * @return user that has passed id.
     */
    @Override
    public User findById(Long userId) {
        final Optional<User> optionalUser = userDao.findById(userId);
        if (!optionalUser.isPresent()) {
            logger.error(String.format(USER_BY_ID_WAS_NOT_FOUND_MESSAGE, userId));
            throw new ServiceException(String.format(USER_BY_ID_WAS_NOT_FOUND_MESSAGE, userId));
        }
        User foundUser = optionalUser.get();
        logger.info(String.format(USER_BY_ID_WAS_FOUND_MESSAGE, userId, foundUser));
        return foundUser;
    }

    /**
     * Makes user role promotion.
     * @param user whose role need to promote.
     */
    @Override
    public void promoteRole(User user) {
        final UserRole promotedRole = user.getRole().promote();
        final User userToPromote = new User(user.getId(), user.getLogin(), user.getPassword(), promotedRole, user.getSubscription());
        final User promotedUser = userDao.update(userToPromote);
        logger.info(String.format(USER_ROLE_WAS_PROMOTED_MESSAGE, promotedRole, promotedUser));
    }

    /**
     * Makes user role demotion.
     * @param user whose role need to demote.
     */
    @Override
    public void demoteRole(User user) {
        final UserRole demotedRole = user.getRole().demote();
        final User userToDemote = new User(user.getId(), user.getLogin(), user.getPassword(), demotedRole, user.getSubscription());
        final User demotedUser = userDao.update(userToDemote);
        logger.info(String.format(USER_ROLE_WAS_DEMOTED_MESSAGE, demotedRole, demotedUser));
    }

    /**
     * Deletes saved user that has passed id.
     * @param userId of user that need to be deleted.
     */
    @Override
    public void delete(Long userId) {
        userDao.delete(userId);
        logger.info(String.format(USER_WAS_DELETED_MESSAGE, userId));
    }

    /**
     * Sets new subscription to user.
     * @param user to who need set subscription.
     * @param newSubscription subscription that need to be set to user.
     */
    @Override
    public void setSubscription(User user, Subscription newSubscription) {
        final Subscription savedSubscription = subscriptionDao.save(newSubscription);
        final User userWithNewSubscription = new User(user.getId(), user.getLogin(), user.getPassword(), user.getRole(), savedSubscription);
        final User updatedUser = userDao.update(userWithNewSubscription);
        logger.info(String.format(SUBSCRIPTION_WAS_SET_MESSAGE, updatedUser));
    }

    /**
     * Makes login change
     * @param user user whose login need to be changed.
     * @param newLogin new login that need to set to passed user.
     * @return user with change login.
     * @throws UserWithLoginExistsException when there is another user with entered login.
     */
    @Override
    public User changeLogin(User user, String newLogin) throws UserWithLoginExistsException {
        final Optional<User> optionalUser = userDao.findUserByLogin(newLogin);
        if (optionalUser.isPresent()) {
            logger.info(String.format(USER_WITH_LOGIN_ALREADY_EXISTS_MESSAGE, newLogin));
            throw new UserWithLoginExistsException();
        }
        final User userWithChangedLogin = new User(user.getId(), newLogin, user.getPassword(), user.getRole(), user.getSubscription());
        final User updatedUser = userDao.update(userWithChangedLogin);
        logger.info(String.format(LOGIN_WAS_CHANGED_MESSAGE, updatedUser));
        return updatedUser;
    }

    /**
     * Makes change user password.
     * @param user user whose password need to be changed.
     * @param newPassword new password that need to be set to passed user.
     * @return user with changed password.
     */
    @Override
    public User changePassword(User user, String newPassword) {
        final String encryptedPassword = hasher.hashToString(BCrypt.MIN_COST, newPassword.toCharArray());
        final User userWithChangedPassword = new User(user.getId(), user.getLogin(), encryptedPassword, user.getRole(), user.getSubscription());
        final User updatedUser = userDao.update(userWithChangedPassword);
        logger.info(String.format(PASSWORD_WAS_CHANGED_MESSAGE, updatedUser));
        return updatedUser;
    }

    /**
     * Nested class that encapsulates single {@link SimpleUserService} instance.
     * Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final SimpleUserService INSTANCE = new SimpleUserService();
    }
}
