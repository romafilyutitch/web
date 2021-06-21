package by.epam.jwd.web.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import by.epam.jwd.web.dao.DAOFactory;
import by.epam.jwd.web.dao.SubscriptionDao;
import by.epam.jwd.web.dao.UserDao;
import by.epam.jwd.web.exception.LoginException;
import by.epam.jwd.web.exception.PaginationException;
import by.epam.jwd.web.exception.RegisterException;
import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.model.Subscription;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.model.UserRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

class SimpleUserService implements UserService {
    private static final Logger logger = LogManager.getLogger(SimpleUserService.class);

    private static final UserDao USER_DAO = DAOFactory.getInstance().getUserDao();
    private static final SubscriptionDao SUBSCRIPTION_DAO = DAOFactory.getInstance().getSubscriptionDao();
    private static final BCrypt.Hasher HASHER = BCrypt.withDefaults();
    private static final BCrypt.Verifyer VERIFYER = BCrypt.verifyer();

    private SimpleUserService() { }

    public static SimpleUserService getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public List<User> findAll() {
        final List<User> allUsers = USER_DAO.findAll();
        logger.info("All users was found");
        return allUsers;
    }

    @Override
    public User loginUser(User user) throws LoginException {
        final Optional<User> optionalUser = USER_DAO.findUserByLogin(user.getLogin());
        if (!optionalUser.isPresent()) {
            logger.info(String.format("Trying to login user but user with login such login does not exist", user));
            throw new LoginException(String.format("User with login: %s does not exist", user.getLogin()));
        }
        final User foundUser = optionalUser.get();
        final BCrypt.Result verifyResult = VERIFYER.verify(user.getPassword().toCharArray(), foundUser.getPassword().toCharArray());
        if (!verifyResult.verified) {
            logger.info("Incorrect password entered");
            throw new LoginException("Incorrect password");
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
        logger.info(String.format("Page of users number %s was found", currentPage));
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
            logger.info(String.format("Trying to register user but user with login already exists %s",user));
            throw new RegisterException(String.format("User with login %s already exists", user.getLogin()));
        }
        final String encryptedPassword = HASHER.hashToString(BCrypt.MIN_COST, user.getPassword().toCharArray());
        final User savedUser = USER_DAO.save(new User(user.getLogin(), encryptedPassword));
        logger.info(String.format("User was saved in database %s", user));
        return savedUser;
    }

    @Override
    public User findById(Long userId) {
        final Optional<User> optionalUser = USER_DAO.findById(userId);
        if (!optionalUser.isPresent()) {
            logger.error(String.format("Saved user with id %d was not found", userId));
            throw new ServiceException(String.format("Saved user with id %d was not found", userId));
        }
        final User foundUser = optionalUser.get();
        logger.info(String.format("User was found by id %s", foundUser));
        return foundUser;
    }

    @Override
    public User promoteUserRole(Long userId) {
        final User savedUser = findById(userId);
        final UserRole promotedRole = savedUser.getRole().promote();
        logger.info(String.format("User with id %d was promoted to %s", userId, promotedRole));
        return USER_DAO.update(savedUser.updateRole(promotedRole));
    }

    @Override
    public User demoteUserRole(Long userId) {
        final User savedUser = findById(userId);
        final UserRole demotedRole = savedUser.getRole().demote();
        logger.info(String.format("User with id %d was demoted to %s", userId, demotedRole));
        return USER_DAO.update(savedUser.updateRole(demotedRole));
    }

    @Override
    public void delete(Long userId) {
        final User savedUser = findById(userId);
        USER_DAO.delete(savedUser.getId());
        logger.info(String.format("User with id %d was deleted", userId));
    }

    @Override
    public User setSubscription(Long userId, Subscription newSubscription) {
        final User savedUser = findById(userId);
        final Subscription savedSubscription = SUBSCRIPTION_DAO.save(newSubscription);
        final User updatedUser = USER_DAO.update(savedUser.updateSubscription(savedSubscription));
        logger.info(String.format("New subscription was set to user %s", updatedUser));
        return updatedUser;
    }

    @Override
    public User changeLogin(Long userId, String newLogin) throws LoginException {
        final Optional<User> optionalUser = USER_DAO.findUserByLogin(newLogin);
        if (optionalUser.isPresent()) {
            logger.info(String.format("Trying to change login but user with login %s already exists", newLogin));
            throw new LoginException(String.format("User with login %s already exists", newLogin));
        }
        final User user = findById(userId);
        final User updatedUser = USER_DAO.update(user.updateLogin(newLogin));
        logger.info(String.format("New login was set to user %s", updatedUser));
        return updatedUser;
    }

    @Override
    public User changePassword(Long userId, String newPassword) {
        final User user = findById(userId);
        final String encryptedPassword = HASHER.hashToString(BCrypt.MIN_COST, newPassword.toCharArray());
        final User updatedUser = USER_DAO.update(user.updatePassword(encryptedPassword));
        logger.info(String.format("New password was set to user %s", updatedUser));
        return updatedUser;
    }

    private static class Singleton {
        private static final SimpleUserService INSTANCE = new SimpleUserService();
    }
}
