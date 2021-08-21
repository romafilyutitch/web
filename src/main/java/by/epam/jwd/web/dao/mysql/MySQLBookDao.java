package by.epam.jwd.web.dao.mysql;


import by.epam.jwd.web.dao.api.AbstractDao;
import by.epam.jwd.web.dao.api.BookDao;
import by.epam.jwd.web.exception.DAOException;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * {@link AbstractDao} abstract class implementation for {@link Book} database entity. Links to book database table
 * and performs sql statements with that table.
 *
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class MySQLBookDao extends AbstractDao<Book> implements BookDao {
    private static final String TABLE_NAME = "book";

    private static final String FIND_ALL_SQL = "select book.id, book.name, book.author, genre.id, genre.name, book.date, book.pages_amount, book.copies_amount, book.text, book.likes_amount, book.comments_amount " +
            "from book inner join genre on book.genre_id = genre.id";
    private static final String SAVE_SQL = "insert into book (name, author, genre_id, date, pages_amount, copies_amount, text, likes_amount, comments_amount) values (?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_SQL = "update book set name = ?, author = ?, genre_id = ?, date = ?, pages_amount = ?, copies_amount = ?, text = ?, likes_amount = ?, comments_amount = ? where id = ?";
    private static final String DELETE_SQL = "delete from book where id = ?";

    private static final String FIND_BY_NAME_TEMPLATE = "%s where book.name like ? order by book.name";
    private static final String FIND_BY_AUTHOR_NAME_TEMPLATE = "%s where book.author = ? order by book.name";
    private static final String FIND_BY_GENRE_TEMPLATE = "%s where genre.id = ? order by book.name";
    private static final String FIND_BY_NAME_SQL = String.format(FIND_BY_NAME_TEMPLATE, FIND_ALL_SQL);
    private static final String FIND_BY_AUTHOR_NAME_SQL = String.format(FIND_BY_AUTHOR_NAME_TEMPLATE, FIND_ALL_SQL);
    private static final String FIND_BY_GENRE_SQL = String.format(FIND_BY_GENRE_TEMPLATE, FIND_ALL_SQL);

    private static final String BOOK_ID_COLUMN = "book.id";
    private static final String BOOK_NAME_COLUMN = "book.name";
    private static final String BOOK_AUTHOR_COLUMN = "book.author";
    private static final String GENRE_NAME_COLUMN = "genre.name";
    private static final String BOOK_DATE_COLUMN = "book.date";
    private static final String BOOK_PAGES_AMOUNT_COLUMN = "book.pages_amount";
    private static final String BOOK_COPIES_AMOUNT_COLUMN = "book.copies_amount";
    private static final String BOOK_TEXT_COLUMN = "book.text";
    private static final String BOOK_LIKES_AMOUNT_COLUMN = "book.likes_amount";
    private static final String BOOK_COMMENTS_AMOUNT_COLUMN = "book.comments_amount";

    private MySQLBookDao() {
        super(TABLE_NAME, FIND_ALL_SQL, SAVE_SQL, UPDATE_SQL, DELETE_SQL, BOOK_NAME_COLUMN);
    }

    /**
     * Returns singleton from nested class that encapsulates single class instance.
     *
     * @return class instance.
     */
    public static MySQLBookDao getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Maps result from find sql statement to {@link Book} instance and returns it.
     * Template method implementation for {@link Book} database entity.
     *
     * @param result Made during sql find statement execution result.
     * @return {@link Book} instance
     * @throws SQLException when exception in database occurs
     */
    @Override
    protected Book mapResultSet(ResultSet result) throws SQLException {
        final long id = result.getLong(BOOK_ID_COLUMN);
        final String name = result.getString(BOOK_NAME_COLUMN);
        final String author = result.getString(BOOK_AUTHOR_COLUMN);
        final String genreName = result.getString(GENRE_NAME_COLUMN);
        final LocalDate date = result.getObject(BOOK_DATE_COLUMN, LocalDate.class);
        final int pagesAmount = result.getInt(BOOK_PAGES_AMOUNT_COLUMN);
        final int copiesAmount = result.getInt(BOOK_COPIES_AMOUNT_COLUMN);
        final String text = result.getString(BOOK_TEXT_COLUMN);
        final Integer likesAmount = result.getInt(BOOK_LIKES_AMOUNT_COLUMN);
        final Integer commentsAmount = result.getInt(BOOK_COMMENTS_AMOUNT_COLUMN);
        final Genre foundGenre = Genre.valueOf(genreName.toUpperCase());
        return new Book(id, name, author, foundGenre, date, pagesAmount, copiesAmount, text, likesAmount, commentsAmount);
    }

    /**
     * Set {@link Book} instance data to prepared statement to execute save statement.
     * Template method implementation for {@link Book} database book.
     *
     * @param book                book that need to save.
     * @param savePreparedStatement Made save book prepared statement.
     * @throws SQLException when exception in database occurs.
     */
    @Override
    protected void setSavePrepareStatementValues(Book book, PreparedStatement savePreparedStatement) throws SQLException {
        savePreparedStatement.setString(1, book.getName());
        savePreparedStatement.setString(2, book.getAuthor());
        savePreparedStatement.setLong(3, book.getGenre().getId());
        savePreparedStatement.setObject(4, book.getDate());
        savePreparedStatement.setInt(5, book.getPagesAmount());
        savePreparedStatement.setInt(6, book.getCopiesAmount());
        savePreparedStatement.setString(7, book.getText());
        savePreparedStatement.setInt(8, book.getLikesAmount());
        savePreparedStatement.setInt(9, book.getCommentsAmount());
    }

    /**
     * Set {@link Book} instance data to prepared statement to execute update statement.
     * Template method implementation for {@link Book} database book.
     *
     * @param book                  book that need to update.
     * @param updatePreparedStatement Made update book prepared statement
     * @throws SQLException when database exception occurs
     */
    @Override
    protected void setUpdatePreparedStatementValues(Book book, PreparedStatement updatePreparedStatement) throws SQLException {
        updatePreparedStatement.setString(1, book.getName());
        updatePreparedStatement.setString(2, book.getAuthor());
        updatePreparedStatement.setLong(3, book.getGenre().getId());
        updatePreparedStatement.setObject(4, book.getDate());
        updatePreparedStatement.setInt(5, book.getPagesAmount());
        updatePreparedStatement.setInt(6, book.getCopiesAmount());
        updatePreparedStatement.setString(7, book.getText());
        updatePreparedStatement.setInt(8, book.getLikesAmount());
        updatePreparedStatement.setInt(9, book.getCommentsAmount());
        updatePreparedStatement.setLong(10, book.getId());
    }

    /**
     * Finds and returns result of find {@link Book} instance by specified name.
     * Returns found books if there are books with passed name in database table
     *
     * @param name name of book that need to be found.
     * @return Found book in optional if there is book with passed name
     * or empty optional otherwise
     * @throws DAOException when database exception occurs
     */
    @Override
    public List<Book> findByName(String name) {
        return findPreparedEntities(FIND_BY_NAME_SQL, preparedStatement -> preparedStatement.setString(1, name + "%"));
    }

    /**
     * Finds and returns result of find {@link Book} instance by specified author name.
     * Returns found books that have passed author name.
     *
     * @param authorName name of author book that need to be found.
     * @return found books that have passed author name collection.
     */
    @Override
    public List<Book> findByAuthorName(String authorName) {
        return findPreparedEntities((FIND_BY_AUTHOR_NAME_SQL), preparedStatement -> preparedStatement.setString(1, authorName));
    }

    /**
     * Finds and returns result of find {@link Book} instance by specified genre.
     * Returns found books that have passed genre
     *
     * @param genre genre of book that need to be found
     * @return found books that have passed genre
     */
    @Override
    public List<Book> findByGenre(Genre genre) {
        return findPreparedEntities(FIND_BY_GENRE_SQL, preparedStatement -> preparedStatement.setLong(1, genre.getId()));
    }

    /**
     * Nested class that encapsulates single {@link MySQLBookDao} instance. Singleton pattern variation.
     *
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final MySQLBookDao INSTANCE = new MySQLBookDao();
    }
}
