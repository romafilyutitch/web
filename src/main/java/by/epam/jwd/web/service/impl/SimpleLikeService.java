package by.epam.jwd.web.service.impl;

import by.epam.jwd.web.dao.DAOFactory;
import by.epam.jwd.web.dao.LikeDao;
import by.epam.jwd.web.service.ServiceException;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Like;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.api.LikeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

/**
 * Service implementation for like service interface.
 * Makes all operations related to like.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class SimpleLikeService implements LikeService {
    private static final Logger logger = LogManager.getLogger(SimpleLikeService.class);
    private static final String ALL_LIKES_WAS_FOUND_MESSAGE = "All likes was found size = %d";
    private static final String LIKES_PAGE_WAS_FOUND_MESSAGE = "Likes page number %d was found size = %d";
    private static final String SAVED_LIKE_WAS_NOT_FOUND_BY_ID_MESSAGE = "Saved like with id %d was not found";
    private static final String SAVED_LIKE_WAS_FOUND_BY_UD_MESSAGE = "Saved like was found by id %s";
    private static final String LIKE_WAS_SAVED_MESSAGE = "Like was saved %s";
    private static final String LIKE_WAS_DELETED_MESSAGE = "Like was deleted %s";

    private final LikeDao likeDao = DAOFactory.getFactory().getLikeDao();

    private SimpleLikeService() {}

    /**
     * Gets single class instance from nested class.
     * @return class instance.
     */
    public static SimpleLikeService getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Finds like that passed user added to passed book.
     * @param user user that add like to book.
     * @param book book which like was added by user to.
     * @return found like if there is like that passed user added to passed book
     * or empty optional otherwise.
     */
    @Override
    public Optional<Like> findByUserAndBook(User user, Book book) {
        return likeDao.findByUserAndBook(user, book);
    }

    /**
     * Finds and returns result of find all likes.
     * @return all saved likes collections.
     */
    @Override
    public List<Like> findAll() {
        final List<Like> allLikes = likeDao.findAll();
        logger.info(String.format(ALL_LIKES_WAS_FOUND_MESSAGE, allLikes.size()));
        return allLikes;
    }

    /**
     * Founds likes that is on passed page.
     * @throws IllegalArgumentException when passed page number is negative
     * or if passed page number is greater then current pages amount.
     * @param currentPage number entities page that need to be found.
     * @return found likes that is on passed page collection.
     */
    @Override
    public List<Like> findPage(int currentPage) {
        if (currentPage <= 0 || currentPage > getPagesAmount()) {
            throw new IllegalArgumentException();
        }
        final List<Like> likesPage = likeDao.findPage(currentPage);
        logger.info(String.format(LIKES_PAGE_WAS_FOUND_MESSAGE, currentPage, likesPage.size()));
        return likesPage;
    }

    /**
     * Find saved like by id.
     * @throws ServiceException when saved like is not found by id.
     * @param likeId id of found entity.
     * @return found saved like that has passed id.
     */
    @Override
    public Like findById(Long likeId) {
        final Optional<Like> optionalLike = likeDao.findById(likeId);
        if (!optionalLike.isPresent()) {
            logger.error(String.format(SAVED_LIKE_WAS_NOT_FOUND_BY_ID_MESSAGE, likeId));
            throw new ServiceException(String.format(SAVED_LIKE_WAS_NOT_FOUND_BY_ID_MESSAGE, likeId));
        }
        final Like foundLike = optionalLike.get();
        logger.info(String.format(SAVED_LIKE_WAS_FOUND_BY_UD_MESSAGE, foundLike));
        return foundLike;
    }

    /**
     * Make save like and assigns generated id to saved like.
     * @param like that need to be saved.
     * @return saved like that has generated id.
     */
    @Override
    public Like save(Like like) {
        final Like savedLike = likeDao.save(like);
        logger.info(String.format(LIKE_WAS_SAVED_MESSAGE, savedLike));
        return savedLike;
    }

    /**
     * Deletes saved Like.
     * @param like Like that need to be deleted.
     */
    @Override
    public void delete(Like like) {
        likeDao.delete(like.getId());
        logger.info(String.format(LIKE_WAS_DELETED_MESSAGE, like));
    }

    /**
     * Calculates current like pages amount.
     * @return current pages amount.
     */
    @Override
    public int getPagesAmount() {
        return likeDao.getPagesAmount();
    }

    /**
     * Nested class that encapsulates single {@link SimpleLikeService} instance.
     * Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final SimpleLikeService INSTANCE = new SimpleLikeService();
    }
}
