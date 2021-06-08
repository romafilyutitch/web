package by.epam.jwd.web.dao;



import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.BookOrder;
import by.epam.jwd.web.model.User;

import java.sql.Date;
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

    private static final String ID_COLUMN = "id";
    private static final String READER_COLUMN = "reader";
    private static final String BOOK_COLUMN = "book";
    private static final String ORDER_DATE_COLUMN = "order_date";
    private static final String SAVE_PREPARED_SQL = "insert into book_order (reader, book, order_date) values (?, ?, ?)";
    private static final String FIND_ALL_SQL = "select id, reader, book, order_date from book_order";
    private static final String UPDATE_PREPARED_SQL = "update book_order set reader = ?, book = ?, order_date = ? where id = ?";
    private static final String DELETE_PREPARED_SQL = "delete from book_order where id = ?";

    private MySQLOrderDao() {
        super(FIND_ALL_SQL, SAVE_PREPARED_SQL, null, DELETE_PREPARED_SQL);
    }

    public static MySQLOrderDao getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    protected BookOrder mapResultSet(ResultSet result) throws SQLException, DAOException {
        final Optional<User> optionalLibraryUser = USER_DAO_SERVICE.findById(result.getLong(READER_COLUMN));
        final Optional<Book> optionalBook = BOOK_DAO_SERVICE.findById(result.getLong(BOOK_COLUMN));
        final LocalDate orderDate = result.getDate(ORDER_DATE_COLUMN).toLocalDate();
        return new BookOrder(result.getLong(ID_COLUMN), optionalLibraryUser.orElseThrow(DAOException::new), optionalBook.orElseThrow(DAOException::new), orderDate);
    }

    @Override
    protected void setSavePrepareStatementValues(BookOrder entity, PreparedStatement savePreparedStatement) throws SQLException, DAOException {
        savePreparedStatement.setLong(1, USER_DAO_SERVICE.save(entity.getUser()).getId());
        savePreparedStatement.setLong(2, BOOK_DAO_SERVICE.save(entity.getBook()).getId());
        savePreparedStatement.setDate(3, Date.valueOf(entity.getOrderDate()));
    }

    @Override
    protected void setUpdatePreparedStatementValues(BookOrder entity, PreparedStatement updatePreparedStatement) throws SQLException, DAOException {
        updatePreparedStatement.setLong(1, entity.getUser().getId());
        updatePreparedStatement.setLong(2, entity.getBook().getId());
        updatePreparedStatement.setDate(3, Date.valueOf(entity.getOrderDate()));
        updatePreparedStatement.setLong(4, entity.getId());
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

    private static class Singleton {
        private static final MySQLOrderDao INSTANCE = new MySQLOrderDao();
    }
}
