package by.epam.jwd.web.dao;



import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.BookAuthor;
import by.epam.jwd.web.model.BookGenre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MySQLBookDao extends AbstractDao<Book> implements BookDao {
    private static final MySQLAuthorDao BOOK_AUTHOR_DAO = MySQLAuthorDao.getInstance();
    private static final MySQLGenreDao BOOK_GENRE_DAO = MySQLGenreDao.getInstance();

    private static final String ID_COLUMN = "id";
    private static final String NAME_COLUMN = "name";
    private static final String AUTHOR_COLUMN = "author";
    private static final String GENRE_COLUMN = "genre";
    private static final String YEAR_COLUMN = "year";
    private static final String PAGES_AMOUNT_COLUMN = "pages_amount";
    private static final String DESCRIPTION_COLUMN = "description";
    private static final String SAVE_PREPARED_SQL = "insert into lib_book (name, author, genre, year, pages_amount, description, text) values (?, ?, ?, ?, ?, ?, ?)";
    private static final String FIND_ALL_SQL = "select id, name, author, genre, year, pages_amount, copies_amount, description, text from lib_book";
    private static final String UPDATE_PREPARED_SQL = "update lib_book set name = ?, author = ?, genre = ?, year = ?, pages_amount = ?, copies_amount = ?, description = ?, text = ? where id = ?";
    private static final String DELETE_PREPARED_SQL = "delete from lib_book where id = ?";
    public static final String COPIES_AMOUNT_COLUMN = "copies_amount";
    private static final String TEXT_COLUMN = "text";

    private MySQLBookDao() {
        super(FIND_ALL_SQL, SAVE_PREPARED_SQL, UPDATE_PREPARED_SQL, DELETE_PREPARED_SQL);
    }

    public static MySQLBookDao getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    protected Book mapResultSet(ResultSet result) throws SQLException, DAOException {
        Optional<BookAuthor> optionalAuthor = BOOK_AUTHOR_DAO.findById(result.getLong(AUTHOR_COLUMN));
        Optional<BookGenre> optionalBookGenre = BOOK_GENRE_DAO.findById(result.getLong(GENRE_COLUMN));
        final Long id = result.getLong(ID_COLUMN);
        final String name = result.getString(NAME_COLUMN);
        final LocalDate date = result.getObject(YEAR_COLUMN, LocalDate.class);
        final int pagesAmount = result.getInt(PAGES_AMOUNT_COLUMN);
        final int booksAmount = result.getInt(COPIES_AMOUNT_COLUMN);
        final String description = result.getString(DESCRIPTION_COLUMN);
        final String text = result.getString(TEXT_COLUMN);
        return new Book(id, name, optionalAuthor.orElseThrow(DAOException::new), optionalBookGenre.orElseThrow(DAOException::new), date, pagesAmount, booksAmount, description, text);
    }

    @Override
    protected void setSavePrepareStatementValues(Book entity, PreparedStatement savePreparedStatement) throws SQLException, DAOException {
        final BookAuthor bookAuthor = BOOK_AUTHOR_DAO.save(entity.getAuthor());
        final BookGenre bookGenre = BOOK_GENRE_DAO.save(entity.getGenre());
        savePreparedStatement.setString(1, entity.getName());
        savePreparedStatement.setLong(2, bookAuthor.getId());
        savePreparedStatement.setLong(3, bookGenre.getId());
        savePreparedStatement.setObject(4, entity.getDate());
        savePreparedStatement.setInt(5, entity.getPagesAmount());
        savePreparedStatement.setString(6, entity.getDescription());
        savePreparedStatement.setString(7, entity.getText());
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
        updatePreparedStatement.setString(8, entity.getText());
        updatePreparedStatement.setLong(9, entity.getId());
    }

    @Override
    public Optional<Book> findBookByName(String name) throws DAOException {
        return findAll().stream().filter(book -> book.getName().equals(name)).findAny();
    }

    @Override
    public List<Book> findBooksByAuthorName(String authorName) throws DAOException {
        return findAll().stream().filter(book -> book.getAuthor().getName().equals(authorName)).collect(Collectors.toList());
    }

    @Override
    public List<Book> findBooksByGenreName(String genreName) throws DAOException {
        return findAll().stream().filter(book -> book.getGenre().getName().equals(genreName)).collect(Collectors.toList());
    }

    @Override
    public List<Book> findBooksByYear(int year) throws DAOException {
        return findAll().stream().filter(book -> book.getDate().getYear() == year).collect(Collectors.toList());
    }

    private static class Singleton {
        private static final MySQLBookDao INSTANCE = new MySQLBookDao();
    }
}
