package by.epam.jwd.web.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import by.epam.jwd.web.dao.DAOFactory;
import by.epam.jwd.web.dao.SubscriptionDao;
import by.epam.jwd.web.dao.UserDao;
import by.epam.jwd.web.exception.LoginException;
import by.epam.jwd.web.exception.RegisterException;
import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.exception.SubscriptionException;
import by.epam.jwd.web.model.Subscription;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.model.UserRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class SimpleUserService implements UserService {
    private static final Logger logger = LogManager.getLogger(SimpleUserService.class);

    private static final UserDao USER_DAO = DAOFactory.getInstance().getUserDao();
    private static final SubscriptionDao SUBSCRIPTION_DAO = DAOFactory.getInstance().getSubscriptionDao();

    private static final BCrypt.Hasher HASHER = BCrypt.withDefaults();
    private static final BCrypt.Verifyer VERIFYER = BCrypt.verifyer();

    private static final String ALL_USERS_WERE_FOUND_MESSAGE = "All users was found";
    private static final String USER_WITH_LOGIN_DOES_NOT_EXIST_MESSAGE = "User with login %s does not exist";
    private static final String WRONG_PASSWORD_WAS_ENTERED_MESSAGE = "Wrong password was entered";
    private static final String PAGE_OF_USERS_WAS_FOUND_MESSAGE = "Page of users number %s was found";
    private static final String USER_WITH_LOGIN_ALREADY_EXISTS_MESSAGE = "User with login %s already exists";
    private static final String USER_WAS_SAVED_MESSAGE = "User was saved in database %s";
    private static final String USER_BY_ID_WAS_NOT_FOUND_MESSAGE = "Saved user with id %d was not found";
    private static final String USER_BY_ID_WAS_FOUND_MESSAGE = "User was found by id %s";
    private static final String USER_ROLE_WAS_PROMOTED_MESSAGE = "User with id %d was promoted to %s";
    private static final String USER_ROLE_WAS_DEMOTED_MESSAGE = "User with id %d was demoted to %s";
    private static final String USER_WAS_DELETED_MESSAGE = "User with id %d was deleted";
    private static final String SUBSCRIPTION_WAS_SET_MESSAGE = "New subscription was set to user %s";
    private static final String LOGIN_WAS_CHANGED_MESSAGE = "New login was set to user %s";
    private static final String PASSWORD_WAS_CHANGED_MESSAGE = "New password was set to user %s";
    private static final String SUBSCRIPTION_WAS_NOT_FOUND_MESSAGE = "Saved subscription with id %d was not found";

    private SimpleUserService() {
    }

    public static SimpleUserService getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public List<User> findAll() {
        List<User> allUsers = USER_DAO.findAll();
        allUsers = fillWithSubscription(allUsers);
        logger.info(ALL_USERS_WERE_FOUND_MESSAGE);
        return allUsers;
    }

    @Override
    public User loginUser(User user) throws LoginException {
        final Optional<User> optionalUser = USER_DAO.findUserByLogin(user.getLogin());
        if (!optionalUser.isPresent()) {
            logger.info(String.format(USER_WITH_LOGIN_DOES_NOT_EXIST_MESSAGE, user.getLogin()));
            throw new LoginException(String.format(USER_WITH_LOGIN_DOES_NOT_EXIST_MESSAGE, user.getLogin()));
        }
        User foundUser = optionalUser.get();
        foundUser = fillWithSubscription(foundUser);
        final BCrypt.Result verifyResult = VERIFYER.verify(user.getPassword().toCharArray(), foundUser.getPassword().toCharArray());
        if (!verifyResult.verified) {
            logger.info(WRONG_PASSWORD_WAS_ENTERED_MESSAGE);
            throw new LoginException(WRONG_PASSWORD_WAS_ENTERED_MESSAGE);
        }
        return foundUser;
    }

    @Override
    public List<User> findPage(int currentPage) {
        List<User> foundPage;
        if (currentPage < 1) {
            foundPage = USER_DAO.findPage(1);
        } else if (currentPage >= getPagesAmount()) {
            foundPage = USER_DAO.findPage(getPagesAmount());
        } else {
            foundPage = USER_DAO.findPage(currentPage);
        }
        foundPage = fillWithSubscription(foundPage);
        logger.info(String.format(PAGE_OF_USERS_WAS_FOUND_MESSAGE, currentPage));
        return foundPage;
    }

    @Override
    public int getPagesAmount() {
        return USER_DAO.getPagesAmount();
    }

    @Override
    public User register(User user) throws RegisterException {
        final Optional<User> optionalUser = USER_DAO.findUserByLogin(user.getLogin());
        if (optionalUser.isPresent()) {
            logger.info(String.format(USER_WITH_LOGIN_ALREADY_EXISTS_MESSAGE, user.getLogin()));
            throw new RegisterException(String.format(USER_WITH_LOGIN_ALREADY_EXISTS_MESSAGE, user.getLogin()));
        }
        final String encryptedPassword = HASHER.hashToString(BCrypt.MIN_COST, user.getPassword().toCharArray());
        final User savedUser = USER_DAO.save(new User(user.getLogin(), encryptedPassword));
        logger.info(String.format(USER_WAS_SAVED_MESSAGE, user));
        return savedUser;
    }

    @Override
    public User findById(Long userId) {
        final Optional<User> optionalUser = USER_DAO.findById(userId);
        if (!optionalUser.isPresent()) {
            logger.error(String.format(USER_BY_ID_WAS_NOT_FOUND_MESSAGE, userId));
            throw new ServiceException(String.format(USER_BY_ID_WAS_NOT_FOUND_MESSAGE, userId));
        }
        User foundUser = optionalUser.get();
        foundUser = fillWithSubscription(foundUser);
        logger.info(String.format(USER_BY_ID_WAS_FOUND_MESSAGE, foundUser));
        return foundUser;
    }

    @Override
    public User promoteUserRole(Long userId) {
        final User savedUser = findById(userId);
        final UserRole promotedRole = savedUser.getRole().promote();
        logger.info(String.format(USER_ROLE_WAS_PROMOTED_MESSAGE, userId, promotedRole));
        return USER_DAO.update(savedUser.updateRole(promotedRole));
    }

    @Override
    public User demoteUserRole(Long userId) {
        final User savedUser = findById(userId);
        final UserRole demotedRole = savedUser.getRole().demote();
        logger.info(String.format(USER_ROLE_WAS_DEMOTED_MESSAGE, userId, demotedRole));
        return USER_DAO.update(savedUser.updateRole(demotedRole));
    }

    @Override
    public void delete(Long userId) {
        USER_DAO.delete(userId);
        logger.info(String.format(USER_WAS_DELETED_MESSAGE, userId));
    }

    @Override
    public User setSubscription(Long userId, Subscription newSubscription) throws SubscriptionException {
        if (newSubscription.getStartDate().isAfter(newSubscription.getEndDate())) {
            throw new SubscriptionException("Start date is after end date");
        }
        final User savedUser = findById(userId);
        final Subscription savedSubscription = SUBSCRIPTION_DAO.save(newSubscription);
        final User updatedUser = USER_DAO.update(savedUser.updateSubscription(savedSubscription));
        logger.info(String.format(SUBSCRIPTION_WAS_SET_MESSAGE, updatedUser));
        return updatedUser;
    }

    @Override
    public User changeLogin(Long userId, String newLogin) throws LoginException {
        final Optional<User> optionalUser = USER_DAO.findUserByLogin(newLogin);
        if (optionalUser.isPresent()) {
            logger.info(String.format(USER_WITH_LOGIN_ALREADY_EXISTS_MESSAGE, newLogin));
            throw new LoginException(String.format(USER_WITH_LOGIN_ALREADY_EXISTS_MESSAGE, newLogin));
        }
        final User user = findById(userId);
        final User updatedUser = USER_DAO.update(user.updateLogin(newLogin));
        logger.info(String.format(LOGIN_WAS_CHANGED_MESSAGE, updatedUser));
        return updatedUser;
    }

    @Override
    public User changePassword(Long userId, String newPassword) {
        final User user = findById(userId);
        final String encryptedPassword = HASHER.hashToString(BCrypt.MIN_COST, newPassword.toCharArray());
        final User updatedUser = USER_DAO.update(user.updatePassword(encryptedPassword));
        logger.info(String.format(PASSWORD_WAS_CHANGED_MESSAGE, updatedUser));
        return updatedUser;
    }

    private List<User> fillWithSubscription(List<User> users) {
        return users.stream().map(this::fillWithSubscription).collect(Collectors.toList());
    }

    private User fillWithSubscription(User user) {
        if (user.getSubscription() == null) {
            return user;
        }
        final Optional<Subscription> optionalSubscription = SUBSCRIPTION_DAO.findById(user.getSubscription().getId());
        if (!optionalSubscription.isPresent()) {
            throw new ServiceException(String.format(SUBSCRIPTION_WAS_NOT_FOUND_MESSAGE, user.getSubscription().getId()));
        }
        return user.updateSubscription(optionalSubscription.get());
    }

    private static class Singleton {
        private static final SimpleUserService INSTANCE = new SimpleUserService();
    }
}
