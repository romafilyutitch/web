package by.epam.jwd.web.service;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Comment;

import java.util.List;

/**
 * Service interface for service layer that defines {@link Comment} service behavior.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public interface CommentService extends Service<Comment>{
    /**
     * Finds comments that added to passed book.
     * @param book which comments need to be found.
     * @return collection of comments that was added to passed book.
     */
    List<Comment> findByBook(Book book);

    /**
     * Finds comments that added to passed books
     * @param books list of books that contains comments that need to be found
     * @return books comments
     */
    List<Comment> findByBooks(List<Book> books);

    /**
     * Returns Comment service implementation instance
     * @return Comment service instance;
     */
    static CommentService getInstance() {
        return SimpleCommentService.getInstance();
    }
}
