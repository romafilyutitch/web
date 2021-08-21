package by.epam.jwd.web.dao.api;



import by.epam.jwd.web.dao.DAOException;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.model.UserRole;

import java.util.List;
import java.util.Optional;

/**
 * User data access object interface. Extends {@link Dao} base interface.
 * @author roma0
 * @version 1.0
 * @since 1.0
 * @see "Data access object pattern"
 */
public interface UserDao extends Dao<User> {
    /**
     * Finds and returns result of find {@link User} with specified name.
     * Returns user if there is user with passed name in database table.
     * or empty optional otherwise
     * @throws DAOException when exception in dao layer occurs.
     * @param login of user that need to find
     * @return found User if there is user with passed login or
     * empty optional otherwise
     */
    Optional<User> findUserByLogin(String login);

    /**
     * Finds and returns result of find users that have passed role.
     * @throws DAOException when exception in dao layer occurs.
     * @param role of users that need to find
     * @return Users with specified role
     */
    List<User> findUsersByRole(UserRole role);

}
