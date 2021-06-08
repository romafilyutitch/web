package by.epam.jwd.web.dao;



import by.epam.jwd.web.model.UserRole;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class MySQLRoleDao extends AbstractDao<UserRole> implements RoleDao {
    private static final String FIND_ALL_SQL = "select id, role from user_role";
    public static final String ROLE_COLUMN = "role";

    private MySQLRoleDao() {
        super(FIND_ALL_SQL);
    }

    public static MySQLRoleDao getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    protected UserRole mapResultSet(ResultSet result) throws SQLException {
        return UserRole.valueOf(result.getString(ROLE_COLUMN).toUpperCase());
    }

    @Override
    protected void setSavePrepareStatementValues(UserRole entity, PreparedStatement savePreparedStatement) {
        throw new UnsupportedOperationException();
    }

    @Override
    protected void setUpdatePreparedStatementValues(UserRole entity, PreparedStatement updatePreparedStatement) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Optional<UserRole> findByName(String userRoleName) throws DAOException {
        return findAll().stream().filter(role -> role.getRoleName().equals(userRoleName)).findAny();
    }

    private static class Singleton {
        private static final MySQLRoleDao INSTANCE = new MySQLRoleDao();
    }
}
