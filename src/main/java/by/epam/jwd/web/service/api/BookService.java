package by.epam.jwd.web.service.api;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;

import java.util.List;

/**
 * Service interface for service layer that defines {@link Book} service behavior.
 *
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public interface BookService extends Service<Book> {

    /**
     * Adds one copy of passed book.
     *
     * @param book which need to add one copy.
     */
    void addOneCopy(Book book);

    /**
     * Removed one cope of passed book.
     *
     * @param book which need to add one copy.
     */
    void removeOneCopy(Book book);

    /**
     * Finds books by passed name.
     *
     * @param name that book need to be found.
     * @return collection of books which names matches with the passed one.
     */
    List<Book> findByName(String name);

    /**
     * Finds books by passed genre.
     *
     * @param genre those books need to be found.
     * @return collection of books that have passed genre.
     */
    List<Book> findByGenre(Genre genre);

}
