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
    private static final String SAVED_ENTITY_WAS_NOT_FOUND_BY_ID_MESSAGE = "Saved entity was not found by id %d %s";
    private static final String ENTITY_WAS_SAVED_MESSAGE = "Entity was saved %s";
    private static final String ENTITY_WAS_UPDATED_MESSAGE = "Entity was updated %s";
    private static final String ENTITY_WAS_DELETED_MESSAGE = "Entity with id %d was deleted";
    private static final String ENTITIES_BY_PREPARED_SQL_WAS_FOUND_MESSAGE = "Entities by prepared sql was found size = %d";
    private static final String ALL_ENTITIES_WAS_FOUND_MESSAGE = "All entities was found size = %d";
    private static final String ENTITIES_ON_PAGE_WAS_FOUND_MESSAGE = "Entities on page %d was found size = %d";
    private static final String SQL_EXCEPTION_HAPPENED_MESSAGE = "Exception happened when sql statement executed";
    private static final String ENTITY_WAS_FOUND_BY_ID_MESSAGE = "Entity was found by id %d %s";
    private static final String ENTITY_WAS_NOT_FOUND_BY_ID_MESSAGE = "Entity was not found by id %d";

    private static final String FIND_BY_ID_SQL_TEMPLATE = "%s where %s.id = ?";
    private static final String FIND_PAGE_SQL_TEMPLATE = "%s limit ?, ?";
    private static final String RECORDS_AMOUNT_SQL_TEMPLATE = "select count(*) from %s";

    private static final int RECORDS_PER_PAGE = 5;

    private final String findAllSql;
    private final String findByIdSql;
    private final String findPageSql;
    private final String deleteSql;
    private final String saveSql;
    private final String updateSql;
    private final String countSql;

    public AbstractDao(String tableName, String findAllSql, String saveSql, String updateSql, String deleteSql) {
        this.findAllSql = findAllSql;
        this.saveSql = saveSql;
        this.updateSql = updateSql;
        this.deleteSql = deleteSql;
        this.findByIdSql = String.format(FIND_BY_ID_SQL_TEMPLATE, this.findAllSql, tableName);
        this.findPageSql = String.format(FIND_PAGE_SQL_TEMPLATE, this.findAllSql);
        this.countSql = String.format(RECORDS_AMOUNT_SQL_TEMPLATE, tableName);
    }

    @Override
    public T save(T entity) {
        long savedEntityId;
        try (Connection connection = ConnectionPool.getConnectionPool().takeFreeConnection();
             PreparedStatement saveStatement = connection.prepareStatement(saveSql, Statement.RETURN_GENERATED_KEYS)) {
            setSavePrepareStatementValues(entity, saveStatement);
            saveStatement.executeUpdate();
            try (ResultSet generatedKeyResultSet = saveStatement.getGeneratedKeys()) {
                generatedKeyResultSet.first();
                savedEntityId = generatedKeyResultSet.getLong(GENERATED_KEY_COLUMN);
            }
        } catch (SQLException e) {
            logger.error(SQL_EXCEPTION_HAPPENED_MESSAGE,e);
            throw new DAOException(e);
        }
        Optional<T> savedEntity = findById(savedEntityId);
        if (!savedEntity.isPresent()) {
            logger.error(String.format(SAVED_ENTITY_WAS_NOT_FOUND_BY_ID_MESSAGE, savedEntityId, entity));
            throw new DAOException(String.format(SAVED_ENTITY_WAS_NOT_FOUND_BY_ID_MESSAGE, savedEntityId, entity));
        } else {
            logger.info(String.format(ENTITY_WAS_SAVED_MESSAGE, savedEntity.get()));
            return savedEntity.get();
        }
    }

    @Override
    public List<T> findAll() {
        final List<T> foundEntities = new ArrayList<>();
        try (Connection connection = ConnectionPool.getConnectionPool().takeFreeConnection();
             Statement findAllStatement = connection.createStatement();
             ResultSet resultSet = findAllStatement.executeQuery(findAllSql)) {
            while (resultSet.next()) {
                final T foundEntity = mapResultSet(resultSet);
                foundEntities.add(foundEntity);
            }
            logger.info(String.format(ALL_ENTITIES_WAS_FOUND_MESSAGE, foundEntities.size()));
            return foundEntities;
        } catch (SQLException e) {
            logger.error(SQL_EXCEPTION_HAPPENED_MESSAGE, e);
            throw new DAOException(e);
        }
    }

    @Override
    public Optional<T> findById(Long id) {
        try (Connection connection = ConnectionPool.getConnectionPool().takeFreeConnection();
             PreparedStatement findByIdStatement = connection.prepareStatement(findByIdSql)) {
            findByIdStatement.setLong(1, id);
            try (ResultSet resultSet = findByIdStatement.executeQuery()) {
                if (resultSet.next()) {
                    final T foundEntity = mapResultSet(resultSet);
                    logger.info(String.format(ENTITY_WAS_FOUND_BY_ID_MESSAGE, id, foundEntity));
                    return Optional.of(foundEntity);
                } else {
                    logger.info(String.format(ENTITY_WAS_NOT_FOUND_BY_ID_MESSAGE, id));
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            logger.error(SQL_EXCEPTION_HAPPENED_MESSAGE, e);
            throw new DAOException(e);
        }
    }

    @Override
    public T update(T entity) {
        try (Connection connection = ConnectionPool.getConnectionPool().takeFreeConnection();
             PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
            setUpdatePreparedStatementValues(entity, updateStatement);
            updateStatement.executeUpdate();
            logger.info(String.format(ENTITY_WAS_UPDATED_MESSAGE, entity));
            return entity;
        } catch (SQLException e) {
            logger.error(SQL_EXCEPTION_HAPPENED_MESSAGE, e);
            throw new DAOException(e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = ConnectionPool.getConnectionPool().takeFreeConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
            deleteStatement.setLong(1, id);
            deleteStatement.executeUpdate();
            logger.info(String.format(ENTITY_WAS_DELETED_MESSAGE, id));
        } catch (SQLException e) {
            logger.error(SQL_EXCEPTION_HAPPENED_MESSAGE, e);
            throw new DAOException(e);
        }
    }

    @Override
    public List<T> findPage(int pageNumber) {
        final int offset = pageNumber * RECORDS_PER_PAGE - RECORDS_PER_PAGE;
        final List<T> entitiesInCurrentPage = findPreparedEntities(findPageSql, preparedStatement -> {
            preparedStatement.setInt(1, offset);
            preparedStatement.setInt(2, RECORDS_PER_PAGE);
        });
        logger.info(String.format(ENTITIES_ON_PAGE_WAS_FOUND_MESSAGE, pageNumber, entitiesInCurrentPage.size()));
        return entitiesInCurrentPage;
    }

    @Override
    public int getRowsAmount() {
        try (Connection connection = ConnectionPool.getConnectionPool().takeFreeConnection();
             Statement countStatement = connection.createStatement();
             ResultSet resultSet = countStatement.executeQuery(countSql)) {
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            logger.error(SQL_EXCEPTION_HAPPENED_MESSAGE, e);
            throw new DAOException(e);
        }
    }

    @Override
    public int getPagesAmount() {
        int numberOfPages = getRowsAmount() / RECORDS_PER_PAGE;
        if (numberOfPages == 0 || numberOfPages % RECORDS_PER_PAGE > 0) {
            numberOfPages++;
        }
        return numberOfPages;
    }

    protected List<T> findPreparedEntities(String preparedSql, SQLConsumer<PreparedStatement> preparedStatementConsumer) {
        final List<T> foundEntities = new ArrayList<>();
        try (Connection connection = ConnectionPool.getConnectionPool().takeFreeConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(preparedSql)) {
            preparedStatementConsumer.accept(preparedStatement);
            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                while (resultSet.next()) {
                    final T foundEntity = mapResultSet(resultSet);
                    foundEntities.add(foundEntity);
                }
                logger.info(String.format(ENTITIES_BY_PREPARED_SQL_WAS_FOUND_MESSAGE, foundEntities.size()));
                return foundEntities;
            }
        } catch (SQLException e) {
            logger.error(SQL_EXCEPTION_HAPPENED_MESSAGE, e);
            throw new DAOException(e);
        }
    }

    protected abstract T mapResultSet(ResultSet result) throws SQLException;

    protected abstract void setSavePrepareStatementValues(T entity, PreparedStatement savePreparedStatement) throws SQLException;

    protected abstract void setUpdatePreparedStatementValues(T entity, PreparedStatement updatePreparedStatement) throws SQLException;
}
