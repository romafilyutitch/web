package by.epam.jwd.web.service;

import by.epam.jwd.web.dao.CommentDao;
import by.epam.jwd.web.dao.DAOFactory;
import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Comment;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

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

    public static SimpleCommentService getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public List<Comment> findByBook(Book book) {
        final List<Comment> foundBookComments = commentDao.findByBook(book);
        logger.info(String.format(COMMENTS_BY_BOOK_WAS_FOUND_MESSAGE, book.getName(), foundBookComments.size()));
        return foundBookComments;
    }

    @Override
    public List<Comment> findAll() {
        final List<Comment> allComments = commentDao.findAll();
        logger.info(String.format(ALL_COMMENTS_WAS_FOUND, allComments.size()));
        return allComments;
    }

    @Override
    public List<Comment> findPage(int currentPage) {
        final List<Comment> commentsPage;
        if (currentPage < 1) {
            commentsPage = commentDao.findPage(1);
        } else if (currentPage >= commentDao.getPagesAmount()) {
            commentsPage = commentDao.findPage(getPagesAmount());
        } else {
            commentsPage = commentDao.findPage(currentPage);
        }
        logger.info(String.format(COMMENTS_PAGE_WAS_FOUND_MESSAGE, currentPage, commentsPage.size()));
        return commentsPage;
    }

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

    @Override
    public Comment save(Comment entity) {
        final Comment savedComment = commentDao.save(entity);
        logger.info(String.format(COMMENT_WAS_SAVED_MESSAGE, savedComment));
        return savedComment;
    }

    @Override
    public void delete(Long entityId) {
        commentDao.delete(entityId);
        logger.info(String.format(COMMENT_WAS_DELETED_MESSAGE, entityId));
    }

    @Override
    public int getPagesAmount() {
        return commentDao.getPagesAmount();
    }

    private static class Singleton {
        private static final SimpleCommentService INSTANCE = new SimpleCommentService();
    }
}
