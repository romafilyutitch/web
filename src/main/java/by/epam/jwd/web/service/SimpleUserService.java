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
    public User login(User user) throws ServiceException {
        final Optional<User> userByLogin = USER_DAO.findUserByLogin(user.getLogin());
        if (!userByLogin.isPresent()) {
            throw new ServiceException(String.format("User with login: %s does not exist", user.getLogin()));
        }
        final User foundUser = userByLogin.get();
        final BCrypt.Result verifyResult = VERIFYER.verify(user.getPassword().toCharArray(), foundUser.getPassword().toCharArray());
        if (!verifyResult.verified) {
            throw new ServiceException("Incorrect password");
        }
        return foundUser;
    }

    @Override
    public User save(User user) throws ServiceException {
        if (USER_DAO.findUserByLogin(user.getLogin()).isPresent()) {
            throw new ServiceException(String.format("User with login %s already exists", user.getLogin()));
        }
        final String userPassword = user.getPassword();
        final String encryptedPassword = HASHER.hashToString(BCrypt.MIN_COST, userPassword.toCharArray());
        return USER_DAO.save(new User(user.getLogin(), encryptedPassword));
    }

    private static class Singleton {
        private static final SimpleUserService INSTANCE = new SimpleUserService();
    }
}
