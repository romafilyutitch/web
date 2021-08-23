package by.epam.jwd.web.service;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;

import java.util.List;
import java.util.Optional;

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
     * Finds Book which names are like passed name.
     * @param name of those books that hase name like passed
     * @return collection of books which names matches with the passed one.
     */
    List<Book> findWhereNameLike(String name);

    /**
     * Finds book by passed name.
     * @param name to book which need to be found by name
     * @return not empty optional book if there is book with passed name
     * or empty optional book otherwise
     */
    Optional<Book> findByName(String name);

    /**
     * Finds books by passed genre.
     *
     * @param genre those books need to be found.
     * @return collection of books that have passed genre.
     */
    List<Book> findByGenre(Genre genre);

    /**
     * Performs sort of passed books by their names
     * @param books books that need to be sorted
     * @return sorted books by name
     */
    List<Book> sortByName(List<Book> books);

    /**
     * Performs sort of passed books by their likes amount in descending order
     * @param books that need to be sorted
     * @return sorted books.
     */
    List<Book> sortByLikes(List<Book> books);

    /**
     * Performs sort of passed books by their comments amount in descending order
     * @param books that need to be sorted
     * @return sorted books.
     */
    List<Book> sortByComments(List<Book> books);

    /**
     * Returns book service interface implementation instance.
     * @return book service instance
     */
    static BookService getInstance() {
        return SimpleBookService.getInstance();
    }

}
