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
import java.util.List;
import java.util.Optional;

public class MySQLBookDao extends AbstractDao<Book> implements BookDao {
    private static final String TABLE_NAME = "book";

    private static final String FIND_ALL_SQL = "select book.id, book.name, author.id, author.name, genre.id, genre.name, book.date, book.pages_amount, book.copies_amount, book.description, book.likes from book inner join author on book.author_id = author.id inner join genre on book.genre_id = genre.id";
    private static final String SAVE_SQL = "insert into book (name, author_id, genre_id, date, pages_amount, copies_amount, description, likes) values (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "update book set name = ?, author_id = ?, genre_id = ?, date = ?, pages_amount = ?, copies_amount = ?, description = ?, likes = ? where id = ?";
    private static final String DELETE_SQL = "delete from book where id = ?";

    private static final String FIND_BY_NAME_TEMPLATE = "%s where book.name = ?";
    private static final String FIND_BY_AUTHOR_TEMPLATE = "%s where author.id = ?";
    private static final String FIND_BY_GENRE_TEMPLATE = "%s where genre.id = ?";
    private static final String FIND_BY_NAME_SQL = String.format(FIND_BY_NAME_TEMPLATE, FIND_ALL_SQL);
    private static final String FIND_BY_AUTHOR_SQL = String.format(FIND_BY_AUTHOR_TEMPLATE, FIND_ALL_SQL);
    private static final String FIND_BY_GENRE_SQL = String.format(FIND_BY_GENRE_TEMPLATE, FIND_ALL_SQL);

    private static final String ID_COLUMN = "book.id";
    private static final String NAME_COLUMN = "book.name";
    private static final String AUTHOR_ID_COLUMN = "author.id";
    private static final String AUTHOR_NAME_COLUMN = "author.name";
    private static final String GENRE_NAME_COLUMN = "genre.name";
    private static final String DATE_COLUMN = "book.date";
    private static final String PAGES_AMOUNT_COLUMN = "book.pages_amount";
    private static final String COPIES_AMOUNT_COLUMN = "book.copies_amount";
    private static final String DESCRIPTION_COLUMN = "book.description";
    private static final String LIKES_COLUMN = "book.likes";

    private static final String ADD_LIKE_SQL = "insert into book_like (user_id, book_id) values (?, ?)";
    private static final String DELETE_LIKE_SQL = "delete from book_like where book_id = ? and user_id = ?";
    private static final String COUNT_LIKES_SQL = "select count(*) from book_like where user_id = ? and book_id = ?";
    private static final String LIKES_COUNT_COLUMN = "count(*)";

    private MySQLBookDao() {
        super(TABLE_NAME, FIND_ALL_SQL, SAVE_SQL, UPDATE_SQL, DELETE_SQL);
    }

    public static MySQLBookDao getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    protected Book mapResultSet(ResultSet result) throws SQLException, DAOException {
        final long id = result.getLong(ID_COLUMN);
        final String name = result.getString(NAME_COLUMN);
        final long authorId = result.getLong(AUTHOR_ID_COLUMN);
        final String authorName = result.getString(AUTHOR_NAME_COLUMN);
        final String genreName = result.getString(GENRE_NAME_COLUMN);
        final LocalDate date = result.getObject(DATE_COLUMN, LocalDate.class);
        final int pagesAmount = result.getInt(PAGES_AMOUNT_COLUMN);
        final int copiesAmount = result.getInt(COPIES_AMOUNT_COLUMN);
        final String description = result.getString(DESCRIPTION_COLUMN);
        final Integer likes = result.getInt(LIKES_COLUMN);
        final Author foundAuthor = new Author(authorId, authorName);
        final Genre foundGenre = Genre.valueOf(genreName.toUpperCase());
        return new Book(id, name, foundAuthor, foundGenre, date, pagesAmount, copiesAmount, description, likes);
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
        final List<Book> foundBooks = findPreparedEntities(FIND_BY_NAME_SQL, preparedStatement -> preparedStatement.setString(1, name));
        return foundBooks.stream().findAny();
    }

    @Override
    public List<Book> findByAuthorId(Long authorId) throws DAOException {
        return findPreparedEntities((FIND_BY_AUTHOR_SQL), preparedStatement -> preparedStatement.setLong(1, authorId));
    }

    @Override
    public List<Book> findByGenreId(Long genreId) throws DAOException {
        return findPreparedEntities(FIND_BY_GENRE_SQL, preparedStatement -> preparedStatement.setLong(1, genreId));
    }

    @Override
    public void addLike(Long bookId, Long userId) {
        try (Connection connection = ConnectionPool.getConnectionPool().takeFreeConnection();
             PreparedStatement addLikeStatement = connection.prepareStatement(ADD_LIKE_SQL)) {
            addLikeStatement.setLong(1, userId);
            addLikeStatement.setLong(2, bookId);
            addLikeStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
     }

    @Override
    public void removeLike(Long bookId, Long userId) {
        try (Connection connection = ConnectionPool.getConnectionPool().takeFreeConnection();
        PreparedStatement deleteStatement = connection.prepareStatement(DELETE_LIKE_SQL)) {
            deleteStatement.setLong(1, bookId);
            deleteStatement.setLong(2, userId);
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    @Override
    public boolean isLikedByUserWithId(Long bookId, Long userId) {
        try (Connection connection = ConnectionPool.getConnectionPool().takeFreeConnection();
        PreparedStatement findLikeStatement = connection.prepareStatement(COUNT_LIKES_SQL)) {
            findLikeStatement.setLong(1, userId);
            findLikeStatement.setLong(2, bookId);
            try (ResultSet findResult = findLikeStatement.executeQuery()) {
                findResult.next();
                final int likesAmount = findResult.getInt(LIKES_COUNT_COLUMN);
                return likesAmount != 0;
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    private static class Singleton {
        private static final MySQLBookDao INSTANCE = new MySQLBookDao();
    }
}
