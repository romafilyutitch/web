package by.epam.jwd.web.dao.api;



import by.epam.jwd.web.model.DbEntity;
import by.epam.jwd.web.service.api.Service;

import java.util.List;
import java.util.Optional;

/**
 * Data access object base interface for dao layer.
 * Contains all basic dao methods and used by {@link Service}
 * interface implementations. Data access object is used to map entities data to database table and otherwise.
 * Throws {@link by.epam.jwd.web.exception.DAOException} when exception in dao layer occurs
 * @author roma0
 * @version 1.0
 * @since 1.0
 * @param <T> entities that dao used to map entities
 *           data to database table structure
 * @see Service
 * @see by.epam.jwd.web.exception.DAOException
 * @sse "Data access object pattern"
 */
public interface Dao<T extends DbEntity> {
    /**
     * Saves entity to database and assign database generated id to entity
     * @throws by.epam.jwd.web.exception.DAOException when exception in dao layer occurs
     * @param entity entity that need to be saved in database
     * @return saved entity with assigned id
     */
    T save(T entity);

    /**
     * Finds all entities from database table.
     * @throws by.epam.jwd.web.exception.DAOException when exception in dao layer occurs
     * @return all entities list from database table
     */
    List<T> findAll();

    /**
     * Find saved entity by id. Returns {@link Optional} instance that is
     * not empty when entity with passed is is present in database table end
     * empty when there is no entity with passed id
     * @throws by.epam.jwd.web.exception.DAOException when exception in dao layer occurs
     * @param id entity id that need to find
     * @return presented or not entity with specified id
     */
    Optional<T> findById(Long id);

    /**
     * Finds page from database table to implement pagination.
     * Uses limit sql statement to entities in page form.
     * @throws by.epam.jwd.web.exception.DAOException when exception in dao layer occurs
     * @param pageNumber number of needed entities page
     * @return found entities page collection
     */
    List<T> findPage(int pageNumber);

    /**
     * Returns saved entities amount at the moment. Used when need to
     * do entities pagination by services
     * @throws by.epam.jwd.web.exception.DAOException when exception in dao layer occurs
     * @return amount of saved entities
     */
    int getRowsAmount();

    /**
     * Returns saved entities pages amount at the moment. Used when need to
     * do entities pagination by services.
     * @return amount of saved entities pages
     */
    int getPagesAmount();

    /**
     * Performs entity update and returns updated entity
     * @throws by.epam.jwd.web.exception.DAOException when exception in dao layer occurs
     * @param entity entity that need to update in database table
     * @return updated entity
     */
    T update(T entity);

    /**
     * Deletes saved entity that contains specified id from database table
     * @throws by.epam.jwd.web.exception.DAOException when exception in dao layer occurs
     * @param id saved entity id
     */
    void delete(Long id);

}
