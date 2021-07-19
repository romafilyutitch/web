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
import java.util.List;
import java.util.Optional;

public class MySQLUserDao extends AbstractDao<User> implements UserDao {
    private static final String TABLE_NAME = "user";

    private static final String FIND_ALL_SQL = "select user.id, user.login, user.password, role.id, role.name, subscription.id, subscription.start_date, subscription.end_date from user\n" +
            "inner join role on user.role_id = role.id left join subscription on user.subscription_id = subscription.id";
    private static final String SAVE_SQL = "insert into user (login, password, role_id, subscription_id) values (?, ?, ?, ?)";
    private static final String UPDATE_SQL = "update user set login = ?, password = ?, role_id = ?, subscription_id = ? where id = ?";
    private static final String DELETE_SQL = "delete from user where id = ?";

    private static final String FIND_BY_LOGIN_TEMPLATE = "%s where user.login = ?";
    private static final String FIND_BY_ROLE_TEMPLATE = "%s where role.id = ?";
    private static final String FIND_BY_LOGIN_SQL = String.format(FIND_BY_LOGIN_TEMPLATE, FIND_ALL_SQL);
    private static final String FIND_BY_ROLE_SQL = String.format(FIND_BY_ROLE_TEMPLATE, FIND_ALL_SQL);

    private static final String ID_COLUMN = "user.id";
    private static final String LOGIN_COLUMN = "user.login";
    private static final String PASSWORD_COLUMN = "user.password";
    private static final String ROLE_NAME_COLUMN = "role.name";
    private static final String SUBSCRIPTION_ID_COLUMN = "subscription.id";
    private static final String SUBSCRIPTION_START_DATE_COLUMN = "subscription.start_date";
    private static final String SUBSCRIPTION_END_DATE_COLUMN = "subscription.end_date";

    private MySQLUserDao() {
        super(TABLE_NAME, FIND_ALL_SQL, SAVE_SQL, UPDATE_SQL, DELETE_SQL);
    }

    public static MySQLUserDao getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    protected User mapResultSet(ResultSet result) throws SQLException, DAOException {
        final long id = result.getLong(ID_COLUMN);
        final String login = result.getString(LOGIN_COLUMN);
        final String password = result.getString(PASSWORD_COLUMN);
        final String roleName = result.getString(ROLE_NAME_COLUMN);
        final UserRole role = UserRole.valueOf(roleName.toUpperCase());
        final Subscription subscription = buildSubscription(result);
        return new User(id, login, password, role, subscription);
    }

    private Subscription buildSubscription(ResultSet resultSet) throws SQLException {
        final long subscriptionId = resultSet.getLong(SUBSCRIPTION_ID_COLUMN);
        if (subscriptionId == 0) {
            return null;
        } else {
            final LocalDate startDate = resultSet.getObject(SUBSCRIPTION_START_DATE_COLUMN, LocalDate.class);
            final LocalDate endDate = resultSet.getObject(SUBSCRIPTION_END_DATE_COLUMN, LocalDate.class);
            return new Subscription(subscriptionId, startDate, endDate);
        }
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
        final List<User> foundUsers = findPreparedEntities(FIND_BY_LOGIN_SQL, preparedStatement -> preparedStatement.setString(1, login));
        return foundUsers.stream().findAny();
    }

    @Override
    public List<User> findUsersByRole(UserRole role) throws DAOException {
        return findPreparedEntities(FIND_BY_ROLE_SQL, preparedStatement -> preparedStatement.setLong(1, role.getId()));
    }

    private static class Singleton {
        private static final MySQLUserDao INSTANCE = new MySQLUserDao();
    }
}
