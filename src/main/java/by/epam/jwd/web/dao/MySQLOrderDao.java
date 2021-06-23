package by.epam.jwd.web.dao;


import by.epam.jwd.web.exception.DAOException;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Order;
import by.epam.jwd.web.model.Status;
import by.epam.jwd.web.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

public class MySQLOrderDao extends AbstractDao<Order> implements OrderDao {
    private static final String TABLE_NAME = "book_order";
    private static final String ID_COLUMN = "id";
    private static final String READER_COLUMN = "reader";
    private static final String BOOK_COLUMN = "book";
    private static final String DATE_COLUMN = "date";
    private static final String STATUS_COLUMN = "status";

    private static final List<String> COLUMNS = Arrays.asList(ID_COLUMN, READER_COLUMN, BOOK_COLUMN, STATUS_COLUMN, DATE_COLUMN);

    private final String findByUserIdSql;
    private final String findByBookIdSql;
    private final String findByDateSql;

    private MySQLOrderDao() {
        super(TABLE_NAME, COLUMNS);
        final StringJoiner joiner = new StringJoiner(",");
        COLUMNS.forEach(joiner::add);
        findByUserIdSql = String.format(FIND_BY_COLUMN_SQL_TEMPLATE, joiner, TABLE_NAME, READER_COLUMN);
        findByBookIdSql = String.format(FIND_BY_COLUMN_SQL_TEMPLATE, joiner, TABLE_NAME, BOOK_COLUMN);
        findByDateSql = String.format(FIND_BY_COLUMN_SQL_TEMPLATE, joiner, TABLE_NAME, DATE_COLUMN);
    }

    public static MySQLOrderDao getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    protected Order mapResultSet(ResultSet result) throws SQLException {
        final long id = result.getLong(ID_COLUMN);
        final long readerId = result.getLong(READER_COLUMN);
        final long bookId = result.getLong(BOOK_COLUMN);
        final Status status = Status.getInstance(result.getLong(STATUS_COLUMN));
        final LocalDate orderDate = result.getObject(DATE_COLUMN, LocalDate.class);
        return new Order(id, new User(readerId), new Book(bookId), orderDate, status);
    }

    @Override
    protected void setSavePrepareStatementValues(Order entity, PreparedStatement savePreparedStatement) throws SQLException, DAOException {
        savePreparedStatement.setLong(1, entity.getUser().getId());
        savePreparedStatement.setLong(2, entity.getBook().getId());
        savePreparedStatement.setLong(3, entity.getStatus().getId());
        savePreparedStatement.setObject(4, entity.getOrderDate());
    }

    @Override
    protected void setUpdatePreparedStatementValues(Order entity, PreparedStatement updatePreparedStatement) throws SQLException, DAOException {
        updatePreparedStatement.setLong(1, entity.getUser().getId());
        updatePreparedStatement.setLong(2, entity.getBook().getId());
        updatePreparedStatement.setLong(3, entity.getStatus().getId());
        updatePreparedStatement.setObject(4, entity.getOrderDate());
        updatePreparedStatement.setLong(5, entity.getId());
    }

    @Override
    public List<Order> findOrdersByBookId(Long bookId) throws DAOException {
        return findPreparedEntities(findByBookIdSql, preparedStatement -> preparedStatement.setLong(1, bookId));
    }

    @Override
    public List<Order> findOrdersByOrderDate(LocalDate orderDate) throws DAOException {
        return findPreparedEntities(findByDateSql, preparedStatement -> preparedStatement.setObject(1, orderDate));
    }

    @Override
    public List<Order> findOrdersByUserId(Long userId) {
        return findPreparedEntities(findByUserIdSql, preparedStatement -> preparedStatement.setLong(1, userId));
    }

    private static class Singleton {
        private static final MySQLOrderDao INSTANCE = new MySQLOrderDao();
    }
}
