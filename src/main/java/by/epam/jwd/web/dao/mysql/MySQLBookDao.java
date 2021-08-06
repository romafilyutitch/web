package by.epam.jwd.web.dao.mysql;


import by.epam.jwd.web.dao.api.AbstractDao;
import by.epam.jwd.web.dao.api.BookDao;
import by.epam.jwd.web.exception.DAOException;
import by.epam.jwd.web.model.Author;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * {@link AbstractDao} abstract class implementation for {@link Book} database entity. Links to book database table
 * and performs sql statements with that table.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class MySQLBookDao extends AbstractDao<Book> implements BookDao {
    private static final String TABLE_NAME = "book";

    private static final String FIND_ALL_SQL = "select book.id, book.name, author.id, author.name, genre.id, genre.name, book.date, book.pages_amount, book.copies_amount, book.text, book.likes_amount, book.comments_amount " +
            "from book inner join author on book.author_id = author.id inner join genre on book.genre_id = genre.id";
    private static final String SAVE_SQL = "insert into book (name, author_id, genre_id, date, pages_amount, copies_amount, text, likes_amount, comments_amount) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "update book set name = ?, author_id = ?, genre_id = ?, date = ?, pages_amount = ?, copies_amount = ?, text = ?, likes_amount = ?, comments_amount = ? where id = ?";
    private static final String DELETE_SQL = "delete from book where id = ?";

    private static final String FIND_BY_NAME_TEMPLATE = "%s where book.name = ?";
    private static final String FIND_BY_AUTHOR_NAME_TEMPLATE = "%s where author.name = ?";
    private static final String FIND_BY_GENRE_TEMPLATE = "%s where genre.id = ?";
    private static final String FIND_BY_NAME_SQL = String.format(FIND_BY_NAME_TEMPLATE, FIND_ALL_SQL);
    private static final String FIND_BY_AUTHOR_NAME_SQL = String.format(FIND_BY_AUTHOR_NAME_TEMPLATE, FIND_ALL_SQL);
    private static final String FIND_BY_GENRE_SQL = String.format(FIND_BY_GENRE_TEMPLATE, FIND_ALL_SQL);

    private static final String BOOK_ID_COLUMN = "book.id";
    private static final String BOOK_NAME_COLUMN = "book.name";
    private static final String AUTHOR_ID_COLUMN = "author.id";
    private static final String AUTHOR_NAME_COLUMN = "author.name";
    private static final String GENRE_NAME_COLUMN = "genre.name";
    private static final String BOOK_DATE_COLUMN = "book.date";
    private static final String BOOK_PAGES_AMOUNT_COLUMN = "book.pages_amount";
    private static final String BOOK_COPIES_AMOUNT_COLUMN = "book.copies_amount";
    private static final String BOOK_TEXT_COLUMN = "book.text";
    private static final String BOOK_LIKES_AMOUNT_COLUMN = "book.likes_amount";
    private static final String BOOK_COMMENTS_AMOUNT_COLUMN = "book.comments_amount";

    private MySQLBookDao() {
        super(TABLE_NAME, FIND_ALL_SQL, SAVE_SQL, UPDATE_SQL, DELETE_SQL);
    }

    /**
     * Returns singleton from nested class that encapsulates single class instance.
     * @return class instance.
     */
    public static MySQLBookDao getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Maps result from find sql statement to {@link Book} instance and returns it.
     * Template method implementation for {@link Book} database entity.
     * @param result Made during sql find statement execution result.
     * @return {@link Book} instance
     * @throws SQLException when exception in database occurs
     */
    @Override
    protected Book mapResultSet(ResultSet result) throws SQLException {
        final long id = result.getLong(BOOK_ID_COLUMN);
        final String name = result.getString(BOOK_NAME_COLUMN);
        final String genreName = result.getString(GENRE_NAME_COLUMN);
        final LocalDate date = result.getObject(BOOK_DATE_COLUMN, LocalDate.class);
        final int pagesAmount = result.getInt(BOOK_PAGES_AMOUNT_COLUMN);
        final int copiesAmount = result.getInt(BOOK_COPIES_AMOUNT_COLUMN);
        final String text = result.getString(BOOK_TEXT_COLUMN);
        final Integer likesAmount = result.getInt(BOOK_LIKES_AMOUNT_COLUMN);
        final Integer commentsAmount = result.getInt(BOOK_COMMENTS_AMOUNT_COLUMN);
        final Author foundAuthor = buildAuthor(result);
        final Genre foundGenre = Genre.valueOf(genreName.toUpperCase());
        return new Book(id, name, foundAuthor, foundGenre, date, pagesAmount, copiesAmount, text, likesAmount, commentsAmount);
    }

    private Author buildAuthor(ResultSet resultSet) throws SQLException {
        final long authorId = resultSet.getLong(AUTHOR_ID_COLUMN);
        final String authorName = resultSet.getString(AUTHOR_NAME_COLUMN);
        return new Author(authorId, authorName);
    }

    /**
     * Set {@link Book} instance data to prepared statement to execute save statement.
     * Template method implementation for {@link Book} database entity.
     * @param entity entity that need to save.
     * @param savePreparedStatement Made save entity prepared statement.
     * @throws SQLException when exception in database occurs.
     */
    @Override
    protected void setSavePrepareStatementValues(Book entity, PreparedStatement savePreparedStatement) throws SQLException {
        savePreparedStatement.setString(1, entity.getName());
        savePreparedStatement.setLong(2, entity.getAuthor().getId());
        savePreparedStatement.setLong(3, entity.getGenre().getId());
        savePreparedStatement.setObject(4, entity.getDate());
        savePreparedStatement.setInt(5, entity.getPagesAmount());
        savePreparedStatement.setInt(6, entity.getCopiesAmount());
        savePreparedStatement.setString(7, entity.getText());
        savePreparedStatement.setInt(8, entity.getLikesAmount());
        savePreparedStatement.setInt(9, entity.getCommentsAmount());
    }

    /**
     * Set {@link Book} instance data to prepared statement to execute update statement.
     * Template method implementation for {@link Book} database entity.
     * @param entity entity that need to update.
     * @param updatePreparedStatement Made update entity prepared statement
     * @throws SQLException when database exception occurs
     */
    @Override
    protected void setUpdatePreparedStatementValues(Book entity, PreparedStatement updatePreparedStatement) throws SQLException {
        updatePreparedStatement.setString(1, entity.getName());
        updatePreparedStatement.setLong(2, entity.getAuthor().getId());
        updatePreparedStatement.setLong(3, entity.getGenre().getId());
        updatePreparedStatement.setObject(4, entity.getDate());
        updatePreparedStatement.setInt(5, entity.getPagesAmount());
        updatePreparedStatement.setInt(6, entity.getCopiesAmount());
        updatePreparedStatement.setString(7, entity.getText());
        updatePreparedStatement.setInt(8, entity.getLikesAmount());
        updatePreparedStatement.setInt(9, entity.getCommentsAmount());
        updatePreparedStatement.setLong(10, entity.getId());
    }

    /**
     * Finds and returns result of find {@link Book} instance by specified name.
     * Returns found book if there is book with passed name in database table or
     * empty optional otherwise
     * @param name name of book that need to be found.
     * @return Found book in optional if there is book with passed name
     * or empty optional otherwise
     * @throws DAOException when database exception occurs
     */
    @Override
    public Optional<Book> findByName(String name) throws DAOException {
        final List<Book> foundBooks = findPreparedEntities(FIND_BY_NAME_SQL, preparedStatement -> preparedStatement.setString(1, name));
        return foundBooks.stream().findAny();
    }

    /**
     * Finds and returns result of find {@link Book} instance by specified author name.
     * Returns found books that have passed author name.
     * @param authorName name of author book that need to be found.
     * @return found books that have passed author name collection.
     */
    @Override
    public List<Book> findByAuthorName(String authorName) {
        return findPreparedEntities((FIND_BY_AUTHOR_NAME_SQL), preparedStatement -> preparedStatement.setString(1, authorName));
    }

    /**
     * Finds and returns result of find {@link Book} insatnce by specified genre.
     * Returns found books that have passed genre
     * @param genre genre of book that need to be found
     * @return found books that have passed genre
     */
    @Override
    public List<Book> findByGenre(Genre genre) {
        return findPreparedEntities(FIND_BY_GENRE_SQL, preparedStatement -> preparedStatement.setLong(1, genre.getId()));
    }

    /**
     * Nested class that encapsulates single {@link MySQLBookDao} instance. Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final MySQLBookDao INSTANCE = new MySQLBookDao();
    }
}
