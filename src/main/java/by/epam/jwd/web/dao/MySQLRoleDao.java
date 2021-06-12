package by.epam.jwd.web.dao;



import by.epam.jwd.web.model.UserRole;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class MySQLRoleDao extends AbstractDao<UserRole> implements RoleDao {
    private static final String FIND_ALL_SQL = "select id, name from role";
    private static final String SAVE_PREPARED_SQL = "insert into role (name) values (?)";
    private static final String UPDATE_PREPARED_SQL = "update role set name = ? where id = ?";
    private static final String DELETE_PREPARED_SQL = "delete from role where id = ?";
    public static final String NAME_COLUMN = "name";

    private MySQLRoleDao() {
        super(FIND_ALL_SQL, SAVE_PREPARED_SQL, UPDATE_PREPARED_SQL, DELETE_PREPARED_SQL);
    }

    public static MySQLRoleDao getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    protected UserRole mapResultSet(ResultSet result) throws SQLException {
        return UserRole.valueOf(result.getString(NAME_COLUMN));
    }

    @Override
    protected void setSavePrepareStatementValues(UserRole entity, PreparedStatement savePreparedStatement) throws SQLException {
        savePreparedStatement.setString(1, entity.getName());
    }

    @Override
    protected void setUpdatePreparedStatementValues(UserRole entity, PreparedStatement updatePreparedStatement) throws SQLException {
        updatePreparedStatement.setString(1, entity.getName());
        updatePreparedStatement.setLong(2, entity.getId());
    }

    @Override
    public Optional<UserRole> findByName(String userRoleName) throws DAOException {
        return findAll().stream().filter(role -> role.getName().equals(userRoleName)).findAny();
    }

    private static class Singleton {
        private static final MySQLRoleDao INSTANCE = new MySQLRoleDao();
    }
}
