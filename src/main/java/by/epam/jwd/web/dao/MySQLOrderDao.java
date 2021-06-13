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
    private static final MySQLBookDao BOOK_DAO_SERVICE = MySQLBookDao.getInstance();
    private static final MySQLUserDao USER_DAO_SERVICE = MySQLUserDao.getInstance();
    private static final MySQLStatusDao STATUS_DAO = MySQLStatusDao.getInstance();

    private static final String ID_COLUMN = "id";
    private static final String READER_COLUMN = "reader";
    private static final String BOOK_COLUMN = "book";
    private static final String ORDER_DATE_COLUMN = "date";
    public static final String STATUS_COLUMN = "status";
    private static final String SAVE_PREPARED_SQL = "insert into `order` (reader, book) values (?, ?)";
    private static final String FIND_ALL_SQL = "select id, reader, book, date, status from `order`";//todo mysql does not see table order table change in future
    private static final String UPDATE_PREPARED_SQL = "update `order` set reader = ?, book = ?, date = ?, status = ? where id = ?";
    private static final String DELETE_PREPARED_SQL = "delete from `order` where id = ?";

    private MySQLOrderDao() {
        super(FIND_ALL_SQL, SAVE_PREPARED_SQL, UPDATE_PREPARED_SQL, DELETE_PREPARED_SQL);
    }

    public static MySQLOrderDao getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    protected BookOrder mapResultSet(ResultSet result) throws SQLException, DAOException {
        final Optional<User> optionalLibraryUser = USER_DAO_SERVICE.findById(result.getLong(READER_COLUMN));
        final Optional<Book> optionalBook = BOOK_DAO_SERVICE.findById(result.getLong(BOOK_COLUMN));
        final LocalDate orderDate = result.getObject(ORDER_DATE_COLUMN, LocalDate.class);
        final Optional<Status> optionalStatus = STATUS_DAO.findById(result.getLong(STATUS_COLUMN));
        return new BookOrder(result.getLong(ID_COLUMN), optionalLibraryUser.orElseThrow(DAOException::new), optionalBook.orElseThrow(DAOException::new), orderDate, optionalStatus.orElseThrow(DAOException::new));
    }

    @Override
    protected void setSavePrepareStatementValues(BookOrder entity, PreparedStatement savePreparedStatement) throws SQLException, DAOException {
        savePreparedStatement.setLong(1, USER_DAO_SERVICE.save(entity.getUser()).getId());
        savePreparedStatement.setLong(2, BOOK_DAO_SERVICE.save(entity.getBook()).getId());

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
