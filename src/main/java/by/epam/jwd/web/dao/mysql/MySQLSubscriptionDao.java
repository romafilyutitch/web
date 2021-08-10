package by.epam.jwd.web.dao.mysql;


import by.epam.jwd.web.dao.api.AbstractDao;
import by.epam.jwd.web.dao.api.SubscriptionDao;
import by.epam.jwd.web.model.Subscription;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * {@link AbstractDao} implementation for {@link Subscription} database entity.
 * Links to subscription database table and performs operations with that table.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class MySQLSubscriptionDao extends AbstractDao<Subscription> implements SubscriptionDao {
    private static final String TABLE_NAME = "subscription";

    private static final String FIND_ALL_SQL = "select subscription.id, subscription.start_date, subscription.end_date from subscription";
    private static final String SAVE_SQL = "insert into subscription (start_date, end_date) values (?, ?)";
    private static final String UPDATE_SQL = "update subscription set start_date = ?, end_date = ? where id = ?";
    private static final String DELETE_SQL = "delete from subscription where id = ?";


    private static final String FIND_BY_START_DATE_TEMPLATE = "%s where subscription.start_date = ?";
    private static final String FIND_BY_END_DATE_TEMPLATE = "%s where subscription.end_date = ?";
    private static final String FIND_BY_START_DATE_SQL = String.format(FIND_BY_START_DATE_TEMPLATE, FIND_ALL_SQL);
    private static final String FIND_BY_END_DATE_SQL = String.format(FIND_BY_END_DATE_TEMPLATE, FIND_ALL_SQL);

    private static final String SUBSCRIPTION_ID_COLUMN = "subscription.id";
    private static final String SUBSCRIPTION_START_DATE_COLUMN = "subscription.start_date";
    private static final String SUBSCRIPTION_END_DATE_COLUMN = "subscription.end_date";


    private MySQLSubscriptionDao() {
        super(TABLE_NAME, FIND_ALL_SQL, SAVE_SQL, UPDATE_SQL, DELETE_SQL, SUBSCRIPTION_START_DATE_COLUMN);
    }

    /**
     * Returns single class instance from nested class that encapsulates single {@link MySQLSubscriptionDao} instance.
     * @return class instance.
     */
    public static MySQLSubscriptionDao getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Maps find execution result set to {@link Subscription} instance.
     * Template method implementation for {@link Subscription} database entity.
     * @param result Made during sql find statement execution result.
     * @return Mapped {@link Subscription} instance.
     * @throws SQLException when database excecption occcurs.
     */
    @Override
    protected Subscription mapResultSet(ResultSet result) throws SQLException {
        final long id = result.getLong(SUBSCRIPTION_ID_COLUMN);
        final LocalDate startDate = result.getObject(SUBSCRIPTION_START_DATE_COLUMN, LocalDate.class);
        final LocalDate endDate = result.getObject(SUBSCRIPTION_END_DATE_COLUMN, LocalDate.class);
        return new Subscription(id, startDate, endDate);
    }

    /**
     * Set {@link Subscription} instance to execute save prepared statement.
     * Template method implementation for {@link Subscription} database entity.
     * @param entity entity that need to save.
     * @param savePreparedStatement Made save entity prepared statement.
     * @throws SQLException when database exception occurs.
     */
    @Override
    protected void setSavePrepareStatementValues(Subscription entity, PreparedStatement savePreparedStatement) throws SQLException {
        savePreparedStatement.setObject(1, entity.getStartDate());
        savePreparedStatement.setObject(2, entity.getEndDate());
    }

    /**
     * Set {@link Subscription} instance to execute update prepared statement.
     * Template method implementation for {@link Subscription} database entity.
     * @param entity entity that need to update.
     * @param updatePreparedStatement Made update entity prepared statement.
     * @throws SQLException when database exception occurs.
     */
    @Override
    protected void setUpdatePreparedStatementValues(Subscription entity, PreparedStatement updatePreparedStatement) throws SQLException {
        updatePreparedStatement.setObject(1, entity.getStartDate());
        updatePreparedStatement.setObject(2, entity.getEndDate());
        updatePreparedStatement.setLong(3, entity.getId());
    }

    /**
     * Find and returns result of find subscriptions with passed start date.
     * @param startDate {@link LocalDate} find subscription start date
     * @return subscriptions that have passed start date.
     */
    @Override
    public List<Subscription> findByStartDate(LocalDate startDate) {
        return findPreparedEntities(FIND_BY_START_DATE_SQL, preparedStatement -> preparedStatement.setObject(1, startDate));
    }

    /**
     * Find and returns result of find subscription with passed end date.
     * @param endDate {@link LocalDate} find Subscription end date.
     * @return subscriptions that have passed end date.
     */
    @Override
    public List<Subscription> findByEndDate(LocalDate endDate) {
        return findPreparedEntities(FIND_BY_END_DATE_SQL, preparedStatement -> preparedStatement.setObject(1, endDate));
    }

    /**
     * Nested class that encapsulates single {@link MySQLSubscriptionDao} instance.
     * Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final MySQLSubscriptionDao INSTANCE = new MySQLSubscriptionDao();
    }
}
