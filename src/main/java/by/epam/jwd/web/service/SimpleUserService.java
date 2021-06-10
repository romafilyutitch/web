package by.epam.jwd.web.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import by.epam.jwd.web.dao.DaoFactory;
import by.epam.jwd.web.dao.MySQLSubscriptionDao;
import by.epam.jwd.web.dao.SubscriptionDao;
import by.epam.jwd.web.dao.UserDao;
import by.epam.jwd.web.model.Subscription;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.model.UserRole;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class SimpleUserService implements UserService {
    private static final UserDao USER_DAO = DaoFactory.getInstance().getUserDao();
    private static final SubscriptionDao SUBSCRIPTION_DAO = DaoFactory.getInstance().getSubscriptionDao();
    private static final BCrypt.Hasher HASHER = BCrypt.withDefaults();
    private static final BCrypt.Verifyer VERIFYER = BCrypt.verifyer();


    private SimpleUserService() {
    }

    public static SimpleUserService getInstance() {
        return Singleton.INSTANCE;
    }


    @Override
    public List<User> findAll() {
        return USER_DAO.findAll();
    }

    @Override
    public User login(String login, String password) throws ServiceException {
        final Optional<User> userByLogin = USER_DAO.findUserByLogin(login);
        if (!userByLogin.isPresent()) {
            throw new ServiceException(String.format("User with login: %s does not exist", login));
        }
        if (password == null || password.isEmpty()) {
            throw new ServiceException("Password is empty. Enter password");
        }
        final User foundUser = userByLogin.get();
        final BCrypt.Result verifyResult = VERIFYER.verify(password.toCharArray(), foundUser.getPassword().toCharArray());
        if (!verifyResult.verified) {
            throw new ServiceException("Incorrect password");
        }
        return foundUser;
    }

    @Override
    public User createUser(String login, String password) throws ServiceException {
        if (USER_DAO.findUserByLogin(login).isPresent()) {
            throw new ServiceException(String.format("User with login %s already exists", login));
        }
        if (password == null || password.isEmpty()) {
            throw new ServiceException("Password is empty. Enter password");
        }
        final String encryptedPassword = HASHER.hashToString(BCrypt.MIN_COST, password.toCharArray());
        return USER_DAO.save(new User(login, encryptedPassword));
    }

    @Override
    public User update(User user) throws ServiceException {
        final Optional<User> optionalUser = USER_DAO.findById(user.getId());
        if (!optionalUser.isPresent()) {
            throw new ServiceException("User does not exist");
        }
        return USER_DAO.update(user);
    }

    @Override
    public User findById(Long id) throws ServiceException {
        final Optional<User> optionalUser = USER_DAO.findById(id);
        if (!optionalUser.isPresent()) {
            throw new ServiceException(String.format("User with id %d does not exist", id));
        }
        return optionalUser.get();
    }

    @Override
    public User promoteUserRole(Long userId) throws ServiceException {
        final Optional<User> optionalUser = USER_DAO.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new ServiceException(String.format("User with id %d does not exist", userId));
        }
        final User savedUser = optionalUser.get();
        final UserRole promotedRole = savedUser.getRole().promote();
        return USER_DAO.update(savedUser.updateRole(promotedRole));
    }

    @Override
    public User demoteUserRole(Long userId) throws ServiceException {
        final Optional<User> optionalUser = USER_DAO.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new ServiceException(String.format("User with id %d does not exist", userId));
        }
        final User savedUser = optionalUser.get();
        final UserRole demotedRole = savedUser.getRole().demote();
        return USER_DAO.update(savedUser.updateRole(demotedRole));
    }

    @Override
    public void deleteUser(Long userId) throws ServiceException {
        final Optional<User> optionalUser = USER_DAO.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new ServiceException(String.format("User with id %d does not exist", userId));
        }
        final User savedUser = optionalUser.get();
        USER_DAO.delete(savedUser.getId());
    }

    @Override
    public User setSubscription(Long userId, String startDate, String endDate) throws ServiceException {
        final Optional<User> optionalUser = USER_DAO.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new ServiceException(String.format("User with id %d does not exist", userId));
        }
        if (startDate == null || startDate.isEmpty()) {
            throw new ServiceException("Start date is empty. Enter start date");
        }
        if (endDate == null || endDate.isEmpty()) {
            throw new ServiceException("End date is empty. Enter end date");
        }
        final LocalDate parsedStartDate = LocalDate.parse(startDate);
        final LocalDate parsedEndDate = LocalDate.parse(endDate);
        final User savedUser = optionalUser.get();
        final Subscription savedSubscription = SUBSCRIPTION_DAO.save(new Subscription(parsedStartDate, parsedEndDate));
        return USER_DAO.update(savedUser.updateSubscription(savedSubscription));
    }

    private static class Singleton {
        private static final SimpleUserService INSTANCE = new SimpleUserService();
    }
}
