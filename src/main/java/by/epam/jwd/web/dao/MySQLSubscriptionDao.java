package by.epam.jwd.web.dao;



import by.epam.jwd.web.model.Subscription;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class MySQLSubscriptionDao extends AbstractDao<Subscription> implements SubscriptionDao {

    private static final String SAVE_PREPARED_SQL = "insert into reader_subscription (start_date, end_date) values (?, ?)";
    private static final String FIND_ALL_SQL = "select id, start_date, end_date from reader_subscription";
    private static final String UPDATE_PREPARED_SQL = "update reader_subscription set start_date = ?, end_date = ? where id = ?";
    private static final String DELETE_PREPARED_SQL = "delete from reader_subscription where id = ?";
    public static final String ID_COLUMN = "id";
    public static final String START_DATE_COLUMN = "start_date";
    public static final String END_DATE_COLUMN = "end_date";

    private MySQLSubscriptionDao() {
       super(FIND_ALL_SQL, SAVE_PREPARED_SQL, UPDATE_PREPARED_SQL);
    }

    public static MySQLSubscriptionDao getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    protected Subscription mapResultSet(ResultSet result) throws SQLException {
        return new Subscription(result.getLong(ID_COLUMN), result.getObject(START_DATE_COLUMN, LocalDate.class), result.getObject(END_DATE_COLUMN,LocalDate.class));
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
        return findAll().stream().filter(subscription -> subscription.getStartDate().equals(startDate)).collect(Collectors.toList());
    }

    @Override
    public List<Subscription> findByEndDate(LocalDate endDate) throws DAOException {
        return findAll().stream().filter(subscription -> subscription.getEndDate().equals(endDate)).collect(Collectors.toList());
    }

    @Override
    public List<Subscription> findBetween(LocalDate startDate, LocalDate endDate) throws DAOException {
        return findAll().stream().filter(subscription -> subscription.getStartDate().isAfter(startDate) && subscription.getEndDate().isBefore(endDate)).collect(Collectors.toList());
    }

    private static class Singleton {
        private static final MySQLSubscriptionDao INSTANCE = new MySQLSubscriptionDao();
    }
}
