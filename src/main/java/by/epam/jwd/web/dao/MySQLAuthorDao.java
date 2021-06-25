package by.epam.jwd.web.dao;


import by.epam.jwd.web.model.Author;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.StringJoiner;

public class MySQLAuthorDao extends AbstractDao<Author> implements AuthorDao {
    private static final String TABLE_NAME = "author";
    private static final String ID_COLUMN = "id";
    private static final String NAME_COLUMN = "name";

    private static final List<String> COLUMNS = Arrays.asList(ID_COLUMN, NAME_COLUMN);

    private final String findByNameSql;

    private MySQLAuthorDao() {
        super(TABLE_NAME,COLUMNS);
        final StringJoiner joiner = new StringJoiner(",");
        COLUMNS.forEach(joiner::add);
        findByNameSql = String.format(FIND_BY_COLUMN_SQL_TEMPLATE, joiner, TABLE_NAME, NAME_COLUMN);
    }

    public static MySQLAuthorDao getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    protected Author mapResultSet(ResultSet result) throws SQLException {
        final long id = result.getLong(ID_COLUMN);
        final String name = result.getString(NAME_COLUMN);
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
        final List<Author> foundEntities = findPreparedEntities(findByNameSql, preparedStatement -> preparedStatement.setString(1, authorName));
        return foundEntities.stream().findAny();
    }

    private static class Singleton {
        private static final MySQLAuthorDao INSTANCE = new MySQLAuthorDao();
    }
}
