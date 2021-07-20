package by.epam.jwd.web.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import by.epam.jwd.web.dao.DAOFactory;
import by.epam.jwd.web.dao.SubscriptionDao;
import by.epam.jwd.web.dao.UserDao;
import by.epam.jwd.web.exception.UserWithLoginExistsException;
import by.epam.jwd.web.exception.NoUserWithLoginException;
import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.exception.WrongSubscriptionException;
import by.epam.jwd.web.exception.WrongPasswordException;
import by.epam.jwd.web.model.Subscription;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.model.UserRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

class SimpleUserService implements UserService {
    private static final Logger logger = LogManager.getLogger(SimpleUserService.class);

    private final UserDao userDao = DAOFactory.getInstance().getUserDao();
    private final SubscriptionDao subscriptionDao = DAOFactory.getInstance().getSubscriptionDao();

    private final BCrypt.Hasher hasher = BCrypt.withDefaults();
    private final BCrypt.Verifyer verifyer = BCrypt.verifyer();

    private static final String START_DATE_IS_AFTER_END_DATE_MESSAGE = "Start date is after end date";
    private static final String ALL_USERS_WERE_FOUND_MESSAGE = "All users was found";
    private static final String USER_WITH_LOGIN_DOES_NOT_EXIST_MESSAGE = "User with login %s does not exist";
    private static final String WRONG_PASSWORD_WAS_ENTERED_MESSAGE = "Wrong password was entered";
    private static final String PAGE_OF_USERS_WAS_FOUND_MESSAGE = "Page of users number %s was found";
    private static final String USER_WITH_LOGIN_ALREADY_EXISTS_MESSAGE = "User with login %s already exists";
    private static final String USER_WAS_SAVED_MESSAGE = "User was saved in database %s";
    private static final String USER_BY_ID_WAS_NOT_FOUND_MESSAGE = "Saved user with id %d was not found";
    private static final String USER_BY_ID_WAS_FOUND_MESSAGE = "User was found by id %s";
    private static final String USER_ROLE_WAS_PROMOTED_MESSAGE = "Role was promoted for user %s";
    private static final String USER_ROLE_WAS_DEMOTED_MESSAGE = "Role was demoted for user %s";
    private static final String USER_WAS_DELETED_MESSAGE = "User with id %d was deleted";
    private static final String SUBSCRIPTION_WAS_SET_MESSAGE = "New subscription was set to user %s";
    private static final String LOGIN_WAS_CHANGED_MESSAGE = "New login was set to user %s";
    private static final String PASSWORD_WAS_CHANGED_MESSAGE = "New password was set to user %s";
    private static final String SUBSCRIPTION_WAS_NOT_FOUND_MESSAGE = "Saved subscription with id %d was not found";
    private static final String USER_WAS_LOGGED_IN_MESSAGE = "User %s was logged in";

    private SimpleUserService() {
    }

    public static SimpleUserService getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public List<User> findAll() {
        List<User> allUsers = userDao.findAll();
        logger.info(ALL_USERS_WERE_FOUND_MESSAGE);
        return allUsers;
    }

    @Override
    public Optional<User> findByLogin(String login) {
        return userDao.findUserByLogin(login);
    }

    @Override
    public User loginUser(User user) throws NoUserWithLoginException, WrongPasswordException {
        final Optional<User> optionalUser = userDao.findUserByLogin(user.getLogin());
        if (!optionalUser.isPresent()) {
            logger.info(String.format(USER_WITH_LOGIN_DOES_NOT_EXIST_MESSAGE, user.getLogin()));
            throw new NoUserWithLoginException();
        }
        User foundUser = optionalUser.get();
        final BCrypt.Result verifyResult = verifyer.verify(user.getPassword().toCharArray(), foundUser.getPassword().toCharArray());
        if (!verifyResult.verified) {
            logger.info(WRONG_PASSWORD_WAS_ENTERED_MESSAGE);
            throw new WrongPasswordException();
        }
        logger.info(String.format(USER_WAS_LOGGED_IN_MESSAGE, foundUser.getLogin()));
        return foundUser;
    }

