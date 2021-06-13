package by.epam.jwd.web.dao;

import by.epam.jwd.web.model.Status;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class MySQLStatusDao extends AbstractDao<Status> implements StatusDao {


    public static MySQLStatusDao getInstance() {
        return Singleton.INSTANCE;
    }

    private static final String TABLE_NAME = "status";
    private static final String ID_COLUMN = "id";
    private static final String NAME_COLUMN = "name";

    private static final String FIND_ALL_SQL = String.format("select %s, %s from %s", ID_COLUMN, NAME_COLUMN, TABLE_NAME);
    private static final String INSERT_PREPARED_SQL = String.format("insert into %s (%s) values (?)", TABLE_NAME, NAME_COLUMN);
    private static final String UPDATE_PREPARED_SQL = String.format("update %s set %s = ? where %s = ?", TABLE_NAME, NAME_COLUMN, ID_COLUMN);
    private static final String DELETE_PREPARED_SQL = String.format("delete from %s where %s = ?", TABLE_NAME, ID_COLUMN);

    private MySQLStatusDao() {
        super(FIND_ALL_SQL, INSERT_PREPARED_SQL, UPDATE_PREPARED_SQL, DELETE_PREPARED_SQL);
    }
    @Override
    protected Status mapResultSet(ResultSet result) throws SQLException {
        return Status.valueOf(result.getString(NAME_COLUMN));
    }

    @Override
    protected void setSavePrepareStatementValues(Status entity, PreparedStatement savePreparedStatement) throws SQLException {
        savePreparedStatement.setString(1, entity.getName());
    }

    @Override
    protected void setUpdatePreparedStatementValues(Status entity, PreparedStatement updatePreparedStatement) throws SQLException {
        updatePreparedStatement.setString(1, entity.getName());
    }

    @Override
    public Optional<Status> findByName(String statusName) {
        return findAll().stream().filter(status -> status.getName().equals(statusName)).findAny();
    }

    private static class Singleton {
        private static final MySQLStatusDao INSTANCE = new MySQLStatusDao();
    }
}
