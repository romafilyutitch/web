package by.epam.jwd.web.service;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Like;
import by.epam.jwd.web.model.User;

import java.util.Optional;

/**
 * Service interface for service layer that defines {@link Like} service behavior.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public interface LikeService extends Service<Like> {
    /**
     * Finds like by passed user and book.
     * @param user user that add like to book.
     * @param book book which like was added by user to.
     * @return like in optional if there is like to passed book by passed user
     * or empty optional otherwise.
     */
    Optional<Like> findByUserAndBook(User user, Book book);

    /**
     * Returns like service implementation instanace.
     * @return like service instance
     */
    static LikeService getInstance() {
        return SimpleLikeService.getInstance();
    }
}
