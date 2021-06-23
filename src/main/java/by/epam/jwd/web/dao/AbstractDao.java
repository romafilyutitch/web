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
import java.util.StringJoiner;

public abstract class AbstractDao<T extends DbEntity> implements Dao<T> {
    private static final Logger logger = LogManager.getLogger(AbstractDao.class);

    private static final String GENERATED_KEY_COLUMN = "GENERATED_KEY";
    private static final String SAVED_ENTITY_WAS_NOT_FOUND_BY_ID_MESSAGE = "Saved entity was not found by id %s";
    private static final String ENTITY_WAS_SAVED_MESSAGE = "Entity %s was saved";
    private static final String COULD_NOT_SAVE_ENTITY_MESSAGE = "Could not save entity %s ";
    private static final String ENTITY_WAS_UPDATED_MESSAGE = "Entity %s was updated";
    private static final String COULD_NOT_UPDATE_ENTITY_MESSAGE = "Could not update entity %s ";
    private static final String ENTITY_WAS_DELETED_MESSAGE = "Entity with id %d was deleted";
    private static final String COULD_NOT_DELETE_ENTITY_MESSAGE = "Could not delete entity with id %d";
    private static final String ENTITIES_BY_PREPARED_SQL_WAS_FOUND_MESSAGE = "Entities by prepared sql was found %s";
    private static final String COULD_NOT_FIND_ENTITIES_BY_PREPARED_SQL_MESSAGE = "Could not find entities by prepared sql was found %s";
    private static final String ALL_ENTITIES_WAS_FOUND_MESSAGE = "All entities was found";
    private static final String ALL_ENTITIES_WAS_NOT_FOUND_MESSAGE = "All entities was not found";
    private static final String ENTITIES_ON_PAGE_WAS_FOUND_MESSAGE = "Entities on page %d was found";
    private static final String COULD_NOT_FIND_NUMBER_OF_ROWS_MESSAGE = "Could not find number of rows";

    private static final String FIND_ALL_SQL_TEMPLATE = "select %s from %s";
    protected static final String FIND_BY_COLUMN_SQL_TEMPLATE = "select %s from %s where %s = ?";
    private static final String FIND_PAGE_SQL_TEMPLATE = "select %s from %s limit ?, ?";
    private static final String SAVE_SQL_TEMPLATE = "insert into %s %s values %s";
    private static final String UPDATE_SQL_TEMPLATE = "update %s set %s where %s = ?";
    private static final String DELETE_SQL_TEMPLATE = "delete from %s where %s = ?";
    private static final String COUNT_SQL_TEMPLATE = "select count(*) from %s";

    private static final int RECORDS_PER_PAGE = 5;

    private final String findAllSql;
    private final String findByIdSql;
    private final String findPageSql;
    private final String deleteSql;
    private final String saveSql;
    private final String updateSql;
    private final String countSql;

    public AbstractDao(String tableName, List<String> columns) {
        final String id = columns.get(0);
        final String findAllColumns = buildFindAllColumns(columns);
        final String saveColumns = buildSaveColumns(columns);
        final String questionMarks = buildQuestionMarks(columns);
        final String updateColumns = buildUpdateColumns(columns);
        this.findAllSql = String.format(FIND_ALL_SQL_TEMPLATE, findAllColumns, tableName);
        this.findByIdSql = String.format(FIND_BY_COLUMN_SQL_TEMPLATE, findAllColumns, tableName, id);
        this.findPageSql = String.format(FIND_PAGE_SQL_TEMPLATE, findAllColumns, tableName);
        this.saveSql = String.format(SAVE_SQL_TEMPLATE, tableName, saveColumns, questionMarks);
        this.updateSql = String.format(UPDATE_SQL_TEMPLATE, tableName, updateColumns, id);
        this.deleteSql = String.format(DELETE_SQL_TEMPLATE, tableName, id);
        this.countSql = String.format(COUNT_SQL_TEMPLATE, tableName);
    }

    private String buildUpdateColumns(List<String> columns) {
        final StringJoiner joiner = new StringJoiner(" = ?,",""," = ?");
        final List<String> subList = columns.subList(1, columns.size());
        subList.forEach(joiner::add);
        return joiner.toString();
    }

    private String buildFindAllColumns(List<String> columns) {
        final StringJoiner joiner = new StringJoiner(",");
        columns.forEach(joiner::add);
        return joiner.toString();
    }

    private String buildSaveColumns(List<String> columns) {
        final StringJoiner joiner = new StringJoiner(",", "(", ")");
        final List<String> subList = columns.subList(1, columns.size());
        subList.forEach(joiner::add);
        return joiner.toString();
    }

