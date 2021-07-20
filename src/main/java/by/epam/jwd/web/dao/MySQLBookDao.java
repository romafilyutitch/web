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
        final String genreName = result.getString(GENRE_NAME_COLUMN);
        final LocalDate date = result.getObject(DATE_COLUMN, LocalDate.class);
        final int pagesAmount = result.getInt(PAGES_AMOUNT_COLUMN);
        final int copiesAmount = result.getInt(COPIES_AMOUNT_COLUMN);
        final String description = result.getString(DESCRIPTION_COLUMN);
        final Integer likes = result.getInt(LIKES_COLUMN);
        final Author foundAuthor = buildAuthor(result);
        final Genre foundGenre = Genre.valueOf(genreName.toUpperCase());
        return new Book(id, name, foundAuthor, foundGenre, date, pagesAmount, copiesAmount, description, likes);
    }

    private Author buildAuthor(ResultSet resultSet) throws SQLException {
        final long authorId = resultSet.getLong(AUTHOR_ID_COLUMN);
        final String authorName = resultSet.getString(AUTHOR_NAME_COLUMN);
        return new Author(authorId, authorName);
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
    public List<Book> findByAuthor(Author author) throws DAOException {
        return findPreparedEntities((FIND_BY_AUTHOR_SQL), preparedStatement -> preparedStatement.setLong(1, author.getId()));
    }

    @Override
    public List<Book> findByGenre(Genre genre) throws DAOException {
        return findPreparedEntities(FIND_BY_GENRE_SQL, preparedStatement -> preparedStatement.setLong(1, genre.getId()));
    }

    private static class Singleton {
        private static final MySQLBookDao INSTANCE = new MySQLBookDao();
    }
}
