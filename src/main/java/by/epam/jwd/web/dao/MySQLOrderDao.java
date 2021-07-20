package by.epam.jwd.web.dao;


import by.epam.jwd.web.exception.DAOException;
import by.epam.jwd.web.model.Author;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;
import by.epam.jwd.web.model.Order;
import by.epam.jwd.web.model.Status;
import by.epam.jwd.web.model.Subscription;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.model.UserRole;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class MySQLOrderDao extends AbstractDao<Order> implements OrderDao {
    private static final String TABLE_NAME = "book_order";

    private final static String FIND_ALL_SQL = "select book_order.id, book_order.date, status.name,\n" +
            "       user.id, user.login, user.password, role.name, subscription.id, subscription.start_date, subscription.end_date,\n" +
            "       book.id, book.name, author.id, author.name, genre.name, book.date, book.pages_amount, book.copies_amount, book.description, book.likes from book_order\n" +
            "inner join user on book_order.user_id = user.id inner join book on book_order.book_id = book.id inner join author on book.author_id = author.id inner join  genre on book.genre_id = genre.id\n" +
            "inner join role on user.role_id = role.id inner join status on book_order.status = status.id left join subscription on user.subscription_id = subscription.id\n";
    private final static String SAVE_SQL = "insert into book_order (user_id, book_id, date, status) values (?, ?, ?, ?)";
    private final static String UPDATE_SQL = "update book_order set user_id = ?, book_id = ?, date = ?, status = ? where id = ?";
    private final static String DELETE_SQL = "delete from book_order where id = ?";

    private static final String FIND_BY_BOOK_ID_TEMPLATE = "%s where book.id = ?";
    private static final String FIND_BY_DATE_TEMPLATE = "%s where book_order.date = ?";
    private static final String FIND_BY_USER_ID_TEMPLATE = "%s where user.id = ?";
    private final static String FIND_BY_BOOK_ID_SQL = String.format(FIND_BY_BOOK_ID_TEMPLATE, FIND_ALL_SQL);
    private final static String FIND_BY_DATE_SQL = String.format(FIND_BY_DATE_TEMPLATE, FIND_ALL_SQL);
    private final static String FIND_BY_USER_ID_SQL = String.format(FIND_BY_USER_ID_TEMPLATE, FIND_ALL_SQL);

    private static final String ID_COLUMN = "book_order.id";
    private static final String DATE_COLUMN = "book_order.date";
    private static final String USER_ID_COLUMN = "user.id";
    private static final String USER_LOGIN_COLUMN = "user.login";
    private static final String USER_PASSWORD_COLUMN = "user.password";
    private static final String ROLE_NAME_COLUMN = "role.name";
    private static final String BOOK_ID_COLUMN = "book.id";
    private static final String BOOK_NAME_COLUMN = "book.name";
    private static final String GENRE_NAME_COLUMN = "genre.name";
    private static final String BOOK_DATE_COLUMN = "book.date";
    private static final String BOOK_PAGES_AMOUNT_COLUMN = "book.pages_amount";
    private static final String BOOK_COPIES_AMOUNT_COLUMN = "book.copies_amount";
    private static final String BOOK_DESCRIPTION_COLUMN = "book.description";
    private static final String BOOK_LIKES_COLUMN = "book.likes";
    private static final String AUTHOR_ID_COLUMN = "author.id";
    private static final String AUTHOR_NAME_COLUMN = "author.name";
    private static final String SUBSCRIPTION_ID_COLUMN = "subscription.id";
    private static final String SUBSCRIPTION_START_DATE_COLUMN = "subscription.start_date";
    private static final String SUBSCRIPTION_END_DATE_COLUMN = "subscription.end_date";
    private static final String STATUS_NAME_COLUMN = "status.name";

    private MySQLOrderDao() {
        super(TABLE_NAME, FIND_ALL_SQL, SAVE_SQL, UPDATE_SQL, DELETE_SQL);
    }

    public static MySQLOrderDao getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    protected Order mapResultSet(ResultSet result) throws SQLException {
        final long id = result.getLong(ID_COLUMN);
        final String statusName = result.getString(STATUS_NAME_COLUMN);
        final LocalDate orderDate = result.getObject(DATE_COLUMN, LocalDate.class);
        final User user = buildUser(result);
        final Book book = buildBook(result);
        final Status status = Status.valueOf(statusName.toUpperCase());
        return new Order(id, user, book, orderDate, status);
    }

    private User buildUser(ResultSet resultSet) throws SQLException {
        final long userId = resultSet.getLong(USER_ID_COLUMN);
        final String userLogin = resultSet.getString(USER_LOGIN_COLUMN);
        final String userPassword = resultSet.getString(USER_PASSWORD_COLUMN);
        final String roleName = resultSet.getString(ROLE_NAME_COLUMN);
        final Subscription subscription = buildSubscription(resultSet);
        final UserRole role = UserRole.valueOf(roleName.toUpperCase());
        return new User(userId, userLogin, userPassword, role, subscription);
    }

    private Book buildBook(ResultSet resultSet) throws SQLException {
        final long bookId = resultSet.getLong(BOOK_ID_COLUMN);
        final String bookName = resultSet.getString(BOOK_NAME_COLUMN);
        final String genreName = resultSet.getString(GENRE_NAME_COLUMN);
        final LocalDate bookDate = resultSet.getObject(BOOK_DATE_COLUMN, LocalDate.class);
        final int pagesAmount = resultSet.getInt(BOOK_PAGES_AMOUNT_COLUMN);
        final int copiesAmount = resultSet.getInt(BOOK_COPIES_AMOUNT_COLUMN);
        final String description = resultSet.getString(BOOK_DESCRIPTION_COLUMN);
        final int likes = resultSet.getInt(BOOK_LIKES_COLUMN);
        final Author author = buildAuthor(resultSet);
        final Genre genre = Genre.valueOf(genreName.toUpperCase());
        return new Book(bookId, bookName, author, genre, bookDate, pagesAmount, copiesAmount, description, likes);
    }

    private Author buildAuthor(ResultSet resultSet) throws SQLException {
        final long authorId = resultSet.getLong(AUTHOR_ID_COLUMN);
        final String authorName = resultSet.getString(AUTHOR_NAME_COLUMN);
        return new Author(authorId, authorName);
    }

    private Subscription buildSubscription(ResultSet resultSet) throws SQLException {
        final long subscriptionId = resultSet.getLong(SUBSCRIPTION_ID_COLUMN);
        if (subscriptionId == 0) {
            return null;
        } else {
            final LocalDate startDate = resultSet.getObject(SUBSCRIPTION_START_DATE_COLUMN, LocalDate.class);
            final LocalDate endDate = resultSet.getObject(SUBSCRIPTION_END_DATE_COLUMN, LocalDate.class);
            return new Subscription(subscriptionId, startDate, endDate);
        }
    }

    @Override
    protected void setSavePrepareStatementValues(Order entity, PreparedStatement savePreparedStatement) throws SQLException, DAOException {
        savePreparedStatement.setLong(1, entity.getUser().getId());
        savePreparedStatement.setLong(2, entity.getBook().getId());
        savePreparedStatement.setObject(3, entity.getOrderDate());
        savePreparedStatement.setLong(4, entity.getStatus().getId());
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
    public List<Order> findByBook(Book book) throws DAOException {
        return findPreparedEntities(FIND_BY_BOOK_ID_SQL, preparedStatement -> preparedStatement.setLong(1, book.getId()));
    }

    @Override
    public List<Order> findByOrderDate(LocalDate orderDate) throws DAOException {
        return findPreparedEntities(FIND_BY_DATE_SQL, preparedStatement -> preparedStatement.setObject(1, orderDate));
    }

    @Override
    public List<Order> findByUser(User user) {
        return findPreparedEntities((FIND_BY_USER_ID_SQL), preparedStatement -> preparedStatement.setLong(1, user.getId()));
    }

    private static class Singleton {
        private static final MySQLOrderDao INSTANCE = new MySQLOrderDao();
    }
}
