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

/**
 * Base abstract implementation of Data access object pattern. Abstract class
 * does all basic dao operations and uses Template method pattern to make actions
 * based of implementations.
 * Gives from implementations base sql statements (select, insert, update, delete) and makes
 * additional sql statements based of them like find by id or select by limit.
 *
 * @param <T> Database entities with implementation will work. Extends {@link DbEntity} interface
 */
public abstract class AbstractDao<T extends DbEntity> implements Dao<T> {
    private static final Logger logger = LogManager.getLogger(AbstractDao.class);

    private static final String GENERATED_KEY_COLUMN = "GENERATED_KEY";
    private static final String SAVED_ENTITY_WAS_NOT_FOUND_BY_ID_MESSAGE = "Saved entity was not found by id %d %s";
    private static final String SAVE_STATEMENT_EXCEPTION_MESSAGE = "Save sql statement execute exception";
    private static final String FIND_ALL_STATEMENT_EXCEPTION_MESSAGE = "Find all sql statement execution exception";
    private static final String FIND_BY_ID_STATEMENT_EXCEPTION_MESSAGE = "Find by id sql statement execution exception";
    private static final String UPDATE_STATEMENT_EXCEPTION_MESSAGE = "Updated sql statement execution exception";
    private static final String DELETE_STATEMENT_EXCEPTION_MESSAGE = "Delete sql statement execution exception";
    private static final String GET_ROWS_AMOUNT_STATEMENT_EXCEPTION_MESSAGE = "Get rows amount sql statement execution exception";
    private static final String PREPARED_FIND_SQL_EXCEPTION_MESSAGE = "Find prepared sql execution exception";

    private static final String FIND_BY_ID_SQL_TEMPLATE = "%s where %s.id = ?";
    private static final String RECORDS_AMOUNT_SQL_TEMPLATE = "select count(*) from %s";

    private static final int RECORDS_PER_PAGE = 5;

    private final String findAllSql;
    private final String findPageSql;
    private final String findByIdSql;
    private final String deleteSql;
    private final String saveSql;
    private final String updateSql;
    private final String countSql;

    /**
     * Abstract class constructor
     *
     * @param tableName  name of table to which need make queries
     * @param findAllSql SQL statement to find all records from table
     * @param saveSql    SQL statement to save entity in table
     * @param updateSql  SQL statement to update saved entity in table
     * @param deleteSql  SQL statement to delete entity in table
     */
    public AbstractDao(String tableName, String findAllSql, String findPageSql, String saveSql, String updateSql, String deleteSql) {
        this.findAllSql = findAllSql;
        this.findPageSql = findPageSql;
        this.saveSql = saveSql;
        this.updateSql = updateSql;
        this.deleteSql = deleteSql;
        this.findByIdSql = String.format(FIND_BY_ID_SQL_TEMPLATE, findAllSql, tableName);
        this.countSql = String.format(RECORDS_AMOUNT_SQL_TEMPLATE, tableName);
    }

    /**
     * Saves entity in table and assigns id to saved entity
     * Implemented as transaction. Uses template method pattern on which
     * subclasses choose how to set values to prepared statement
     * depending on saved entity type.
     *
     * @param entity entity that need to be saved in database
     * @return saved entity with assigned id
     * @throws DAOException when {@link SQLException} occurs.
     */
    @Override
    public T save(T entity) {
        long savedEntityId;
        try (Connection connection = ConnectionPool.getConnectionPool().takeFreeConnection();
             PreparedStatement saveStatement = connection.prepareStatement(saveSql, Statement.RETURN_GENERATED_KEYS)) {
            setSavePrepareStatementValues(entity, saveStatement);
            saveStatement.executeUpdate();
            ResultSet generatedKeyResultSet = saveStatement.getGeneratedKeys();
            generatedKeyResultSet.first();
            savedEntityId = generatedKeyResultSet.getLong(GENERATED_KEY_COLUMN);
        } catch (SQLException e) {
            logger.error(SAVE_STATEMENT_EXCEPTION_MESSAGE, e);
            throw new DAOException(e);
        }
        Optional<T> savedEntity = findById(savedEntityId);
        if (!savedEntity.isPresent()) {
            logger.error(String.format(SAVED_ENTITY_WAS_NOT_FOUND_BY_ID_MESSAGE, savedEntityId, entity));
            throw new DAOException(String.format(SAVED_ENTITY_WAS_NOT_FOUND_BY_ID_MESSAGE, savedEntityId, entity));
        } else {
            return savedEntity.get();
        }
    }

