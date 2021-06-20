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

    private static final String FIND_ALL_SQL = "select user.id, user.login, user.password, role.name, subscription.id, subscription.start_date, subscription.end_date from user inner join role on user.role = role.id left outer join subscription on user.subscription = subscription.id";
    private static final String FIND_BY_ID_SQL = String.format("%s where user.id = ?", FIND_ALL_SQL);
    private static final String FIND_BY_LOGIN_SQL = String.format("%s where user.login = ?", FIND_ALL_SQL);
    private static final String FIND_BY_ROLE_SQL = String.format("%s where role.id = ?", FIND_ALL_SQL);
    private static final String SAVE_SQL = "insert into user (login, password, role, subscription) value (?, ?, ?, ?)";
    private static final String UPDATE_SQL = "update user set login = ?, password = ?, role = ?, subscription = ? where id = ?";
    private static final String DELETE_SQL = "delete user where id = ?";

    private MySQLUserDao() {
        super(TABLE_NAME, FIND_ALL_SQL, FIND_BY_ID_SQL, SAVE_SQL, UPDATE_SQL, DELETE_SQL);
    }

    public static MySQLUserDao getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    protected User mapResultSet(ResultSet result) throws SQLException, DAOException {
        final long id = result.getLong("user.id");
        final String login = result.getString("user.login");
        final String password = result.getString("user.password");
        final UserRole role = UserRole.valueOf(result.getString("role.name"));
        final long subscriptionId = result.getLong("subscription.id");
        final LocalDate startDate = result.getObject("subscription.start_date", LocalDate.class);
        final LocalDate endDate = result.getObject("subscription.end_date", LocalDate.class);
        return new User(id, login, password, role, subscriptionId != 0 ? new Subscription(subscriptionId, startDate, endDate) : null);
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
