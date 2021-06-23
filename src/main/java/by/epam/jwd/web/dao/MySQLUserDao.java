package by.epam.jwd.web.dao;



import by.epam.jwd.web.exception.DAOException;
import by.epam.jwd.web.model.Subscription;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.model.UserRole;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

public class MySQLUserDao extends AbstractDao<User> implements UserDao {
    private static final String TABLE_NAME = "user";
    private static final String ID_COLUMN = "id";
    private static final String LOGIN_COLUMN = "login";
    private static final String PASSWORD_COLUMN = "password";
    private static final String ROLE_COLUMN = "role";
    private static final String SUBSCRIPTION_COLUMN = "subscription";

    private static final List<String> COLUMNS = Arrays.asList(ID_COLUMN, LOGIN_COLUMN, PASSWORD_COLUMN, ROLE_COLUMN, SUBSCRIPTION_COLUMN);

    private final String findByLoginSql;
    private final String findByRoleSql;

    private MySQLUserDao() {
        super(TABLE_NAME, COLUMNS);
        final StringJoiner joiner = new StringJoiner(",");
        COLUMNS.forEach(joiner::add);
        findByLoginSql = String.format(FIND_BY_COLUMN_SQL_TEMPLATE, joiner, TABLE_NAME, LOGIN_COLUMN);
        findByRoleSql = String.format(FIND_BY_COLUMN_SQL_TEMPLATE, joiner, TABLE_NAME, ROLE_COLUMN);
    }

    public static MySQLUserDao getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    protected User mapResultSet(ResultSet result) throws SQLException, DAOException {
        final long id = result.getLong(ID_COLUMN);
        final String login = result.getString(LOGIN_COLUMN);
        final String password = result.getString(PASSWORD_COLUMN);
        final UserRole role = UserRole.getInstance(result.getLong(ROLE_COLUMN));
        final long subscriptionId = result.getLong(SUBSCRIPTION_COLUMN);
        return new User(id, login, password, role, subscriptionId != 0 ? new Subscription(subscriptionId) : null);
    }

    @Override
    protected void setSavePrepareStatementValues(User entity, PreparedStatement savePreparedStatement) throws SQLException, DAOException {
        savePreparedStatement.setString(1, entity.getLogin());
        savePreparedStatement.setString(2, entity.getPassword());
        savePreparedStatement.setLong(3, entity.getRole().getId());
        if (entity.getSubscription() != null) {
            savePreparedStatement.setLong(4, entity.getSubscription().getId());
        } else {
            savePreparedStatement.setNull(4, Types.INTEGER);
        }
    }

    @Override
    protected void setUpdatePreparedStatementValues(User entity, PreparedStatement updatePreparedStatement) throws SQLException {
        updatePreparedStatement.setString(1, entity.getLogin());
        updatePreparedStatement.setString(2, entity.getPassword());
        updatePreparedStatement.setLong(3, entity.getRole().getId());
        if (entity.getSubscription() != null) {
            updatePreparedStatement.setLong(4, entity.getSubscription().getId());
        } else {
            updatePreparedStatement.setNull(4, Types.INTEGER);
        }
        updatePreparedStatement.setLong(5, entity.getId());
    }

    @Override
    public Optional<User> findUserByLogin(String login) throws DAOException {
        final List<User> foundUsers = findPreparedEntities(findByLoginSql, preparedStatement -> preparedStatement.setString(1, login));
        return foundUsers.stream().findAny();
    }

    @Override
    public List<User> findUsersByRole(UserRole role) throws DAOException {
        return findPreparedEntities(findByRoleSql, preparedStatement -> preparedStatement.setLong(1, role.getId()));
    }

    private static class Singleton {
        private static final MySQLUserDao INSTANCE = new MySQLUserDao();
    }
}
