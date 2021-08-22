package by.epam.jwd.web.dao;



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
     *
     * Finds all book whose names are matches to passed name.
     * @throws DAOException when exception in dao layer occurs
     * @param name name of book that need to be found
     * @return List of books whose names are matches with passed name.
     */
    List<Book> findWhereNameLike(String name);

    /**
     * Finds saved book which has passed name.
     * @param name for book that need to be found
     * @return Not empty optional book if there is book with passed name,
     * empty optional book otherwise.
     */
    Optional<Book> findByName(String name);

    /**
     * Finds and returns result of find book that have passed author name.
     * @throws DAOException when exception in dao layer occurs
     * @param authorName name of author book that need to be found
     * @return books that have specified author name collection
     */
    List<Book> findByAuthorName(String authorName);

    /**
     * Finds and returns result of find book that have passed {@link Genre}
     * @throws DAOException when exception in dao layer occurs
     * @param genre genre of book that need to be found
     * @return books that have specified genre collection
     */
    List<Book> findByGenre(Genre genre);
}
