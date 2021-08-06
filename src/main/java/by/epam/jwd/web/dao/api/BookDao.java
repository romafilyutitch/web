package by.epam.jwd.web.dao.api;



import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;

import java.util.List;
import java.util.Optional;

/**
 * Book Data access object interface for dao layer. Extends {@link Dao} base interface.
 * @author roma0
 * @version 1.0
 * @since 1.0
 * @see "Data access object pattern"
 *
 */
public interface BookDao extends Dao<Book> {
    /**
     * Finds and returns result of find book that has passed name.
     * @throws by.epam.jwd.web.exception.DAOException when exception in dao layer occurs
     * @param name name of book that need to be found
     * @return {@link Book} instance when there is book with passed name
     *                              in database table or empty optional when
     *                              there is no {@link Book} that has specified name
     */
    Optional<Book> findByName(String name);

    /**
     * Finds and returns result of find book that have passed author name.
     * @throws by.epam.jwd.web.exception.DAOException when exception in dao layer occurs
     * @param authorName name of author book that need to be found
     * @return books that have specified author name collection
     */
    List<Book> findByAuthorName(String authorName);

    /**
     * Finds and returns result of find book that have passed {@link Genre}
     * @throws by.epam.jwd.web.exception.DAOException when exception in dao layer occurs
     * @param genre genre of book that need to be found
     * @return books that have specified genre collection
     */
    List<Book> findByGenre(Genre genre);
}