    @Override
    public List<User> findPage(int currentPage) {
        List<User> foundPage;
        if (currentPage < 1) {
            foundPage = userDao.findPage(1);
        } else if (currentPage >= getPagesAmount()) {
            foundPage = userDao.findPage(getPagesAmount());
        } else {
            foundPage = userDao.findPage(currentPage);
        }
        logger.info(String.format(PAGE_OF_USERS_WAS_FOUND_MESSAGE, currentPage));
        return foundPage;
    }

    @Override
    public int getPagesAmount() {
        return userDao.getPagesAmount();
    }

    @Override
    public User register(User user) {
        final String encryptedPassword = hasher.hashToString(BCrypt.MIN_COST, user.getPassword().toCharArray());
        final User savedUser = userDao.save(new User(user.getLogin(), encryptedPassword, UserRole.READER, null));
        logger.info(String.format(USER_WAS_SAVED_MESSAGE, user));
        return savedUser;
    }

    @Override
    public User findById(Long userId) {
        final Optional<User> optionalUser = userDao.findById(userId);
        if (!optionalUser.isPresent()) {
            logger.error(String.format(USER_BY_ID_WAS_NOT_FOUND_MESSAGE, userId));
            throw new ServiceException(String.format(USER_BY_ID_WAS_NOT_FOUND_MESSAGE, userId));
        }
        User foundUser = optionalUser.get();
        logger.info(String.format(USER_BY_ID_WAS_FOUND_MESSAGE, foundUser));
        return foundUser;
    }

    @Override
    public void promoteUserRole(User user) {
        final UserRole promotedRole = user.getRole().promote();
        final User userToPromote = new User(user.getId(), user.getLogin(), user.getPassword(), promotedRole, user.getSubscription());
        final User promotedUser = userDao.update(userToPromote);
        logger.info(String.format(USER_ROLE_WAS_PROMOTED_MESSAGE, promotedUser));
    }

    @Override
    public void demoteUserRole(User user) {
        final UserRole demotedRole = user.getRole().demote();
        final User userToDemote = new User(user.getId(), user.getLogin(), user.getPassword(), demotedRole, user.getSubscription());
        final User demotedUser = userDao.update(userToDemote);
        logger.info(String.format(USER_ROLE_WAS_DEMOTED_MESSAGE, demotedUser));
    }

    @Override
    public void delete(Long userId) {
        userDao.delete(userId);
        logger.info(String.format(USER_WAS_DELETED_MESSAGE, userId));
    }

    @Override
    public void setSubscription(User user, Subscription newSubscription) throws WrongSubscriptionException {
        if (newSubscription.getStartDate().isAfter(newSubscription.getEndDate())) {
            throw new WrongSubscriptionException(START_DATE_IS_AFTER_END_DATE_MESSAGE);
        }
        final Subscription savedSubscription = subscriptionDao.save(newSubscription);
        final User userWithNewSubscription = new User(user.getId(), user.getLogin(), user.getPassword(), user.getRole(), savedSubscription);
        final User updatedUser = userDao.update(userWithNewSubscription);
        logger.info(String.format(SUBSCRIPTION_WAS_SET_MESSAGE, updatedUser));
    }

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

    @Override
    public User changePassword(User user, String newPassword) {
        final String encryptedPassword = hasher.hashToString(BCrypt.MIN_COST, newPassword.toCharArray());
        final User userWithChangedPassword = new User(user.getId(), user.getLogin(), encryptedPassword, user.getRole(), user.getSubscription());
        final User updatedUser = userDao.update(userWithChangedPassword);
        logger.info(String.format(PASSWORD_WAS_CHANGED_MESSAGE, updatedUser));
        return updatedUser;
    }


    private static class Singleton {
        private static final SimpleUserService INSTANCE = new SimpleUserService();
    }
}
