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
import java.util.List;
import java.util.Optional;

public class MySQLOrderDao extends AbstractDao<Order> implements OrderDao {
    private static final String TABLE_NAME = "book_order";

    private static final String FIND_ALL_SQL = "select book_order.id, book_order.reader, book_order.book, book_order.date, status.name from book_order inner join status on book_order.status = status.id";
    private static final String FIND_BY_ID_SQL = String.format("%s where book_order.id = ?", FIND_ALL_SQL);
    private static final String FIND_BY_USER_ID_SQL = String.format("%s where book_order.reader = ?", FIND_ALL_SQL);
    private static final String FIND_BY_BOOK_ID_SQL = String.format("%s where book_order.book = ?", FIND_ALL_SQL);
    private static final String FIND_BY_DATE_SQL = String.format("%s where book_order.date = ?", FIND_ALL_SQL);
    private static final String SAVE_SQL = "insert into book_order (reader, book) value (?, ?)";
    private static final String UPDATE_SQL = "update book_order set reader = ?, book = ?, date = ?, status = ? where id = ?";
    private static final String DELETE_SQL = "delete from book_order where id = ?";


    private MySQLOrderDao() {
        super(TABLE_NAME, FIND_ALL_SQL, FIND_BY_ID_SQL, SAVE_SQL, UPDATE_SQL, DELETE_SQL);
    }

    public static MySQLOrderDao getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    protected Order mapResultSet(ResultSet result) throws SQLException {
        final Optional<User> optionalLibraryUser = DAOFactory.getInstance().getUserDao().findById(result.getLong("reader"));
        final Optional<Book> optionalBook = DAOFactory.getInstance().getBookDao().findById(result.getLong("book"));
        if (!optionalLibraryUser.isPresent()) {
            throw new DAOException("User was not found");
        }
        if (!optionalBook.isPresent()) {
            throw new DAOException("Book was not found");
        }
        final long id = result.getLong("id");
        final Status status = Status.valueOf(result.getString("status.name"));
        final LocalDate orderDate = result.getObject("date", LocalDate.class);
        return new Order(id, optionalLibraryUser.get(), optionalBook.get(), orderDate, status);
    }

    @Override
    protected void setSavePrepareStatementValues(Order entity, PreparedStatement savePreparedStatement) throws SQLException, DAOException {
        final Optional<User> optionalUser = DAOFactory.getInstance().getUserDao().findById(entity.getUser().getId());
        final Optional<Book> optionalBook = DAOFactory.getInstance().getBookDao().findById(entity.getBook().getId());
        if (!optionalUser.isPresent()) {
            throw new DAOException("User was not found");
        }
        if (!optionalBook.isPresent()) {
            throw new DAOException("Book was not found");
        }
        final User user = optionalUser.get();
        final Book book = optionalBook.get();
        savePreparedStatement.setLong(1, user.getId());
        savePreparedStatement.setLong(2, book.getId());
    }

    @Override
    protected void setUpdatePreparedStatementValues(Order entity, PreparedStatement updatePreparedStatement) throws SQLException, DAOException {
        updatePreparedStatement.setLong(1, entity.getUser().getId());
        updatePreparedStatement.setLong(2, entity.getBook().getId());
        updatePreparedStatement.setObject(3, entity.getOrderDate());
        updatePreparedStatement.setLong(4, entity.getStatus().getId());
        updatePreparedStatement.setLong(5, entity.getId());
    }

    @Override
    public List<Order> findOrdersByBookId(Long bookId) throws DAOException {
        return findPreparedEntities(FIND_BY_BOOK_ID_SQL, preparedStatement -> preparedStatement.setLong(1, bookId));
    }

    @Override
    public List<Order> findOrdersByOrderDate(LocalDate orderDate) throws DAOException {
        return findPreparedEntities(FIND_BY_DATE_SQL, preparedStatement -> preparedStatement.setObject(1, orderDate));
    }

    @Override
    public List<Order> findOrdersByUserId(Long userId) {
        return findPreparedEntities(FIND_BY_USER_ID_SQL, preparedStatement -> preparedStatement.setLong(1, userId));
    }

    private static class Singleton {
        private static final MySQLOrderDao INSTANCE = new MySQLOrderDao();
    }
}
