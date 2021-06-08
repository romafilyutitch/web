package by.epam.jwd.web.dao;



import by.epam.jwd.web.model.BookAuthor;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class MySQLAuthorDao extends AbstractDao<BookAuthor> implements AuthorDao{
    private static final String SAVE_PREPARED_SQL = "insert into book_author (name) values (?)";
    private static final String FIND_ALL_PREPARED_SQL = "select id, name from book_author";
    private static final String UPDATE_PREPARED_SQL = "update book_author set name = ? where id = ?";
    private static final String DELETE_PREPARED_SQL = "delete from book_author where id = ?";
    public static final String ID_COLUMN = "id";
    public static final String NAME_COLUMN = "name";

    private MySQLAuthorDao() {
        super(FIND_ALL_PREPARED_SQL, SAVE_PREPARED_SQL);
    }

    public static MySQLAuthorDao getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public BookAuthor save(BookAuthor entity) {
        Optional<BookAuthor> optionalBookAuthor = getByName(entity.getName());
        if (optionalBookAuthor.isPresent()) {
            return optionalBookAuthor.get();
        } else {
            return super.save(entity);
        }
    }

    @Override
    protected BookAuthor mapResultSet(ResultSet result) throws SQLException {
        return new BookAuthor(result.getLong(ID_COLUMN), result.getString(NAME_COLUMN));
    }

    @Override
    protected void setSavePrepareStatementValues(BookAuthor entity, PreparedStatement savePreparedStatement) throws SQLException {
        savePreparedStatement.setString(1, entity.getName());
    }

    @Override
    protected void setUpdatePreparedStatementValues(BookAuthor entity, PreparedStatement updatePreparedStatement) throws SQLException {
        updatePreparedStatement.setString(1, entity.getName());
        updatePreparedStatement.setLong(2, entity.getId());
    }



    @Override
    public Optional<BookAuthor> getByName(String authorName) throws DAOException {
        return findAll().stream().filter(author -> author.getName().equals(authorName)).findAny();
    }

    private static class Singleton {
        private static final MySQLAuthorDao INSTANCE = new MySQLAuthorDao();
    }
}
