package by.epam.jwd.web.dao.mysql;


import by.epam.jwd.web.dao.AbstractDao;
import by.epam.jwd.web.dao.SubscriptionDao;
import by.epam.jwd.web.exception.DAOException;
import by.epam.jwd.web.model.Subscription;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

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
        super(TABLE_NAME, FIND_ALL_SQL, SAVE_SQL, UPDATE_SQL, DELETE_SQL);
    }

    public static MySQLSubscriptionDao getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    protected Subscription mapResultSet(ResultSet result) throws SQLException {
        final long id = result.getLong(SUBSCRIPTION_ID_COLUMN);
        final LocalDate startDate = result.getObject(SUBSCRIPTION_START_DATE_COLUMN, LocalDate.class);
        final LocalDate endDate = result.getObject(SUBSCRIPTION_END_DATE_COLUMN, LocalDate.class);
        return new Subscription(id, startDate, endDate);
    }

    @Override
    protected void setSavePrepareStatementValues(Subscription entity, PreparedStatement savePreparedStatement) throws SQLException {
        savePreparedStatement.setObject(1, entity.getStartDate());
        savePreparedStatement.setObject(2, entity.getEndDate());
    }

    @Override
    protected void setUpdatePreparedStatementValues(Subscription entity, PreparedStatement updatePreparedStatement) throws SQLException {
        updatePreparedStatement.setObject(1, entity.getStartDate());
        updatePreparedStatement.setObject(2, entity.getEndDate());
        updatePreparedStatement.setLong(3, entity.getId());
    }

    @Override
    public List<Subscription> findByStartDate(LocalDate startDate) throws DAOException {
        return findPreparedEntities(FIND_BY_START_DATE_SQL, preparedStatement -> preparedStatement.setObject(1, startDate));
    }

    @Override
    public List<Subscription> findByEndDate(LocalDate endDate) throws DAOException {
        return findPreparedEntities(FIND_BY_END_DATE_SQL, preparedStatement -> preparedStatement.setObject(1, endDate));
    }

    private static class Singleton {
        private static final MySQLSubscriptionDao INSTANCE = new MySQLSubscriptionDao();
    }
}
