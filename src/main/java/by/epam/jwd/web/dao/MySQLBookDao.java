package by.epam.jwd.web.dao;


import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.exception.DAOException;
import by.epam.jwd.web.model.Author;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

public class MySQLBookDao extends AbstractDao<Book> implements BookDao {
    private static final String TABLE_NAME = "book";
    private static final String ID_COLUMN = "id";
    private static final String NAME_COLUMN = "name";
    private static final String AUTHOR_COLUMN = "author";
    private static final String GENRE_COLUMN = "genre";
    private static final String DATE_COLUMN = "date";
    private static final String PAGES_AMOUNT_COLUMN = "pages_amount";
    private static final String COPIES_AMOUNT_COLUMN = "copies_amount";
    private static final String DESCRIPTION_COLUMN = "description";
    private static final String LIKES_COLUMN = "likes";

    private static final List<String> COLUMNS = Arrays.asList(ID_COLUMN, NAME_COLUMN, AUTHOR_COLUMN, GENRE_COLUMN, DATE_COLUMN, PAGES_AMOUNT_COLUMN, COPIES_AMOUNT_COLUMN, DESCRIPTION_COLUMN, LIKES_COLUMN);

    private final String findByNameSql;
    private final String findByAuthorSql;
    private final String findByGenreSql;

    private MySQLBookDao() {
        super(TABLE_NAME, COLUMNS);
        final StringJoiner joiner = new StringJoiner(",");
        COLUMNS.forEach(joiner::add);
        findByNameSql = String.format(FIND_BY_COLUMN_SQL_TEMPLATE, joiner, TABLE_NAME, NAME_COLUMN);
        findByAuthorSql = String.format(FIND_BY_COLUMN_SQL_TEMPLATE, joiner, TABLE_NAME, AUTHOR_COLUMN);
        findByGenreSql = String.format(FIND_BY_COLUMN_SQL_TEMPLATE, joiner, TABLE_NAME, GENRE_COLUMN);
    }

    public static MySQLBookDao getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    protected Book mapResultSet(ResultSet result) throws SQLException, DAOException {
        final long id = result.getLong(ID_COLUMN);
        final String name = result.getString(NAME_COLUMN);
        final long authorId = result.getLong(AUTHOR_COLUMN);
        final Genre genre = Genre.getInstance(result.getLong(GENRE_COLUMN));
        final LocalDate date = result.getObject(DATE_COLUMN, LocalDate.class);
        final int pagesAmount = result.getInt(PAGES_AMOUNT_COLUMN);
        final int copiesAmount = result.getInt(COPIES_AMOUNT_COLUMN);
        final String description = result.getString(DESCRIPTION_COLUMN);
        final Integer likes = result.getInt(LIKES_COLUMN);
        return new Book(id, name, new Author(authorId), genre, date, pagesAmount, copiesAmount, description, new ArrayList<>(), likes);
    }

    @Override
    protected void setSavePrepareStatementValues(Book entity, PreparedStatement savePreparedStatement) throws SQLException, DAOException {
        savePreparedStatement.setString(1, entity.getName());
        savePreparedStatement.setLong(2, entity.getAuthor().getId());
        savePreparedStatement.setLong(3, entity.getGenre().getId());
        savePreparedStatement.setObject(4, entity.getDate());
        savePreparedStatement.setInt(5, entity.getPagesAmount());
        savePreparedStatement.setInt(6, entity.getCopiesAmount());
        savePreparedStatement.setString(7, entity.getDescription());
        savePreparedStatement.setInt(8, entity.getLikes());
    }

    @Override
    protected void setUpdatePreparedStatementValues(Book entity, PreparedStatement updatePreparedStatement) throws SQLException {
        updatePreparedStatement.setString(1, entity.getName());
        updatePreparedStatement.setLong(2, entity.getAuthor().getId());
        updatePreparedStatement.setLong(3, entity.getGenre().getId());
        updatePreparedStatement.setObject(4, entity.getDate());
        updatePreparedStatement.setInt(5, entity.getPagesAmount());
        updatePreparedStatement.setInt(6, entity.getCopiesAmount());
        updatePreparedStatement.setString(7, entity.getDescription());
        updatePreparedStatement.setInt(8, entity.getLikes());
        updatePreparedStatement.setLong(9, entity.getId());
    }

    @Override
    public Optional<Book> findByName(String name) throws DAOException {
        final List<Book> foundBooks = findPreparedEntities(findByNameSql, preparedStatement -> preparedStatement.setString(1, name));
        return foundBooks.stream().findAny();
    }

    @Override
    public List<Book> findByAuthorId(Long authorId) throws DAOException {
        return findPreparedEntities(findByAuthorSql, preparedStatement -> preparedStatement.setLong(1, authorId));
    }

    @Override
    public List<Book> findByGenreId(Long genreId) throws DAOException {
        return findPreparedEntities(findByGenreSql, preparedStatement -> preparedStatement.setLong(1, genreId));
    }

    @Override
    public void addLike(Long bookId, Long userId) {
        try (Connection connection = ConnectionPool.getConnectionPool().takeFreeConnection();
             PreparedStatement addLikeStatement = connection.prepareStatement("insert into book_like (user_id, book_id) values (?, ?)")) {
            addLikeStatement.setLong(1, userId);
            addLikeStatement.setLong(2, bookId);
            addLikeStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Could not insert like", e);
        }
     }

    @Override
    public void removeLike(Long bookId, Long userId) {
        try (Connection connection = ConnectionPool.getConnectionPool().takeFreeConnection();
        PreparedStatement deleteStatement = connection.prepareStatement("delete from book_like where book_id = ? and user_id = ?")) {
            deleteStatement.setLong(1, bookId);
            deleteStatement.setLong(2, userId);
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException("Could not delete like", e);
        }
    }

    @Override
    public boolean isLikedByUserWithId(Long bookId, Long userId) {
        try (Connection connection = ConnectionPool.getConnectionPool().takeFreeConnection();
        PreparedStatement findLikeStatement = connection.prepareStatement("select count(*) amount from book_like where user_id = ? and book_id = ?")) {
            findLikeStatement.setLong(1, userId);
            findLikeStatement.setLong(2, bookId);
            try (ResultSet findResult = findLikeStatement.executeQuery()) {
                findResult.next();
                final int likesAmount = findResult.getInt("amount");
                return likesAmount != 0;
            }
        } catch (SQLException e) {
            throw new DAOException("Could not find like", e);
        }
    }

    private static class Singleton {
        private static final MySQLBookDao INSTANCE = new MySQLBookDao();
    }
}
