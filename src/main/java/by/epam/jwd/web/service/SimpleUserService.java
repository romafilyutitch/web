package by.epam.jwd.web.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import by.epam.jwd.web.dao.DAOFactory;
import by.epam.jwd.web.dao.SubscriptionDao;
import by.epam.jwd.web.dao.UserDao;
import by.epam.jwd.web.exception.LoginException;
import by.epam.jwd.web.exception.RegisterException;
import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.model.Subscription;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.model.UserRole;

import java.util.List;
import java.util.Optional;

class SimpleUserService implements UserService {
    private static final UserDao USER_DAO = DAOFactory.getInstance().getUserDao();
    private static final SubscriptionDao SUBSCRIPTION_DAO = DAOFactory.getInstance().getSubscriptionDao();
    private static final BCrypt.Hasher HASHER = BCrypt.withDefaults();
    private static final BCrypt.Verifyer VERIFYER = BCrypt.verifyer();


    private SimpleUserService() {
    }

    public static SimpleUserService getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public List<User> findAllUsers() {
        return USER_DAO.findAll();
    }

    @Override
    public User loginUser(User user) throws LoginException {
        final Optional<User> optionalUser = USER_DAO.findUserByLogin(user.getLogin());
        if (!optionalUser.isPresent()) {
            throw new LoginException(String.format("User with login: %s does not exist", user.getLogin()));
        }
        final User foundUser = optionalUser.get();
        final BCrypt.Result verifyResult = VERIFYER.verify(user.getPassword().toCharArray(), foundUser.getPassword().toCharArray());
        if (!verifyResult.verified) {
            throw new LoginException("Incorrect password");
        }
        return foundUser;
    }

    @Override
    public User registerUser(User user) throws RegisterException {
        final Optional<User> optionalUser = USER_DAO.findUserByLogin(user.getLogin());
        if (optionalUser.isPresent()) {
            throw new RegisterException(String.format("User with login %s already exists", user.getLogin()));
        }
        final String encryptedPassword = HASHER.hashToString(BCrypt.MIN_COST, user.getPassword().toCharArray());
        return USER_DAO.save(new User(user.getLogin(), encryptedPassword));
    }

    @Override
    public User findById(Long userId) {
        final Optional<User> optionalUser = USER_DAO.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new ServiceException(String.format("Saved user with id %d was not found", userId));
        }
        return optionalUser.get();
    }

    @Override
    public User promoteUserRole(Long userId) {
        final User savedUser = findById(userId);
        final UserRole promotedRole = savedUser.getRole().promote();
        return USER_DAO.update(savedUser.updateRole(promotedRole));
    }

    @Override
    public User demoteUserRole(Long userId) {
        final User savedUser = findById(userId);
        final UserRole demotedRole = savedUser.getRole().demote();
        return USER_DAO.update(savedUser.updateRole(demotedRole));
    }

    @Override
    public void deleteUser(Long userId) {
        final User savedUser = findById(userId);
        USER_DAO.delete(savedUser.getId());
    }

    @Override
    public User setSubscription(Long userId, Subscription newSubscription) {
        final User savedUser = findById(userId);
        final Subscription savedSubscription = SUBSCRIPTION_DAO.save(newSubscription);
        return USER_DAO.update(savedUser.updateSubscription(savedSubscription));
    }

    @Override
    public User changeLogin(Long userId, String newLogin) throws LoginException {
        final Optional<User> optionalUser = USER_DAO.findUserByLogin(newLogin);
        if (optionalUser.isPresent()) {
            throw new LoginException(String.format("User with login %s already exists", newLogin));
        }
        final User user = findById(userId);
        return USER_DAO.update(user.updateLogin(newLogin));
    }

    @Override
    public User changePassword(Long userId, String newPassword) {
        final User user = findById(userId);
        final String encryptedPassword = HASHER.hashToString(BCrypt.MIN_COST, newPassword.toCharArray());
        return USER_DAO.update(user.updatePassword(encryptedPassword));
    }

    private static class Singleton {
        private static final SimpleUserService INSTANCE = new SimpleUserService();
    }


}
