package by.epam.jwd.web.dao.api;


import by.epam.jwd.web.model.Author;

import java.util.Optional;

/**
 * Author entity dao interface for dao layer. extends {@link Dao} base interface.
 * @author roma0
 * @version 1.0
 * @since 1.0
 * @see "Data access object pattern"
 */
public interface AuthorDao extends Dao<Author> {
    /**
     * Finds and returns author that have specified name find result from database table.
     * Returns optional entity that contains entity if there is author with specified
     * name and empty optional when there is not any author entity with specified name
     * in database table
     * @throws by.epam.jwd.web.exception.DAOException when exception in dao layer occurs
     * @param authorName name of that need to find
     * @return author with specified id or empty optional when there is no author with
     * specified name in database table
     */
    Optional<Author> getByName(String authorName);
}
