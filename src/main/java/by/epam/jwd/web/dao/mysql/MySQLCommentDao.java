package by.epam.jwd.web.dao.mysql;

import by.epam.jwd.web.dao.api.AbstractDao;
import by.epam.jwd.web.dao.api.CommentDao;
import by.epam.jwd.web.model.Author;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Comment;
import by.epam.jwd.web.model.Genre;
import by.epam.jwd.web.model.Subscription;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.model.UserRole;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * {@link AbstractDao} abstract class implementation for {@link Comment} database entity. Links to database
 * comment table and performs sql operations with that table.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class MySQLCommentDao extends AbstractDao<Comment> implements CommentDao {
    private static final String TABLE_NAME = "comment";

    private static final String FIND_ALL_SQL = "select comment.id, comment.date, comment.text,\n" +
            "       user.id, user.login, user.password, role.name, subscription.id, subscription.start_date, subscription.end_date,\n" +
            "       book.id, book.name, author.id, author.name, genre.name, book.date, book.pages_amount, book.copies_amount, book.text, book.likes_amount, book.comments_amount from comment " +
            "inner join user on comment.user_id = user.id inner join book on comment.book_id = book.id inner join author on book.author_id = author.id inner join genre on book.genre_id = genre.id\n" +
            "inner join role on user.role_id = role.id left join subscription on user.subscription_id = subscription.id";
    private static final String SAVE_SQL = "insert into comment (user_id, book_id, date, text) values (?, ?, ?, ?)";
    private static final String UPDATE_SQL = "update comment set user_id = ?, book_id = ?, date = ?, text = ? where id = ?";
    private static final String DELETE_SQL = "delete from comment where id = ?";
    private static final String FIND_BY_BOOK_ID_SQL = String.format("%s where book.id = ?", FIND_ALL_SQL);

    private static final String COMMENT_ID_COLUMN = "comment.id";
    private static final String COMMENT_DATE_COLUMN = "comment.date";
    private static final String COMMENT_TEXT_COLUMN = "comment.text";
    private static final String USER_ID_COLUMN = "user.id";
    private static final String USER_LOGIN_COLUMN = "user.login";
    private static final String USER_PASSWORD_COLUMN = "user.password";
    private static final String USER_ROLE_COLUMN = "role.name";
    private static final String BOOK_ID_COLUMN = "book.id";
    private static final String BOOK_NAME_COLUMN = "book.name";
    private static final String BOOK_GENRE_COLUMN = "genre.name";
    private static final String BOOK_DATE_COLUMN = "book.date";
    private static final String BOOK_PAGES_AMOUNT_COLUMN = "book.pages_amount";
    private static final String BOOK_COPIES_AMOUNT_COLUMN = "book.copies_amount";
    private static final String BOOK_TEXT_COLUMN = "book.text";
    private static final String BOOK_LIKES_AMOUNT_COLUMN = "book.likes_amount";
    private static final String BOOK_COMMENTS_AMOUNT_COLUMN = "book.comments_amount";
    private static final String AUTHOR_ID_COLUMN = "author.id";
    private static final String AUTHOR_NAME_COLUMN = "author.name";
    private static final String SUBSCRIPTION_ID_COLUMN = "subscription.id";
    private static final String SUBSCRIPTION_START_DATE_COLUMN = "subscription.start_date";
    private static final String SUBSCRIPTION_END_DATE_COLUMN = "subscription.end_date";

    private MySQLCommentDao() {
        super(TABLE_NAME, FIND_ALL_SQL, SAVE_SQL, UPDATE_SQL, DELETE_SQL);
    }

    /**
     * Returns singleton from nested class that encapsulates single class instance.
     * @return class instance.
     */
    public static MySQLCommentDao getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Maps result from find sql statement to {@link Comment} instance and returns it.
     * Template method implementation for {@link Comment} database entity.
     * @param resultSet Made during sql find statement execution result.
     * @return {@link Comment} instance.
     * @throws SQLException when exception in database occurs.
     */
    @Override
    protected Comment mapResultSet(ResultSet resultSet) throws SQLException {
        final long id = resultSet.getLong(COMMENT_ID_COLUMN);
        final LocalDate commentDate = resultSet.getObject(COMMENT_DATE_COLUMN, LocalDate.class);
        final String commentText = resultSet.getString(COMMENT_TEXT_COLUMN);
        final User user = buildUser(resultSet);
        final Book book = buildBook(resultSet);
        return new Comment(id, user, book, commentDate, commentText);
    }

    private User buildUser(ResultSet resultSet) throws SQLException {
        final long userId = resultSet.getLong(USER_ID_COLUMN);
        final String userLogin = resultSet.getString(USER_LOGIN_COLUMN);
        final String userPassword = resultSet.getString(USER_PASSWORD_COLUMN);
        final String userRoleName = resultSet.getString(USER_ROLE_COLUMN);
        final Subscription foundSubscription = buildSubscription(resultSet);
        final UserRole userRole = UserRole.valueOf(userRoleName.toUpperCase());
        return new User(userId, userLogin, userPassword, userRole, foundSubscription);
    }

    private Book buildBook(ResultSet resultSet) throws SQLException {
        final long bookId = resultSet.getLong(BOOK_ID_COLUMN);
        final String bookName = resultSet.getString(BOOK_NAME_COLUMN);
        final String genreName = resultSet.getString(BOOK_GENRE_COLUMN);
        final LocalDate bookDate = resultSet.getObject(BOOK_DATE_COLUMN, LocalDate.class);
        final int pageAmount = resultSet.getInt(BOOK_PAGES_AMOUNT_COLUMN);
        final int copiesAmount = resultSet.getInt(BOOK_COPIES_AMOUNT_COLUMN);
        final String text = resultSet.getString(BOOK_TEXT_COLUMN);
        final int likesAmount = resultSet.getInt(BOOK_LIKES_AMOUNT_COLUMN);
        final int commentsAmount = resultSet.getInt(BOOK_COMMENTS_AMOUNT_COLUMN);
        final Author foundAuthor = buildAuthor(resultSet);
        final Genre genre = Genre.valueOf(genreName.toUpperCase());
        return new Book(bookId, bookName, foundAuthor, genre, bookDate, pageAmount, copiesAmount, text, likesAmount, commentsAmount);
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

    /**
     * Set {@link Comment} instance data to prepared statement to execute save statement.
     * Template method implementation for {@link Comment} database entity.
     * @param entity entity that need to save.
     * @param savePreparedStatement Made save entity prepared statement.
     * @throws SQLException when exception in database occurs.
     */
    @Override
    protected void setSavePrepareStatementValues(Comment entity, PreparedStatement savePreparedStatement) throws SQLException {
        savePreparedStatement.setLong(1, entity.getUser().getId());
        savePreparedStatement.setLong(2, entity.getBook().getId());
        savePreparedStatement.setObject(3, entity.getDate());
        savePreparedStatement.setString(4, entity.getText());
    }

    /**
     * Set {@link Comment} instance data to prepared statement to execute update statement.
     * Template method implementation for {@link Comment} database entity.
     * @param entity entity that need to update.
     * @param updatePreparedStatement Made update entity prepared statement
     * @throws SQLException when database exception occurs
     */
    @Override
    protected void setUpdatePreparedStatementValues(Comment entity, PreparedStatement updatePreparedStatement) throws SQLException {
        updatePreparedStatement.setLong(1, entity.getUser().getId());
        updatePreparedStatement.setLong(2, entity.getBook().getId());
        updatePreparedStatement.setObject(3, entity.getDate());
        updatePreparedStatement.setString(4, entity.getText());
        updatePreparedStatement.setLong(5, entity.getId());
    }

    /**
     * Finds and returns result of find {@link Comment} instances by specified {@link Book} instance.
     * Returns passed book comments.
     * @param book passed book that has comments.
     * @return passed book comments.
     */
    @Override
    public List<Comment> findByBook(Book book) {
        return findPreparedEntities(FIND_BY_BOOK_ID_SQL, preparedStatement -> preparedStatement.setLong(1, book.getId()));
    }

    /**
     * Nested class that encapsulates single {@link MySQLCommentDao} instance.
     * Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final MySQLCommentDao INSTANCE = new MySQLCommentDao();
    }
}