    /**
     * Find and returns result of finding all entities from table
     *
     * @return all saved entities from table
     * @throws DAOException when {@link SQLException} occurs
     */
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
            return foundEntities;
        } catch (SQLException e) {
            logger.error(FIND_ALL_STATEMENT_EXCEPTION_MESSAGE, e);
            throw new DAOException(e);
        }
    }

    /**
     * Finds and returns result of find entity by passed id.
     * If id with passed id presents in database table then found id returns
     * and returns empty optional if there is no entity with passed id.
     *
     * @param id entity id that need to find
     * @return found entity if it presents or empty optional otherwise
     * @throws DAOException when {@link SQLException} occurs
     */
    @Override
    public Optional<T> findById(Long id) {
        try (Connection connection = ConnectionPool.getConnectionPool().takeFreeConnection();
             PreparedStatement findByIdStatement = connection.prepareStatement(findByIdSql)) {
            findByIdStatement.setLong(1, id);
            ResultSet resultSet = findByIdStatement.executeQuery();
            if (resultSet.next()) {
                final T foundEntity = mapResultSet(resultSet);
                return Optional.ofNullable(foundEntity);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            logger.error(FIND_BY_ID_STATEMENT_EXCEPTION_MESSAGE, e);
            throw new DAOException(e);
        }
    }

    /**
     * Updates saved entity and returns it.
     * Implemented as transaction.
     * Uses Template method pattern. Subclasses choose how to
     * set values to update depending on updated entity type.
     *
     * @param entity entity that need to update in database table
     * @return updated entity
     * @throws DAOException when {@link SQLException} occurs
     */
    @Override
    public T update(T entity) {
        try (Connection connection = ConnectionPool.getConnectionPool().takeFreeConnection();
             PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
            setUpdatePreparedStatementValues(entity, updateStatement);
            updateStatement.executeUpdate();
            return entity;
        } catch (SQLException e) {
            logger.error(UPDATE_STATEMENT_EXCEPTION_MESSAGE, e);
            throw new DAOException(e);
        }
    }

    /**
     * Delete saved entity with passed id.
     * Implemented as transaction. Uses Template method pattern
     *
     * @param id saved entity id
     * @throws DAOException when {@link SQLException} occurs
     */
    @Override
    public void delete(Long id) {
        try (Connection connection = ConnectionPool.getConnectionPool().takeFreeConnection();
             PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
            deleteStatement.setLong(1, id);
            deleteStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error(DELETE_STATEMENT_EXCEPTION_MESSAGE, e);
            throw new DAOException(e);
        }
    }

    /**
     * Finds and returns result of find entity on passed page.
     * Need for pagination.
     *
     * @param pageNumber number of needed entities page
     * @return entities on passed page
     * @throws DAOException when {@link SQLException} occurs
     */
    @Override
    public List<T> findPage(int pageNumber) {
        final int offset = pageNumber * RECORDS_PER_PAGE - RECORDS_PER_PAGE;
        return findPreparedEntities(findPageSql, preparedStatement -> {
            preparedStatement.setInt(1, offset);
            preparedStatement.setInt(2, RECORDS_PER_PAGE);
        });
    }

    /**
     * Returns amount of saved entities.
     * Need for pagination.
     *
     * @return pages amount
     * @throws DAOException when {@link SQLException} occurs
     */
    @Override
    public int getRowsAmount() {
        try (Connection connection = ConnectionPool.getConnectionPool().takeFreeConnection();
             Statement countStatement = connection.createStatement();
             ResultSet resultSet = countStatement.executeQuery(countSql)) {
            resultSet.next();
            return resultSet.getInt(1);
        } catch (SQLException e) {
            logger.error(GET_ROWS_AMOUNT_STATEMENT_EXCEPTION_MESSAGE, e);
            throw new DAOException(e);
        }
    }

    /**
     * Calculates saved entities pages amount.
     * Need for pagination
     *
     * @return saved entities pages amount
     */
    @Override
    public int getPagesAmount() {
        int numberOfPages = getRowsAmount() / RECORDS_PER_PAGE;
        if (numberOfPages == 0 || numberOfPages % RECORDS_PER_PAGE > 0) {
            numberOfPages++;
        }
        return numberOfPages;
    }

    /**
     * Find entities by prepared sql statement.
     * Used in derived classes. Use template method that derived classes must implement
     *
     * @param preparedSql               prepared sql statement that will be executed
     * @param preparedStatementConsumer prepared consumer interface that implemented in derived classes to define consume
     *                                  operation
     * @return Collection of find entities by passed prepared sql statement
     * @throws DAOException when {@link SQLException} occurs
     */
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
                return foundEntities;
            }
        } catch (SQLException e) {
            logger.error(PREPARED_FIND_SQL_EXCEPTION_MESSAGE, e);
            throw new DAOException(e);
        }
    }

    /**
     * Maps {@link ResultSet} found data to database entity instance. Template method declaration
     *
     * @param result Made during sql find statement execution result.
     * @return Mapped database entity instance.
     * @throws SQLException when exception in database work occurs
     * @see "Template method pattern"
     */
    protected abstract T mapResultSet(ResultSet result) throws SQLException;

    /**
     * Get database entity data and put it to prepared statement in save statements. Template method declaration.
     * Subclasses must define how and what values set to prepared save statement that will be executed in superclass.
     *
     * @param entity                entity that need to save
     * @param savePreparedStatement Made save entity prepared statement
     * @throws SQLException when exception in database work occurs
     * @see "Tempalte method pattern"
     */
    protected abstract void setSavePrepareStatementValues(T entity, PreparedStatement savePreparedStatement) throws SQLException;

    /**
     * Get database entity data and put it to prepared statement in update statements. Template method declaration.
     * Subclasses must define how and what values set to prepared update statement that will be executed in superclass.
     *
     * @param entity                  entity that need to update.
     * @param updatePreparedStatement Made update entity prepared statement
     * @throws SQLException when exception in database work occurs
     * @see "Template method pattern"
     */
    protected abstract void setUpdatePreparedStatementValues(T entity, PreparedStatement updatePreparedStatement) throws SQLException;
}
