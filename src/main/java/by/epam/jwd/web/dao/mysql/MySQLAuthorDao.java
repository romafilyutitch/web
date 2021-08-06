package by.epam.jwd.web.dao.mysql;


import by.epam.jwd.web.dao.api.AbstractDao;
import by.epam.jwd.web.dao.api.AuthorDao;
import by.epam.jwd.web.model.Author;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/**
 * {@link AbstractDao} abstract class implementation for {@link Author} database entity. Links to author database table
 * and performs sql statements with that table.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class MySQLAuthorDao extends AbstractDao<Author> implements AuthorDao {
    private static final String TABLE_NAME = "author";

    private static final String FIND_ALL_SQL = "select author.id, author.name from author";
    private static final String SAVE_SQL = "insert into author (name) value (?)";
    private static final String UPDATE_SQL = "update author set name = ? where id = ?";
    private static final String DELETE_SQL = "delete from author where id = ?";
    private static final String FIND_BY_NAME_SQL = "select author.id, author.name from author where name = ?";

    private static final String AUTHOR_ID_COLUMN = "author.id";
    private static final String AUTHOR_NAME_COLUMN = "author.name";

    private MySQLAuthorDao() {
       super(TABLE_NAME, FIND_ALL_SQL, SAVE_SQL, UPDATE_SQL, DELETE_SQL);
    }

    /**
     * Returns Singleton from nested class class that encapsulates single class instance
     * @return class instance
     */
    public static MySQLAuthorDao getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Maps result from find sql statement to {@link Author} instance and returns it. Template method implementation
     * @param result Made during sql find statement execution result.
     * @return {@link Author} instance
     * @throws SQLException when exception in database occurs
     * @see "Template method pattern"
     */
    @Override
    protected Author mapResultSet(ResultSet result) throws SQLException {
        final long id = result.getLong(AUTHOR_ID_COLUMN);
        final String name = result.getString(AUTHOR_NAME_COLUMN);
        return new Author(id, name);
    }

    /**
     * Set {@link Author} instance data to prepared statement to execute save statement. Template method implementation
     * @param entity entity that need to save
     * @param savePreparedStatement Made save entity prepared statement
     * @throws SQLException when exception in database occurs
     * @see "Template method pattern"
     */
    @Override
    protected void setSavePrepareStatementValues(Author entity, PreparedStatement savePreparedStatement) throws SQLException {
        savePreparedStatement.setString(1, entity.getName());
    }

    /**
     * Set {@link Author} instance data to prepared statement to execute update statement. Template method implementation.
     * @param entity entity that need to update.
     * @param updatePreparedStatement Made update entity prepared statement.
     * @throws SQLException when exception in database occurs
     * @see "Template method pattern"
     */
    @Override
    protected void setUpdatePreparedStatementValues(Author entity, PreparedStatement updatePreparedStatement) throws SQLException {
        updatePreparedStatement.setString(1, entity.getName());
        updatePreparedStatement.setLong(2, entity.getId());
    }

    /**
     * Finds and return result of find Author by name. Template method implementation.
     * Returns saved {@link Author} instance that has passed name or
     * empty optional otherwise.
     * @param authorName name of that need to find.
     * @return Found {@link Author} instance if found or empty Optional if there is no author with passed name
     */
    @Override
    public Optional<Author> getByName(String authorName) {
        final List<Author> foundEntities = findPreparedEntities(FIND_BY_NAME_SQL, preparedStatement -> preparedStatement.setString(1, authorName));
        return foundEntities.stream().findAny();
    }

    /**
     * Nested class that encapsulates single {@link MySQLAuthorDao} instance. Singleton pattern implementation
     * @see "Singleton pattenr"
     */
    private static class Singleton {
        private static final MySQLAuthorDao INSTANCE = new MySQLAuthorDao();
    }
}
