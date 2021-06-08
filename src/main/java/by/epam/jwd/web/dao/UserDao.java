package by.epam.jwd.web.dao;



import by.epam.jwd.web.model.User;
import by.epam.jwd.web.model.UserRole;

import java.util.List;
import java.util.Optional;

public interface UserDao extends Dao<User> {
    Optional<User> findUserByLogin(String login);

    List<User> findUsersByRole(UserRole role);

}
