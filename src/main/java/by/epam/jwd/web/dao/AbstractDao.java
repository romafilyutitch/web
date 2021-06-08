package by.epam.jwd.web.dao;


import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.model.DbEntity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDao<T extends DbEntity> implements Dao<T> {
    private static final Logger logger = LogManager.getLogger(AbstractDao.class);

    private static final String GENERATED_KEY_COLUMN = "GENERATED_KEY";
    public static final String SAVE_OPERATION_UNSUPPORTED_MESSAGE = "Save entity operation is unsupported for this service";
    public static final String FIND_OPERATION_UNSUPPORTED_MESSAGE = "Find entities operation is unsupported for this service";
    public static final String UPDATE_OPERATION_UNSUPPORTED_MESSAGE = "Update entity operation is unsupported for this service";
    public static final String DELETE_OPERATION_UNSUPPORTED_MESSAGE = "Delete entity operation is unsupported for this service";
    private final String deleteSql;
    private final String findAllSql;
    private final String saveSql;
    private final String updateSql;

    public AbstractDao(String findAllSql, String saveSql, String updateSql, String deleteSql) {
        this.findAllSql = findAllSql;
        this.saveSql = saveSql;
        this.updateSql = updateSql;
        this.deleteSql = deleteSql;
    }

    public AbstractDao(String findAllSql, String saveSql, String updateSql) {
        this(findAllSql, saveSql, updateSql, null);
    }

    public AbstractDao(String findAllSql, String saveSql) {
        this(findAllSql, saveSql, null, null);
    }

    public AbstractDao(String findAllSql) {
        this(findAllSql, null, null, null);
    }

    public AbstractDao() {
        this(null, null, null, null);
    }


    @Override
    public T save(T entity) {
        if (saveSql == null) {
            throw new UnsupportedOperationException(SAVE_OPERATION_UNSUPPORTED_MESSAGE);
        }
        final Optional<T> byId = findById(entity.getId());
        if (byId.isPresent()) {
            return byId.get();
        }
        try (Connection connection = ConnectionPool.getConnectionPool().takeFreeConnection();
             PreparedStatement saveStatement = connection.prepareStatement(saveSql, Statement.RETURN_GENERATED_KEYS)) {
            setSavePrepareStatementValues(entity, saveStatement);
            saveStatement.executeUpdate();
            ResultSet generatedKeyResultSet = saveStatement.getGeneratedKeys();
            generatedKeyResultSet.next();
            Long id = generatedKeyResultSet.getLong(GENERATED_KEY_COLUMN);
            final T savedEntity = findById(id).orElseThrow(DAOException::new);
            logger.info("Entity was saved " + savedEntity);
            return savedEntity;
        } catch (SQLException e) {
            logger.error("Error with in saving entity " + entity);
            throw new DAOException("Could not save entity ", e);
        }
    }

    @Override
    public List<T> findAll(){
        if (findAllSql == null) {
            throw new UnsupportedOperationException(FIND_OPERATION_UNSUPPORTED_MESSAGE);
        }
        return findEntities(findAllSql);
    }

    @Override
    public Optional<T> findById(Long id) {
        return findAll().stream().filter(entity -> entity.getId().equals(id)).findAny();
    }

    @Override
    public T update(T entity){
        if (updateSql == null) {
            throw new UnsupportedOperationException(UPDATE_OPERATION_UNSUPPORTED_MESSAGE);
        }
        try (Connection connection = ConnectionPool.getConnectionPool().takeFreeConnection();
             PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
            setUpdatePreparedStatementValues(entity, updateStatement);
            updateStatement.executeUpdate();
            logger.info("Entity was updated " + entity);
            return entity;
        } catch (SQLException e) {
            logger.error("Error with updating entity" + entity);
            throw new DAOException("Could not update entity", e);
        }
    }

    @Override
    public void delete(Long id){
        if (deleteSql == null) {
            throw new UnsupportedOperationException(DELETE_OPERATION_UNSUPPORTED_MESSAGE);
        }
        try (Connection connection = ConnectionPool.getConnectionPool().takeFreeConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
            deleteStatement.setLong(1, id);
            deleteStatement.executeUpdate();
            logger.info("Entity with id " + id + " was deleted");
        } catch (SQLException e) {
            logger.error("Error with deleting entity with id" + id);
            throw new DAOException("Could not delete entity", e);
        }
    }

    protected List<T> findEntities(String sql){
        try (final Connection conn = ConnectionPool.getConnectionPool().takeFreeConnection();
             final Statement statement = conn.createStatement()) {
            try (final ResultSet resultSet = statement.executeQuery(sql)) {
                List<T> entities = new ArrayList<>();
                while (resultSet.next()) {
                    final T entity = mapResultSet(resultSet);
                    entities.add(entity);
                }
                return entities;
            }
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    protected abstract T mapResultSet(ResultSet result) throws SQLException;

    protected abstract void setSavePrepareStatementValues(T entity, PreparedStatement savePreparedStatement) throws SQLException;

    protected abstract void setUpdatePreparedStatementValues(T entity, PreparedStatement updatePreparedStatement) throws SQLException;
}
