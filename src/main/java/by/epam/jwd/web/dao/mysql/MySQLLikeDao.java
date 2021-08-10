package by.epam.jwd.web.dao.mysql;

import by.epam.jwd.web.dao.api.AbstractDao;
import by.epam.jwd.web.dao.api.LikeDao;
import by.epam.jwd.web.model.Author;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;
import by.epam.jwd.web.model.Like;
import by.epam.jwd.web.model.Subscription;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.model.UserRole;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * {@link AbstractDao} implementation for {@link Like} database entity. Links to like database table
 * and performs sql operations with that table.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class MySQLLikeDao extends AbstractDao<Like> implements LikeDao {
    private static final String TABLE_NAME = "book_like";

    private static final String FIND_ALL_SQL = "select book_like.id, " +
            "user.id, user.login, user.password, role.name, subscription.id, subscription.start_date, subscription.end_date, " +
            "book.id, book.name, author.id, author.name, genre.name, book.date, book.pages_amount, book.copies_amount, book.text, book.likes_amount, book.comments_amount " +
            "from book_like inner join user on book_like.user_id = user.id inner join role on user.role_id = role.id  inner join book on book_like.book_id = book.id " +
            "inner join genre on book.genre_id = genre.id inner join author on book.author_id = author.id left join subscription on user.subscription_id = subscription.id";
    private static final String SAVE_SQL = "insert into book_like (user_id, book_id) values (?, ?)";
    private static final String UPDATE_SQL = "update book_like set user_id = ?, book_id = ? where id = ?";
    private static final String DELETE_SQL = "delete from book_like where id = ?";

    private static final String FIND_BY_BOOK_AND_USER_TEMPLATE = "%s where book.id = ? and user.id = ?";
    private static final String FIND_BY_BOOK_AND_USER_SQL = String.format(FIND_BY_BOOK_AND_USER_TEMPLATE, FIND_ALL_SQL);

    private static final String BOOK_LIKE_ID_COLUMN = "book_like.id";
    private static final String BOOK_ID_COLUMN = "book.id";
    private static final String BOOK_NAME_COLUMN = "book.name";
    private static final String GENRE_NAME_COLUMN = "genre.name";
    private static final String BOOK_DATE_COLUMN = "book.date";
    private static final String BOOK_PAGES_AMOUNT_COLUMN = "book.pages_amount";
    private static final String BOOK_COPIES_AMOUNT_COLUMN = "book.copies_amount";
    private static final String BOOK_TEXT_COLUMN = "book.text";
    private static final String BOOK_LIKES_AMOUNT_COLUMN = "book.likes_amount";
    private static final String BOOK_COMMENTS_AMOUNT_COLUMN = "book.comments_amount";
    private static final String AUTHOR_ID_COLUMN = "author.id";
    private static final String AUTHOR_NAME_COLUMN = "author.name";
    private static final String USER_ID_COLUMN = "user.id";
    private static final String USER_LOGIN_COLUMN = "user.login";
    private static final String USER_PASSWORD_COLUMN = "user.password";
    private static final String ROLE_NAME_COLUMN = "role.name";
    private static final String SUBSCRIPTION_ID_COLUMN = "subscription.id";
    private static final String SUBSCRIPTION_START_DATE_COLUMN = "subscription.start_date";
    private static final String SUBSCRIPTION_END_DATE_COLUMN = "subscription.end_date";

    private MySQLLikeDao() {
        super(TABLE_NAME, FIND_ALL_SQL, SAVE_SQL, UPDATE_SQL, DELETE_SQL, BOOK_LIKE_ID_COLUMN);
    }

    /**
     * Returns class instance from nested class that encapsulates single {@link MySQLLikeDao} instance.
     * @return class instance.
     */
    public static MySQLLikeDao getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Maps find result set to {@link Like} instance.
     * Template method implementation for {@link Like} database entity.
     * @param result Made during sql find statement execution result.
     * @return Mapped {@link Like} instance.
     * @throws SQLException when database exception occurs.
     */
    @Override
    protected Like mapResultSet(ResultSet result) throws SQLException {
        final long id = result.getLong(BOOK_LIKE_ID_COLUMN);
        final User user = buildUser(result);
        final Book book = buildBook(result);
        return new Like(id, user, book);
    }

    private Book buildBook(ResultSet resultSet) throws SQLException {
        final long bookId = resultSet.getLong(BOOK_ID_COLUMN);
        final String bookName = resultSet.getString(BOOK_NAME_COLUMN);
        final String genreName = resultSet.getString(GENRE_NAME_COLUMN);
        final LocalDate bookDate = resultSet.getObject(BOOK_DATE_COLUMN, LocalDate.class);
        final int pagesAmount = resultSet.getInt(BOOK_PAGES_AMOUNT_COLUMN);
        final int copiesAmount = resultSet.getInt(BOOK_COPIES_AMOUNT_COLUMN);
        final String text = resultSet.getString(BOOK_TEXT_COLUMN);
        final int likesAmount = resultSet.getInt(BOOK_LIKES_AMOUNT_COLUMN);
        final int commentsAmount = resultSet.getInt(BOOK_COMMENTS_AMOUNT_COLUMN);
        final Genre genre = Genre.valueOf(genreName.toUpperCase());
        final Author author = buildAuthor(resultSet);
        return new Book(bookId, bookName, author, genre, bookDate, pagesAmount, copiesAmount, text, likesAmount, commentsAmount);
    }

    private Author buildAuthor(ResultSet resultSet) throws SQLException {
        final long authorId = resultSet.getLong(AUTHOR_ID_COLUMN);
        final String authorName = resultSet.getString(AUTHOR_NAME_COLUMN);
        return new Author(authorId, authorName);
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

    /**
     * Set {@link Like} instance data to execute save prepared statement.
     * Template method for {@link Like} database entity.
     * @param entity entity that need to save
     * @param savePreparedStatement Made save entity prepared statement
     * @throws SQLException when database exception occurs
     */
    @Override
    protected void setSavePrepareStatementValues(Like entity, PreparedStatement savePreparedStatement) throws SQLException {
        savePreparedStatement.setLong(1, entity.getUser().getId());
        savePreparedStatement.setLong(2, entity.getBook().getId());
    }

    /**
     * Set {@link Like} instance data to execute update prepared statement.
     * Template method for {@link Like} database entity.
     * @param entity entity that need to update.
     * @param updatePreparedStatement Made update entity prepared statement
     * @throws SQLException when database exception occurs.
     */
    @Override
    protected void setUpdatePreparedStatementValues(Like entity, PreparedStatement updatePreparedStatement) throws SQLException {
        updatePreparedStatement.setLong(1, entity.getUser().getId());
        updatePreparedStatement.setLong(2, entity.getBook().getId());
        updatePreparedStatement.setLong(3, entity.getId());
    }

    /**
     * Finds and return result of find {@link Like} instance by saved {@link User} and {@link Book} instances.
     * Returns found like in optional when there is like to passed book by passed user
     * or empty optional otherwise.
     * @param user {@link User} instance that added like to {@link Book} instance.
     * @param book {@link Book} instance that may have like by {@link User} instance.
     * @return found like in optional if it presents or empty optional otherwise.
     */
    @Override
    public Optional<Like> findByUserAndBook(User user, Book book) {
        final List<Like> foundLikes = findPreparedEntities(FIND_BY_BOOK_AND_USER_SQL, preparedConsumer -> {
            preparedConsumer.setLong(1, book.getId());
            preparedConsumer.setLong(2, user.getId());
        });
        return foundLikes.stream().findAny();
    }

    /**
     * Nested class that encapsulates single {@link MySQLLikeDao} instance.
     * Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final MySQLLikeDao INSTANCE = new MySQLLikeDao();
    }
}
