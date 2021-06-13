package by.epam.jwd.web.dao;



import by.epam.jwd.web.exception.DAOException;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.BookOrder;
import by.epam.jwd.web.model.Status;
import by.epam.jwd.web.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MySQLOrderDao extends AbstractDao<BookOrder> implements OrderDao {

    private static final String TABLE_NAME = "book_order";
    private static final String ID_COLUMN = "id";
    private static final String READER_COLUMN = "reader";
    private static final String BOOK_COLUMN = "book";
    private static final String ORDER_DATE_COLUMN = "date";
    public static final String STATUS_COLUMN = "status";
    private static final String SAVE_PREPARED_SQL = String.format("insert into %s (%s, %s) values (?, ?)", TABLE_NAME, READER_COLUMN, BOOK_COLUMN);
    private static final String FIND_ALL_SQL = String.format("select %s, %s, %s, %s, %s from %s",
            ID_COLUMN, READER_COLUMN, BOOK_COLUMN, ORDER_DATE_COLUMN, STATUS_COLUMN, TABLE_NAME);//todo mysql does not see table order table change in future
    private static final String UPDATE_PREPARED_SQL = String.format("update %s set %s = ?, %s = ?, %s = ?, %s = ? where %s = ?",
            TABLE_NAME, READER_COLUMN, BOOK_COLUMN, ORDER_DATE_COLUMN, STATUS_COLUMN, ID_COLUMN);
    private static final String DELETE_PREPARED_SQL = String.format("delete from %s where %s = ?", TABLE_NAME, ID_COLUMN);
    private static final String USER_WAS_NOT_FOUND_MESSAGE = "Saved order user was not found by id";
    private static final String BOOK_WAS_NOT_FOUND_MESSAGE = "Saved order book was not found by id";
    private static final String ORDER_STATUS_WAS_NOT_FOUND_MESSAGE = "Saved order status was not found by id";

    private MySQLOrderDao() {
        super(FIND_ALL_SQL, SAVE_PREPARED_SQL, UPDATE_PREPARED_SQL, DELETE_PREPARED_SQL);
    }

    public static MySQLOrderDao getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    protected BookOrder mapResultSet(ResultSet result) throws SQLException, DAOException {
        final Optional<User> optionalLibraryUser = DAOFactory.getInstance().getUserDao().findById(result.getLong(READER_COLUMN));
        final Optional<Book> optionalBook = DAOFactory.getInstance().getBookDao().findById(result.getLong(BOOK_COLUMN));
        final Optional<Status> optionalStatus = DAOFactory.getInstance().getStatusDao().findById(result.getLong(STATUS_COLUMN));
        if(!optionalLibraryUser.isPresent()) {
            throw new DAOException(USER_WAS_NOT_FOUND_MESSAGE);
        }
        if (!optionalBook.isPresent()) {
            throw new DAOException(BOOK_WAS_NOT_FOUND_MESSAGE);
        }
        if (!optionalStatus.isPresent()) {
            throw new DAOException(ORDER_STATUS_WAS_NOT_FOUND_MESSAGE);
        }
        final LocalDate orderDate = result.getObject(ORDER_DATE_COLUMN, LocalDate.class);
        return new BookOrder(result.getLong(ID_COLUMN), optionalLibraryUser.get(), optionalBook.get(), orderDate, optionalStatus.get());
    }

    @Override
    protected void setSavePrepareStatementValues(BookOrder entity, PreparedStatement savePreparedStatement) throws SQLException, DAOException {
        savePreparedStatement.setLong(1, DAOFactory.getInstance().getUserDao().save(entity.getUser()).getId());
        savePreparedStatement.setLong(2, DAOFactory.getInstance().getBookDao().save(entity.getBook()).getId());

    }

    @Override
    protected void setUpdatePreparedStatementValues(BookOrder entity, PreparedStatement updatePreparedStatement) throws SQLException, DAOException {
        updatePreparedStatement.setLong(1, entity.getUser().getId());
        updatePreparedStatement.setLong(2, entity.getBook().getId());
        updatePreparedStatement.setObject(3, entity.getOrderDate());
        updatePreparedStatement.setLong(4, entity.getStatus().getId());
        updatePreparedStatement.setLong(5, entity.getId());
    }

    @Override
    public List<BookOrder> findOrdersByUserLogin(String userLogin) throws DAOException {
        return findAll().stream().filter(bookOrder -> bookOrder.getUser().getLogin().equalsIgnoreCase(userLogin)).collect(Collectors.toList());
    }

    @Override
    public List<BookOrder> findOrdersByBookName(String bookName) throws DAOException {
        return findAll().stream().filter(bookOrder -> bookOrder.getBook().getName().equalsIgnoreCase(bookName)).collect(Collectors.toList());
    }

    @Override
    public List<BookOrder> findOrdersByOrderDate(LocalDate orderDate) throws DAOException {
        return findAll().stream().filter(bookOrder -> bookOrder.getOrderDate().equals(orderDate)).collect(Collectors.toList());
    }

    @Override
    public List<BookOrder> findOrdersByUserId(Long userId) {
        return findAll().stream().filter(bookOrder -> bookOrder.getUser().getId().equals(userId)).collect(Collectors.toList());
    }

    private static class Singleton {
        private static final MySQLOrderDao INSTANCE = new MySQLOrderDao();
    }
}
