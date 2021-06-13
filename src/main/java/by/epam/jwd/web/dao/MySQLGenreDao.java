package by.epam.jwd.web.dao;



import by.epam.jwd.web.exception.DAOException;
import by.epam.jwd.web.model.BookGenre;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class MySQLGenreDao extends AbstractDao<BookGenre> implements GenreDao {
    private static final String TABLE_NAME = "genre";
    private static final String ID_COLUMN = "id";
    private static final String NAME_COLUMN = "name";
    private static final String SAVE_PREPARED_SQL = String.format("insert into %s (%s) values (?)", TABLE_NAME, NAME_COLUMN);
    private static final String FIND_ALL_SQL = String.format("select %s, %s from %s", ID_COLUMN, NAME_COLUMN, TABLE_NAME);
    private static final String UPDATE_PREPARED_SQL = String.format("update %s set %s = ? where %s = ?", TABLE_NAME, NAME_COLUMN, ID_COLUMN);
    private static final String DELETE_PREPARED_SQL = String.format("delete from %s where %s = ?", TABLE_NAME, ID_COLUMN);


    private MySQLGenreDao() {
        super(FIND_ALL_SQL, SAVE_PREPARED_SQL, UPDATE_PREPARED_SQL, DELETE_PREPARED_SQL);
    }

    public static MySQLGenreDao getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public BookGenre save(BookGenre entity) throws DAOException {
        Optional<BookGenre> optionalBookGenre = getByName(entity.getName());
        return optionalBookGenre.orElseGet(() -> super.save(entity));
    }

    @Override
    protected BookGenre mapResultSet(ResultSet result) throws SQLException {
        return new BookGenre(result.getLong(ID_COLUMN), result.getString(NAME_COLUMN));
    }

    @Override
    protected void setSavePrepareStatementValues(BookGenre entity, PreparedStatement savePreparedStatement) throws SQLException {
        savePreparedStatement.setString(1, entity.getName());
    }

    @Override
    protected void setUpdatePreparedStatementValues(BookGenre entity, PreparedStatement updatePreparedStatement) throws SQLException {
        updatePreparedStatement.setString(1, entity.getName());
        updatePreparedStatement.setLong(2, entity.getId());
    }

    @Override
    public Optional<BookGenre> getByName(String genreName) throws DAOException {
        return findAll().stream().filter(genre -> genre.getName().equalsIgnoreCase(genreName)).findAny();
    }

    private static class Singleton {
        private static final MySQLGenreDao INSTANCE = new MySQLGenreDao();
    }
}