    private String buildQuestionMarks(List<String> columns) {
        final StringJoiner joiner = new StringJoiner(",", "(", ")");
        final List<String> subList = columns.subList(1, columns.size());
        subList.forEach(column -> joiner.add("?"));
        return joiner.toString();
    }

    @Override
    public T save(T entity) {
        long savedEntityId;
        try (Connection connection = ConnectionPool.getConnectionPool().takeFreeConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement saveStatement = connection.prepareStatement(saveSql, Statement.RETURN_GENERATED_KEYS)) {
                setSavePrepareStatementValues(entity, saveStatement);
                saveStatement.executeUpdate();
                connection.commit();
                try (ResultSet generatedKeyResultSet = saveStatement.getGeneratedKeys()) {
                    generatedKeyResultSet.first();
                    savedEntityId = generatedKeyResultSet.getLong(GENERATED_KEY_COLUMN);
                }
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            logger.error(String.format(COULD_NOT_SAVE_ENTITY_MESSAGE, entity), e);
            throw new DAOException(String.format(COULD_NOT_SAVE_ENTITY_MESSAGE, entity), e);
        }
        Optional<T> savedEntity = findById(savedEntityId);
        if (!savedEntity.isPresent()) {
            logger.error(String.format(SAVED_ENTITY_WAS_NOT_FOUND_BY_ID_MESSAGE, entity));
            throw new DAOException(String.format(SAVED_ENTITY_WAS_NOT_FOUND_BY_ID_MESSAGE, entity));
        } else {
            logger.info(String.format(ENTITY_WAS_SAVED_MESSAGE, entity));
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
            logger.info(ALL_ENTITIES_WAS_FOUND_MESSAGE);
            return foundEntities;
        } catch (SQLException e) {
            logger.error(ALL_ENTITIES_WAS_NOT_FOUND_MESSAGE, e);
            throw new DAOException(ALL_ENTITIES_WAS_FOUND_MESSAGE, e);
        }
    }

    @Override
    public Optional<T> findById(Long id) {
        final List<T> foundEntities = findPreparedEntities(findByIdSql, statement -> statement.setLong(1, id));
        return foundEntities.stream().findAny();
    }

    @Override
    public T update(T entity) {
        try (Connection connection = ConnectionPool.getConnectionPool().takeFreeConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement updateStatement = connection.prepareStatement(updateSql)) {
                setUpdatePreparedStatementValues(entity, updateStatement);
                updateStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
            logger.info(String.format(ENTITY_WAS_UPDATED_MESSAGE, entity));
            return entity;
        } catch (SQLException e) {
            logger.error(String.format(COULD_NOT_UPDATE_ENTITY_MESSAGE, entity), e);
            throw new DAOException(String.format(COULD_NOT_UPDATE_ENTITY_MESSAGE, entity), e);
        }
    }

    @Override
    public void delete(Long id) {
        try (Connection connection = ConnectionPool.getConnectionPool().takeFreeConnection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement deleteStatement = connection.prepareStatement(deleteSql)) {
                deleteStatement.setLong(1, id);
                deleteStatement.executeUpdate();
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
            logger.info(String.format(ENTITY_WAS_DELETED_MESSAGE, id));
        } catch (SQLException e) {
            logger.error(String.format(COULD_NOT_DELETE_ENTITY_MESSAGE, id), e);
            throw new DAOException(String.format(COULD_NOT_DELETE_ENTITY_MESSAGE, id), e);
        }
    }

    @Override
    public List<T> findPage(int pageNumber) {
        final int offset = pageNumber * RECORDS_PER_PAGE - RECORDS_PER_PAGE;
        final List<T> entitiesInCurrentPage = findPreparedEntities(findPageSql, preparedStatement -> {
            preparedStatement.setInt(1, offset);
            preparedStatement.setInt(2, RECORDS_PER_PAGE);
        });
        logger.info(String.format(ENTITIES_ON_PAGE_WAS_FOUND_MESSAGE, pageNumber));
        return entitiesInCurrentPage;
    }

    @Override
    public int getRowsAmount() {
        try (Connection connection = ConnectionPool.getConnectionPool().takeFreeConnection();
        Statement countStatement = connection.createStatement();
        ResultSet resultSet = countStatement.executeQuery(countSql)) {
        resultSet.next();
            return resultSet.getInt(1);
        }  catch (SQLException e) {
            logger.error(COULD_NOT_FIND_NUMBER_OF_ROWS_MESSAGE, e);
            throw new DAOException(COULD_NOT_FIND_NUMBER_OF_ROWS_MESSAGE, e);
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
