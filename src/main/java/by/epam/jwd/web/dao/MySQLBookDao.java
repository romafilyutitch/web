package by.epam.jwd.web.dao;


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

public class MySQLBookDao extends AbstractDao<Book> implements BookDao {

    private static final String FIND_ALL_SQL = "select book.id, book.name, author.id, author.name, genre.name, book.date, book.pages_amount, book.copies_amount, book.description from book inner join author on book.author = author.id inner join genre on book.genre = genre.id";
    private static final String FIND_BY_ID_PREPARED_SQL = String.format("%s where book.id = ?", FIND_ALL_SQL);
    private static final String SAVE_PREPARED_SQL = "insert into book (name, author, genre, date, pages_amount, copies_amount, description) value (?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_PREPARED_SQL = "update book set name = ?, author = ?, genre = ?, date = ?, pages_amount = ?, copies_amount = ?, description = ? where id = ?";
    private static final String DELETE_PREPARED_SQL = "delete from book where id = ?";
    private static final String FIND_BY_NAME_SQL = String.format("%s where book.name = ?", FIND_ALL_SQL);
    private static final String FIND_BY_AUTHOR_NAME_PREPARED_SQL = String.format("%s where author.name = ?", FIND_ALL_SQL);
    private static final String FIND_BY_GENRE_NAME_PREPARED_SQL = String.format("%s where genre.id = ?", FIND_ALL_SQL);

    private MySQLBookDao() {
        super(FIND_ALL_SQL, FIND_BY_ID_PREPARED_SQL, SAVE_PREPARED_SQL, UPDATE_PREPARED_SQL, DELETE_PREPARED_SQL);
    }

    public static MySQLBookDao getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    protected Book mapResultSet(ResultSet result) throws SQLException, DAOException {
        final long id = result.getLong("book.id");
        final String name = result.getString("book.name");
        final long authorId = result.getLong("author.id");
        final String authorName = result.getString("author.name");
        final Genre genre = Genre.valueOf(result.getString("genre.name"));
        final LocalDate date = result.getObject("book.date", LocalDate.class);
        final int pagesAmount = result.getInt("book.pages_amount");
        final int copiesAmount = result.getInt("book.copies_amount");
        final String description = result.getString("book.description");
        return new Book(id, name, new Author(authorId, authorName), genre, date, pagesAmount, copiesAmount, description);
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
        updatePreparedStatement.setLong(8, entity.getId());
    }

    @Override
    public Optional<Book> findBookByName(String name) throws DAOException {
        final List<Book> foundBooks = findPreparedEntities(FIND_BY_NAME_SQL, preparedStatement -> preparedStatement.setString(1, name));
        return foundBooks.stream().findAny();
    }

    @Override
    public List<Book> findBooksByAuthorName(String authorName) throws DAOException {
        return findPreparedEntities(FIND_BY_AUTHOR_NAME_PREPARED_SQL, preparedStatement -> preparedStatement.setString(1, authorName));
    }

    @Override
    public List<Book> findBooksByGenre(Genre genre) throws DAOException {
        return findPreparedEntities(FIND_BY_GENRE_NAME_PREPARED_SQL, preparedStatement -> preparedStatement.setLong(1, genre.getId()));
    }

    private static class Singleton {
        private static final MySQLBookDao INSTANCE = new MySQLBookDao();
    }
}
