package by.epam.jwd.web.service;

import by.epam.jwd.web.model.DbEntity;

import java.util.List;

/**
 * Service interface for service layer
 * that defines service layer behavior.
 * Uses Data access object implementations to retrieve data from
 * database with the help of data access object implementation.
 * @param <T> entity type that service will operate with.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public interface Service<T extends DbEntity> {

    /**
     * Finds All entities.
     * @return all entities collection.
     */
    List<T> findAll();

    /**
     * Finds entities pages based of passed page number.
     * @throws IllegalArgumentException when negative page number passed or
     * passed page number id greater than pages amount
     * @param currentPage number entities page that need to be found.
     * @return found entities page.
     */
    List<T> findPage(int currentPage);

    /**
     * Finds saved entity by passed id.
     * @throws by.epam.jwd.web.exception.ServiceException when
     * saved entity was not found by id.
     * @param entityId id of found entity.
     * @return entity that has passed id.
     */
    T findById(Long entityId);

    /**
     * Saves entity in database and returns entity with assigned id.
     * @param entity entity instance that need to be saved.
     * @return saved entity with assigned id.
     */
    T save(T entity);

    /**
     * Deletes saved entity with passed id.
     * @param entityId entity id that need to be deleted.
     */
    void delete(Long entityId);

    /**
     * Founds current entities pages amount.
     * Needs to make pagination.
     * @return current entities pages amount.
     */
    int getPagesAmount();

}
