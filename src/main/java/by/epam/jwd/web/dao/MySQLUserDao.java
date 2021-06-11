package by.epam.jwd.web.dao;



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
    private static final MySQLSubscriptionDao SUBSCRIPTION_DAO = MySQLSubscriptionDao.getInstance();
    private static final MySQLRoleDao ROLE_DAO = MySQLRoleDao.getInstance();

    private static final String ID_COLUMN = "id";
    private static final String LOGIN_COLUMN = "login";
    private static final String PASSWORD_COLUMN = "password";
    private static final String ROLE_COLUMN = "role";
    private static final String SUBSCRIPTION_COLUMN = "subscription";
    private static final String SAVE_PREPARED_SQL = "insert into lib_user (login, password, role, subscription) values (?, ?, ?, ?) ";
    private static final String FIND_ALL_SQL = "select id, login, password, role, subscription from lib_user";
    private static final String UPDATE_PREPARED_SQL = "update lib_user set login = ?, password = ?, role = ?, subscription = ? where id = ?";
    private static final String DELETE_PREPARED_SQL = "delete from lib_user where id = ?";

    private MySQLUserDao() {
        super(FIND_ALL_SQL, SAVE_PREPARED_SQL, UPDATE_PREPARED_SQL, DELETE_PREPARED_SQL);
    }

    public static MySQLUserDao getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public User save(User entity) {
        if (UserRole.UNAUTHORIZED.equals(entity.getRole())) {
            throw new DAOException("Save unauthorized users is forbidden");
        } else {
            return super.save(entity);
        }
    }

    @Override
    protected User mapResultSet(ResultSet result) throws SQLException, DAOException {
        final Optional<UserRole> optionalUserRole = ROLE_DAO.findById(result.getLong(ROLE_COLUMN));
        final Optional<Subscription> optionalSubscription = SUBSCRIPTION_DAO.findById(result.getLong(SUBSCRIPTION_COLUMN));
        final Long id = result.getLong(ID_COLUMN);
        final String login = result.getString(LOGIN_COLUMN);
        final String password = result.getString(PASSWORD_COLUMN);
        return new User(id, login, password, optionalUserRole.orElseThrow(DAOException::new), optionalSubscription.orElse(null));
    }

    @Override
    protected void setSavePrepareStatementValues(User entity, PreparedStatement savePreparedStatement) throws SQLException, DAOException {
        savePreparedStatement.setString(1, entity.getLogin());
        savePreparedStatement.setString(2, entity.getPassword());
        savePreparedStatement.setLong(3, entity.getRole().getId());
        if (entity.getSubscription() != null) {
            SUBSCRIPTION_DAO.save(entity.getSubscription());
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
