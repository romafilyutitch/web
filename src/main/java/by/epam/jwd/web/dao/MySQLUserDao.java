package by.epam.jwd.web.dao;



import by.epam.jwd.web.exception.DAOException;
import by.epam.jwd.web.model.Subscription;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.model.UserRole;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MySQLUserDao extends AbstractDao<User> implements UserDao {

    private static final String TABLE_NAME = "user";
    private static final String ID_COLUMN = "id";
    private static final String LOGIN_COLUMN = "login";
    private static final String PASSWORD_COLUMN = "password";
    private static final String ROLE_COLUMN = "role";
    private static final String SUBSCRIPTION_COLUMN = "subscription";

    private static final String SAVE_PREPARED_SQL = String.format("insert into %S (%s, %s, %s, %s) values (?, ?, ?, ?)",
            TABLE_NAME, LOGIN_COLUMN, PASSWORD_COLUMN, ROLE_COLUMN, SUBSCRIPTION_COLUMN);
    private static final String FIND_ALL_SQL = String.format("select %s, %s, %s, %s, %s from %s",
            ID_COLUMN, LOGIN_COLUMN, PASSWORD_COLUMN, ROLE_COLUMN, SUBSCRIPTION_COLUMN, TABLE_NAME);
    private static final String UPDATE_PREPARED_SQL = String.format("update %s set %s = ?, %s = ?, %s = ?, %s = ? where %s = ?",
            TABLE_NAME, LOGIN_COLUMN, PASSWORD_COLUMN, ROLE_COLUMN, SUBSCRIPTION_COLUMN, ID_COLUMN);
    private static final String DELETE_PREPARED_SQL = String.format("delete from %s where %s = ?", TABLE_NAME, ID_COLUMN);
    public static final String USER_ROLE_NOT_FOUND_MESSAGE = "Saved user role not found by id";

    private MySQLUserDao() {
        super(FIND_ALL_SQL, SAVE_PREPARED_SQL, UPDATE_PREPARED_SQL, DELETE_PREPARED_SQL);
    }

    public static MySQLUserDao getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    protected User mapResultSet(ResultSet result) throws SQLException, DAOException {
        final Optional<UserRole> optionalUserRole = DAOFactory.getInstance().getRoleDao().findById(result.getLong(ROLE_COLUMN));
        final Optional<Subscription> optionalSubscription = DAOFactory.getInstance().getSubscriptionDao().findById(result.getLong(SUBSCRIPTION_COLUMN));
        if (!optionalUserRole.isPresent()) {
            throw new DAOException(USER_ROLE_NOT_FOUND_MESSAGE);
        }
        final Long id = result.getLong(ID_COLUMN);
        final String login = result.getString(LOGIN_COLUMN);
        final String password = result.getString(PASSWORD_COLUMN);
        return new User(id, login, password, optionalUserRole.get(), optionalSubscription.orElse(null));
    }

    @Override
    protected void setSavePrepareStatementValues(User entity, PreparedStatement savePreparedStatement) throws SQLException, DAOException {
        savePreparedStatement.setString(1, entity.getLogin());
        savePreparedStatement.setString(2, entity.getPassword());
        savePreparedStatement.setLong(3, entity.getRole().getId());
        if (entity.getSubscription() != null) {
            DAOFactory.getInstance().getSubscriptionDao().save(entity.getSubscription());
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
        return findAll().stream().filter(libraryUser -> libraryUser.getLogin().equals(login)).findAny();
    }

    @Override
    public List<User> findUsersByRole(UserRole role) throws DAOException {
        return findAll().stream().filter(libraryUser -> libraryUser.getRole().equals(role)).collect(Collectors.toList());
    }

    private static class Singleton {
        private static final MySQLUserDao INSTANCE = new MySQLUserDao();
    }
}
