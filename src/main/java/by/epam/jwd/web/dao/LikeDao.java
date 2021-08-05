package by.epam.jwd.web.dao;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Like;
import by.epam.jwd.web.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Like entity data access object interface for dao layer. extends {@link Dao} base interface
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public interface LikeDao extends Dao<Like> {
    /**
     * Finds and returns find like result by passed user and book.
     * Returns {@link Like} instance if there is like for specified book by specified user in
     * database table or empty optional otherwise
     * @throws by.epam.jwd.web.exception.DAOException when exception in dao layer occurs.
     * @param user {@link User} instance that added like to {@link Book} instance
     * @param book {@link Book} instance that may have like by {@link User} instance
     * @return {@link Like} instance if there is like to specified {@link Book} by specified {@link User}
     */
    Optional<Like> findByUserAndBook(User user, Book book);
}
