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
import java.util.Optional;

public abstract class AbstractDao<T extends DbEntity> implements Dao<T> {
    private static final Logger logger = LogManager.getLogger(AbstractDao.class);

    private static final String GENERATED_KEY_COLUMN = "GENERATED_KEY";
    private static final String TRYING_TO_SAVE_ENTITY_MESSAGE = "Trying to save entity %s ";
    private static final String SAVED_ENTITY_WAS_NOT_FOUND_BY_ID_MESSAGE = "Saved entity was not found by id %s";
    private static final String ENTITY_WAS_SAVED_MESSAGE = "Entity %s was saved";
    private static final String COULD_NOT_SAVE_ENTITY_MESSAGE = "Could not save entity %s ";
    private static final String TRYING_TO_UPDATE_ENTITY_MESSAGE = "Trying to update entity %s";
    private static final String ENTITY_WAS_UPDATED_MESSAGE = "Entity %s was updated";
    private static final String COULD_NOT_UPDATE_ENTITY_MESSAGE = "Could not update entity %s ";
    private static final String TRYING_TO_DELETE_ENTITY_MESSAGE = "Trying to delete entity with id %d";
    private static final String ENTITY_WAS_DELETED_MESSAGE = "Entity with id %d was deleted";
    private static final String COULD_NOT_DELETE_ENTITY_MESSAGE = "Could not delete entity with id %d";
    private static final String TRYING_TO_FIND_ENTITIES_BY_SQL_MESSAGE = "Trying to find entities by sql %s";
    private static final String ENTITIES_BY_SQL_WAS_FOUND_MESSAGE = "Entities by sql was found %s";
    private static final String COULD_NOT_FIND_ENTITIES_BY_SQL_MESSAGE = "Could not find entities by sql %s";
    private static final String TRYING_TO_FIND_ENTITIES_BY_PREPARED_SQL_MESSAGE = "Trying to find entities by prepared sql %s";
    private static final String ENTITIES_BY_PREPARED_SQL_WAS_FOUND_MESSAGE = "Entities by prepared sql was found %s";
    private static final String COULD_NOT_FIND_ENTITIES_BY_PREPARED_SQL_MESSAGE = "Entities by prepared sql was found %s";

    private final String findAllSql;
    private final String findByIdSql;
    private final String deleteSql;
    private final String saveSql;
    private final String updateSql;

    public AbstractDao(String findAllSql, String findByIdSql, String saveSql, String updateSql, String deleteSql) {
        this.findAllSql = findAllSql;
        this.findByIdSql = findByIdSql;
        this.saveSql = saveSql;
        this.updateSql = updateSql;
        this.deleteSql = deleteSql;
    }

    @Override
    public T save(T entity) {
        logger.trace(String.format(TRYING_TO_SAVE_ENTITY_MESSAGE, entity));
        try (Connection connection = ConnectionPool.getConnectionPool().takeFreeConnection();
             PreparedStatement saveStatement = connection.prepareStatement(saveSql, Statement.RETURN_GENERATED_KEYS)) {
            setSavePrepareStatementValues(entity, saveStatement);
            saveStatement.executeUpdate();
            ResultSet generatedKeyResultSet = saveStatement.getGeneratedKeys();
            generatedKeyResultSet.next();
            Long id = generatedKeyResultSet.getLong(GENERATED_KEY_COLUMN);
            final Optional<T> optionalEntity = findById(id);
            if (!optionalEntity.isPresent()) {
                logger.error(String.format(SAVED_ENTITY_WAS_NOT_FOUND_BY_ID_MESSAGE, entity));
                throw new DAOException(String.format(SAVED_ENTITY_WAS_NOT_FOUND_BY_ID_MESSAGE, entity));
            }
            logger.info(String.format(ENTITY_WAS_SAVED_MESSAGE, entity));
            return optionalEntity.get();
        } catch (SQLException e) {
            logger.error(String.format(COULD_NOT_SAVE_ENTITY_MESSAGE, entity), e);
            throw new DAOException(String.format(COULD_NOT_SAVE_ENTITY_MESSAGE, entity), e);
        }
    }

