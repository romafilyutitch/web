package by.epam.jwd.web.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import by.epam.jwd.web.dao.DaoFactory;
import by.epam.jwd.web.dao.UserDao;
import by.epam.jwd.web.model.User;

import java.util.List;
import java.util.Optional;

public class SimpleUserService implements UserService {
    private static final UserDao USER_DAO = DaoFactory.getInstance().getUserDao();
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

    private static class Singleton {
        private static final SimpleUserService INSTANCE = new SimpleUserService();
    }
}
