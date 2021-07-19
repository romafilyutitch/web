package by.epam.jwd.web.dao;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Comment;
import by.epam.jwd.web.model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

public class MySQLCommentDao extends AbstractDao<Comment> implements CommentDao {
    private static final String TABLE_NAME = "Comment";

    private static final String ID_COLUMN = "id";
    private static final String USER_COLUMN = "user_id";
    private static final String BOOK_COLUMN = "book_id";
    private static final String DATE_COLUMN = "date";
    private static final String TEXT_COLUMN = "text";

    private static final List<String> COLUMNS = Arrays.asList(ID_COLUMN, USER_COLUMN, BOOK_COLUMN, DATE_COLUMN, TEXT_COLUMN);

    private final String findByBookId;

    private MySQLCommentDao() {
        super(TABLE_NAME, COLUMNS);
        final StringJoiner joiner = new StringJoiner(",");
        COLUMNS.forEach(joiner::add);
        this.findByBookId = String.format(FIND_BY_COLUMN_SQL_TEMPLATE, joiner, TABLE_NAME, BOOK_COLUMN);
    }

    public static MySQLCommentDao getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    protected Comment mapResultSet(ResultSet result) throws SQLException {
        final long id = result.getLong(ID_COLUMN);
        final long userId = result.getLong(USER_COLUMN);
        final long bookId = result.getLong(BOOK_COLUMN);
        final LocalDate commentDate = result.getObject(DATE_COLUMN, LocalDate.class);
        final String commentText = result.getString(TEXT_COLUMN);
        final User user = new User(userId);
        final Book book = new Book(bookId);
        return new Comment(id, user, book, commentDate, commentText);
    }

    @Override
    protected void setSavePrepareStatementValues(Comment entity, PreparedStatement savePreparedStatement) throws SQLException {
        savePreparedStatement.setLong(1, entity.getUser().getId());
        savePreparedStatement.setLong(2, entity.getBook().getId());
        savePreparedStatement.setObject(3, entity.getDate());
        savePreparedStatement.setString(4, entity.getText());
    }

    @Override
    protected void setUpdatePreparedStatementValues(Comment entity, PreparedStatement updatePreparedStatement) throws SQLException {
        updatePreparedStatement.setLong(1, entity.getUser().getId());
        updatePreparedStatement.setLong(2, entity.getBook().getId());
        updatePreparedStatement.setObject(3, entity.getDate());
        updatePreparedStatement.setString(4, entity.getText());
        updatePreparedStatement.setLong(5, entity.getId());
    }

    @Override
    public List<Comment> findByBookId(Long bookId) {
        return findPreparedEntities(findByBookId, preparedStatement -> preparedStatement.setLong(1, bookId));
    }

    private static class Singleton {
        private static final MySQLCommentDao INSTANCE = new MySQLCommentDao();
    }
}
