package by.epam.jwd.web.dao;


import by.epam.jwd.web.model.Author;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class MySQLAuthorDao extends AbstractDao<Author> implements AuthorDao {

    private static final String TABLE_NAME = "author";
    private static final String ID_COLUMN = "id";
    private static final String NAME_COLUMN = "name";

    private static final String FIND_ALL_SQL = "select id, name from author";
    private static final String FIND_BY_ID_PREPARED_SQL = String.format("%s where id = ?", FIND_ALL_SQL);
    private static final String SAVE_PREPARED_SQL = "insert into author (name) value (?)";
    private static final String UPDATE_PREPARED_SQL = "update author set name = ? where id = ?";
    private static final String DELETE_PREPARED_SQL = "delete from author where id = ?";
    public static final String SELECT_BY_NAME_PREPARED_SQL = String.format("%s where name = ?", FIND_ALL_SQL);

    private MySQLAuthorDao() {
        super(FIND_ALL_SQL,FIND_BY_ID_PREPARED_SQL, SAVE_PREPARED_SQL, UPDATE_PREPARED_SQL, DELETE_PREPARED_SQL);
    }

    public static MySQLAuthorDao getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    protected Author mapResultSet(ResultSet result) throws SQLException {
        final long id = result.getLong("id");
        final String name = result.getString("name");
        return new Author(id, name);
    }

    @Override
    protected void setSavePrepareStatementValues(Author entity, PreparedStatement savePreparedStatement) throws SQLException {
        savePreparedStatement.setString(1, entity.getName());
    }

    @Override
    protected void setUpdatePreparedStatementValues(Author entity, PreparedStatement updatePreparedStatement) throws SQLException {
        updatePreparedStatement.setString(1, entity.getName());
        updatePreparedStatement.setLong(2, entity.getId());
    }

    @Override
    public Optional<Author> getByName(String authorName) {
        final List<Author> foundEntities = findPreparedEntities(SELECT_BY_NAME_PREPARED_SQL, preparedStatement -> preparedStatement.setString(1, authorName));
        return foundEntities.stream().findAny();
    }

    private static class Singleton {
        private static final MySQLAuthorDao INSTANCE = new MySQLAuthorDao();
    }
}
