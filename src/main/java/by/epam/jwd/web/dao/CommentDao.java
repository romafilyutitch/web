package by.epam.jwd.web.dao;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Comment;

import java.util.List;

/**
 * Comment entity data access object for dao layer. Extends {@link Dao} base interface
 * @see "Data access object pattern"
 */
public interface CommentDao extends Dao<Comment> {
    /**
     * Finds and returns result of find comments by specified book.
     * @throws by.epam.jwd.web.exception.DAOException when exception in dao layer occurs
     * @param book passed book that has comments
     * @return specified book comments collection
     */
    List<Comment> findByBook(Book book);
}
