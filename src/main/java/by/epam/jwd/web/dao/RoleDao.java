package by.epam.jwd.web.dao;



import by.epam.jwd.web.model.UserRole;

import java.util.Optional;

public interface RoleDao extends Dao<UserRole> {
    Optional<UserRole> findByName(String userRoleName);
}