    @Override
    public List<T> findAll() {
        return findEntities(findAllSql);
    }

    @Override
    public Optional<T> findById(Long id) {
        final List<T> foundEntities = findPreparedEntities(findByIdSql, statement -> statement.setLong(1, id));
        return foundEntities.stream().findAny();
    }

    @Override
    public T update(T entity) {
        logger.trace(String.format(TRYING_TO_UPDATE_ENTITY_MESSAGE, entity));
        try (Connection connection = ConnectionPool.getConnectionPool().takeFreeConnection();
             PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
            setUpdatePreparedStatementValues(entity, updateStatement);
            updateStatement.executeUpdate();
            logger.info(String.format(ENTITY_WAS_UPDATED_MESSAGE, entity));
            return entity;
        } catch (SQLException e) {
            logger.error(String.format(COULD_NOT_UPDATE_ENTITY_MESSAGE, entity), e);
            throw new DAOException(String.format(COULD_NOT_UPDATE_ENTITY_MESSAGE, entity), e);
        }
    }

    @Override
    public void delete(Long id) {
        logger.trace(String.format(TRYING_TO_DELETE_ENTITY_MESSAGE, id));
        try (Connection connection = ConnectionPool.getConnectionPool().takeFreeConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
            deleteStatement.setLong(1, id);
            deleteStatement.executeUpdate();
            logger.info(String.format(ENTITY_WAS_DELETED_MESSAGE, id));
        } catch (SQLException e) {
            logger.error(String.format(COULD_NOT_DELETE_ENTITY_MESSAGE, id), e);
            throw new DAOException(String.format(COULD_NOT_DELETE_ENTITY_MESSAGE, id), e);
        }
    }

    protected List<T> findEntities(String sql) {
        logger.trace(String.format(TRYING_TO_FIND_ENTITIES_BY_SQL_MESSAGE, sql));
        final List<T> foundEntities = new ArrayList<>();
        try (Connection connection = ConnectionPool.getConnectionPool().takeFreeConnection();
             Statement findAllStatement = connection.createStatement();
             ResultSet resultSet = findAllStatement.executeQuery(sql)) {
            while (resultSet.next()) {
                final T foundEntity = mapResultSet(resultSet);
                foundEntities.add(foundEntity);
            }
            logger.info(String.format(ENTITIES_BY_SQL_WAS_FOUND_MESSAGE, sql));
            return foundEntities;
        } catch (SQLException e) {
            logger.error(String.format(COULD_NOT_FIND_ENTITIES_BY_SQL_MESSAGE, sql), e);
            throw new DAOException(String.format(COULD_NOT_FIND_ENTITIES_BY_SQL_MESSAGE, sql), e);
        }
    }

    protected List<T> findPreparedEntities(String preparedSql, SQLConsumer<PreparedStatement> preparedStatementConsumer) {
        logger.trace(String.format(TRYING_TO_FIND_ENTITIES_BY_PREPARED_SQL_MESSAGE, preparedSql));
        final List<T> foundEntities = new ArrayList<>();
        try (Connection connection = ConnectionPool.getConnectionPool().takeFreeConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(preparedSql)) {
            preparedStatementConsumer.accept(preparedStatement);
            final ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                final T foundEntity = mapResultSet(resultSet);
                foundEntities.add(foundEntity);
            }
            logger.info(String.format(ENTITIES_BY_PREPARED_SQL_WAS_FOUND_MESSAGE, preparedSql));
            return foundEntities;
        } catch (SQLException e) {
            logger.error(String.format(COULD_NOT_FIND_ENTITIES_BY_PREPARED_SQL_MESSAGE, preparedSql), e);
            throw new DAOException(String.format(COULD_NOT_FIND_ENTITIES_BY_PREPARED_SQL_MESSAGE, preparedSql), e);
        }
    }

    protected abstract T mapResultSet(ResultSet result) throws SQLException;

    protected abstract void setSavePrepareStatementValues(T entity, PreparedStatement savePreparedStatement) throws SQLException;

    protected abstract void setUpdatePreparedStatementValues(T entity, PreparedStatement updatePreparedStatement) throws SQLException;
}
