package by.epam.jwd.web.dao;

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

/**
 * {@link AbstractDao} implementation for {@link User} database entity.
 * Links to user database table and performs sql operations with that table.
 *
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
class MySQLUserDao extends AbstractDao<User> implements UserDao {
    private static final String TABLE_NAME = "user";

    private static final String FIND_ALL_SQL = "select user.id, user.login, user.password, role.id, role.name, subscription.id, subscription.start_date, subscription.end_date from user " +
            "inner join role on user.role_id = role.id left join subscription on user.subscription_id = subscription.id";
    private static final String SAVE_SQL = "insert into user (login, password, role_id, subscription_id) values (?, ?, ?, ?)";
    private static final String UPDATE_SQL = "update user set login = ?, password = ?, role_id = ?, subscription_id = ? where id = ?";
    private static final String DELETE_SQL = "delete from user where id = ?";

    private static final String FIND_BY_LOGIN_TEMPLATE = "%s where user.login = ?";
    private static final String FIND_BY_ROLE_TEMPLATE = "%s where role.id = ?";
    private static final String FIND_PAGE_SQL_TEMPLATE = "%s order by user.login limit ?, ?";
    private static final String FIND_PAGE_SQL = String.format(FIND_PAGE_SQL_TEMPLATE, FIND_ALL_SQL);
    private static final String FIND_BY_LOGIN_SQL = String.format(FIND_BY_LOGIN_TEMPLATE, FIND_ALL_SQL);
    private static final String FIND_BY_ROLE_SQL = String.format(FIND_BY_ROLE_TEMPLATE, FIND_ALL_SQL);

    private static final String USER_ID_COLUMN = "user.id";
    private static final String USER_LOGIN_COLUMN = "user.login";
    private static final String USER_PASSWORD_COLUMN = "user.password";
    private static final String ROLE_NAME_COLUMN = "role.name";
    private static final String SUBSCRIPTION_ID_COLUMN = "subscription.id";
    private static final String SUBSCRIPTION_START_DATE_COLUMN = "subscription.start_date";
    private static final String SUBSCRIPTION_END_DATE_COLUMN = "subscription.end_date";

    private MySQLUserDao() {
        super(TABLE_NAME, FIND_ALL_SQL, FIND_PAGE_SQL, SAVE_SQL, UPDATE_SQL, DELETE_SQL);
    }

    /**
     * Returns class instance from nested class that encapsulates single {@link MySQLUserDao} instance.
     *
     * @return class instance.
     */
    public static MySQLUserDao getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Maps find entity result set to {@link User} instance.
     * Template method implementation for {@link User} database entity.
     *
     * @param result Made during sql find statement execution result.
     * @return Mapped {@link User} instance.
     * @throws SQLException when database exeption occurs.
     */
    @Override
    protected User mapResultSet(ResultSet result) throws SQLException {
        final long id = result.getLong(USER_ID_COLUMN);
        final String login = result.getString(USER_LOGIN_COLUMN);
        final String password = result.getString(USER_PASSWORD_COLUMN);
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

    /**
     * Set {@link User} instance data to execute save prepared statement.
     * Template method implementation for {@link User} database entity.
     *
     * @param entity                entity that need to save.
     * @param savePreparedStatement Made save entity prepared statement.
     * @throws SQLException when database exception occurs.
     */
    @Override
    protected void setSavePrepareStatementValues(User entity, PreparedStatement savePreparedStatement) throws SQLException {
        savePreparedStatement.setString(1, entity.getLogin());
        savePreparedStatement.setString(2, entity.getPassword());
        savePreparedStatement.setLong(3, entity.getRole().getId());
        if (entity.getSubscription() != null) {
            savePreparedStatement.setLong(4, entity.getSubscription().getId());
        } else {
            savePreparedStatement.setNull(4, Types.INTEGER);
        }
    }

    /**
     * Set {@link User} instance data to perform update prepared statement.
     *
     * @param entity                  entity that need to update.
     * @param updatePreparedStatement Made update entity prepared statement.
     * @throws SQLException when database exception occurs.
     */
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

    /**
     * Finds and returns result of find user by passed login.
     * Returns found user in optional if there is user with passed login
     * or empty optional otherwise.
     *
     * @param login of user that need to find.
     * @return found user in optional if there is user with passed login in database table
     * or empty optional otherwise.
     */
    @Override
    public Optional<User> findUserByLogin(String login) {
        final List<User> foundUsers = findPreparedEntities(FIND_BY_LOGIN_SQL, preparedStatement -> preparedStatement.setString(1, login));
        return foundUsers.stream().findAny();
    }

    /**
     * Finds and returns result of fund users by passed role.
     * Returns users that have passed role.
     *
     * @param role of users that need to find.
     * @return users that have passed role.
     */
    @Override
    public List<User> findUsersByRole(UserRole role) {
        return findPreparedEntities(FIND_BY_ROLE_SQL, preparedStatement -> preparedStatement.setLong(1, role.getId()));
    }

    /**
     * Nested class that encapsulates single {@link MySQLUserDao} instance.
     * Singleton pattern variation.
     *
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final MySQLUserDao INSTANCE = new MySQLUserDao();
    }
}
