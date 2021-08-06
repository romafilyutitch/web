package by.epam.jwd.web.service.impl;

import by.epam.jwd.web.dao.CommentDao;
import by.epam.jwd.web.dao.DAOFactory;
import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Comment;
import by.epam.jwd.web.service.CommentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for comment service interface.
 * Makes all operations related to comment.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class SimpleCommentService implements CommentService {
    private static final Logger logger = LogManager.getLogger(SimpleCommentService.class);

    private static final String COMMENTS_BY_BOOK_WAS_FOUND_MESSAGE = "Comments by book %s was found size = %d";
    private static final String ALL_COMMENTS_WAS_FOUND = "All comments was found size = %d";
    private static final String COMMENTS_PAGE_WAS_FOUND_MESSAGE = "Comments page number %d was found size = %d";
    private static final String SAVED_COMMENT_WAS_NOT_FOUND_BY_ID_MESSAGE = "Saved comment was not found by id %d";
    private static final String SAVED_COMMENT_WAS_FOUND_BY_ID = "Saved comment was found by id %d %s";
    private static final String COMMENT_WAS_SAVED_MESSAGE = "Comment was saved %s ";
    private static final String COMMENT_WAS_DELETED_MESSAGE = "Comment with id %d was deleted";

    private final CommentDao commentDao = DAOFactory.getInstance().getCommentDao();


    private SimpleCommentService() {}

    /**
     * Gets single class instance from nested class.
     * @return class instance.
     */
    public static SimpleCommentService getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Finds comments that have passed book.
     * @param book which comments need to be found.
     * @return comments that have passed book collection.
     */
    @Override
    public List<Comment> findByBook(Book book) {
        final List<Comment> foundBookComments = commentDao.findByBook(book);
        logger.info(String.format(COMMENTS_BY_BOOK_WAS_FOUND_MESSAGE, book.getName(), foundBookComments.size()));
        return foundBookComments;
    }

    /**
     * Finds and returns result of find all comments.
     * @return all found comments collection.
     */
    @Override
    public List<Comment> findAll() {
        final List<Comment> allComments = commentDao.findAll();
        logger.info(String.format(ALL_COMMENTS_WAS_FOUND, allComments.size()));
        return allComments;
    }

    /**
     * Finds saved comments that on passed page.
     * @throws IllegalArgumentException if passed page number is negative
     * of page number is greater then pages amount.
     * @param currentPage number entities page that need to be found.
     * @return comments on passed page collection.
     */
    @Override
    public List<Comment> findPage(int currentPage) {
        if (currentPage <= 0 || currentPage > getPagesAmount()) {
            throw new IllegalArgumentException();
        }
        final List<Comment> commentsPage = commentDao.findPage(currentPage);
        logger.info(String.format(COMMENTS_PAGE_WAS_FOUND_MESSAGE, currentPage, commentsPage.size()));
        return commentsPage;
    }

    /**
     * Finds and returns result of find saved comment that has passed id.
     * @throws ServiceException when saved comment cannot be found by id.
     * @param entityId id of found entity.
     * @return found saved comment that has passed id.
     */
    @Override
    public Comment findById(Long entityId) {
        final Optional<Comment> optionalComment = commentDao.findById(entityId);
        if (!optionalComment.isPresent()) {
            logger.error(String.format(SAVED_COMMENT_WAS_NOT_FOUND_BY_ID_MESSAGE, entityId));
            throw new ServiceException(String.format(SAVED_COMMENT_WAS_NOT_FOUND_BY_ID_MESSAGE, entityId));
        }
        final Comment foundComment = optionalComment.get();
        logger.info(String.format(SAVED_COMMENT_WAS_FOUND_BY_ID, entityId, foundComment));
        return foundComment;
    }

    /**
     * Make save comment and assigns generated id to saved comment.
     * @param entity entity instance that need to be saved.
     * @return saved comment with assigned id.
     */
    @Override
    public Comment save(Comment entity) {
        final Comment savedComment = commentDao.save(entity);
        logger.info(String.format(COMMENT_WAS_SAVED_MESSAGE, savedComment));
        return savedComment;
    }

    /**
     * Deletes saved user that has passed id.
     * @param entityId entity id that need to be deleted.
     */
    @Override
    public void delete(Long entityId) {
        commentDao.delete(entityId);
        logger.info(String.format(COMMENT_WAS_DELETED_MESSAGE, entityId));
    }

    /**
     * Returns current saved comments pages amount.
     * @return saved comments pages amount.
     */
    @Override
    public int getPagesAmount() {
        return commentDao.getPagesAmount();
    }

    /**
     * Nested class that encapsulates single {@link SimpleCommentService} instance.
     * Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final SimpleCommentService INSTANCE = new SimpleCommentService();
    }
}