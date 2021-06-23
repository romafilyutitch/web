package by.epam.jwd.web.dao;


import by.epam.jwd.web.exception.DAOException;
import by.epam.jwd.web.model.Subscription;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class MySQLSubscriptionDao extends AbstractDao<Subscription> implements SubscriptionDao {
    private static final String TABLE_NAME = "subscription";
    private static final String ID_COLUMN = "id";
    private static final String START_DATE_COLUMN = "start_date";
    private static final String END_DATE_COLUMN = "end_date";

    private static final List<String> COLUMNS = Arrays.asList(ID_COLUMN, START_DATE_COLUMN, END_DATE_COLUMN);

    private final String findByStartDateSql;
    private final String findByEndDateSql;

    private MySQLSubscriptionDao() {
        super(TABLE_NAME, COLUMNS);
        final StringJoiner joiner = new StringJoiner(",");
        COLUMNS.forEach(joiner::add);
        findByStartDateSql = String.format(FIND_BY_COLUMN_SQL_TEMPLATE, joiner, TABLE_NAME, START_DATE_COLUMN);
        findByEndDateSql = String.format(FIND_BY_COLUMN_SQL_TEMPLATE, joiner, TABLE_NAME, END_DATE_COLUMN);
    }

    public static MySQLSubscriptionDao getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    protected Subscription mapResultSet(ResultSet result) throws SQLException {
        final long id = result.getLong(ID_COLUMN);
        final LocalDate startDate = result.getObject(START_DATE_COLUMN, LocalDate.class);
        final LocalDate endDate = result.getObject(END_DATE_COLUMN, LocalDate.class);
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
        return findPreparedEntities(findByStartDateSql, preparedStatement -> preparedStatement.setObject(1, startDate));
    }

    @Override
    public List<Subscription> findByEndDate(LocalDate endDate) throws DAOException {
        return findPreparedEntities(findByEndDateSql, preparedStatement -> preparedStatement.setObject(1, endDate));
    }

    private static class Singleton {
        private static final MySQLSubscriptionDao INSTANCE = new MySQLSubscriptionDao();
    }
}
