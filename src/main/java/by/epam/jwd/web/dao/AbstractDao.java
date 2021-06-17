package by.epam.jwd.web.dao;


import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.exception.DAOException;
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
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;

public abstract class AbstractDao<T extends DbEntity> implements Dao<T> {
    private static final Logger logger = LogManager.getLogger(AbstractDao.class);

    private static final String GENERATED_KEY_COLUMN = "GENERATED_KEY";
    private static final String SAVE_OPERATION_UNSUPPORTED_MESSAGE = "Save entity operation is unsupported for this service";
    private static final String FIND_OPERATION_UNSUPPORTED_MESSAGE = "Find entities operation is unsupported for this service";
    private static final String UPDATE_OPERATION_UNSUPPORTED_MESSAGE = "Update entity operation is unsupported for this service";
    private static final String DELETE_OPERATION_UNSUPPORTED_MESSAGE = "Delete entity operation is unsupported for this service";
    private static final String SAVED_ENTITY_WAS_NOT_FOUND_BY_ID_MESSAGE = "Entity was saved, but was not found by id %s";
    private static final String COULD_NOT_SAVE_ENTITY_MESSAGES = "Could not save entity %s, Exception: %s";
    private static final String COULD_NOT_FIND_ALL_ENTITIES_MESSAGE = "Could not find all entities, Exception : %s";
    private static final String COULD_NOT_UPDATE_ENTITY_MESSAGE = "Could not update entity %s, Exception: %s";
    private static final String COULD_NOT_DELETE_ENTITY_MESSAGE = "Could not delete entity with id %d, Exception : %s";

    private final String findAllSql;
    private final String findByIdSql;
    private final String deleteSql;
    private final String saveSql;
    private final String updateSql;

    public AbstractDao(String findAllSql, String saveSql, String updateSql, String deleteSql) {
        this.findAllSql = findAllSql;
        this.findByIdSql = String.format("%s where id = ?", findAllSql);
        this.saveSql = saveSql;
        this.updateSql = updateSql;
        this.deleteSql = deleteSql;
    }


    @Override
    public T save(T entity) {
        if (saveSql == null) {
            throw new UnsupportedOperationException(SAVE_OPERATION_UNSUPPORTED_MESSAGE);
        }
        try (Connection connection = ConnectionPool.getConnectionPool().takeFreeConnection();
             PreparedStatement saveStatement = connection.prepareStatement(saveSql, Statement.RETURN_GENERATED_KEYS)) {
            setSavePrepareStatementValues(entity, saveStatement);
            saveStatement.executeUpdate();
            ResultSet generatedKeyResultSet = saveStatement.getGeneratedKeys();
            generatedKeyResultSet.next();
            Long id = generatedKeyResultSet.getLong(GENERATED_KEY_COLUMN);
            final Optional<T> optionalEntity = findById(id);
            if(!optionalEntity.isPresent()) {
                logger.error(String.format(SAVED_ENTITY_WAS_NOT_FOUND_BY_ID_MESSAGE, entity));
                throw new DAOException(String.format(SAVED_ENTITY_WAS_NOT_FOUND_BY_ID_MESSAGE, entity));
            }
            return optionalEntity.get();
        } catch (SQLException e) {
            logger.error(String.format(COULD_NOT_SAVE_ENTITY_MESSAGES, entity, e));
            throw new DAOException(e);
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
        final List<T> foundEntities = findPreparedEntities(findByIdSql, statement -> statement.setLong(1, id));
        return foundEntities.stream().findAny();
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
            return entity;
        } catch (SQLException e) {
            logger.error(String.format(COULD_NOT_UPDATE_ENTITY_MESSAGE, entity, e));
            throw new DAOException(e);
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
        } catch (SQLException e) {
            logger.error(String.format(COULD_NOT_DELETE_ENTITY_MESSAGE, id, e));
            throw new DAOException(e);
        }
    }

    protected List<T> findEntities(String sql) {
        final List<T> foundEntities = new ArrayList<>();
        try (Connection connection = ConnectionPool.getConnectionPool().takeFreeConnection();
        Statement findAllStatement = connection.createStatement();
        ResultSet resultSet = findAllStatement.executeQuery(sql)) {
            while (resultSet.next()) {
                final T foundEntity = mapResultSet(resultSet);
                foundEntities.add(foundEntity);
            }
            return foundEntities;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    protected List<T> findPreparedEntities(String sql, SQLConsumer<PreparedStatement> preparedStatementConsumer) {
        final List<T> foundEntities = new ArrayList<>();
        try (Connection connection = ConnectionPool.getConnectionPool().takeFreeConnection();
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatementConsumer.accept(preparedStatement);
            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                final T foundEntity = mapResultSet(resultSet);
                foundEntities.add(foundEntity);
            }
            return foundEntities;
        } catch (SQLException e) {
            throw new DAOException(e);
        }
    }

    protected abstract T mapResultSet(ResultSet result) throws SQLException;

    protected abstract void setSavePrepareStatementValues(T entity, PreparedStatement savePreparedStatement) throws SQLException;

    protected abstract void setUpdatePreparedStatementValues(T entity, PreparedStatement updatePreparedStatement) throws SQLException;
}
