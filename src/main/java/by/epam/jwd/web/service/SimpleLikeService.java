package by.epam.jwd.web.service;

import by.epam.jwd.web.dao.DAOFactory;
import by.epam.jwd.web.dao.LikeDao;
import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Like;
import by.epam.jwd.web.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;

public class SimpleLikeService implements LikeService {
    private static final Logger logger = LogManager.getLogger(SimpleLikeService.class);
    private static final String ALL_LIKES_WAS_FOUND_MESSAGE = "All likes was found";
    private static final String LIKES_PAGE_WAS_FOUND_MESSAGE = "Likes page number %d was found";
    private static final String SAVED_LIKE_WAS_NOT_FOUND_BY_ID_MESSAGE = "Saved like with id %d was not found";
    private static final String SAVED_LIKE_WAS_FOUND_BY_UD_MESSAGE = "Saved like with id %d was found";
    private static final String LIKE_WAS_SAVED_MESSAGE = "Like was saved %s";
    private static final String LIKE_WAS_DELETED_MESSAGE = "Like with id %d was deleted";

    private final LikeDao likeDao = DAOFactory.getInstance().getLikeDao();

    private SimpleLikeService() {}

    public static SimpleLikeService getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public Optional<Like> findByUserAndBook(User user, Book book) {
        return likeDao.findByUserAndBook(user, book);
    }

    @Override
    public List<Like> findAll() {
        final List<Like> allLikes = likeDao.findAll();
        logger.info(ALL_LIKES_WAS_FOUND_MESSAGE);
        return allLikes;
    }

    @Override
    public List<Like> findPage(int currentPage) {
        final List<Like> likesPage;
        if (currentPage < 1) {
            likesPage = likeDao.findPage(1);
        } else if (currentPage >= likeDao.getPagesAmount()) {
            likesPage = likeDao.findPage(getPagesAmount());
        } else {
            likesPage = likeDao.findPage(currentPage);
        }
        logger.info(String.format(LIKES_PAGE_WAS_FOUND_MESSAGE, currentPage));
        return likesPage;
    }

    @Override
    public Like findById(Long entityId) {
        final Optional<Like> optionalLike = likeDao.findById(entityId);
        if (!optionalLike.isPresent()) {
            logger.error(String.format(SAVED_LIKE_WAS_NOT_FOUND_BY_ID_MESSAGE, entityId));
            throw new ServiceException(String.format(SAVED_LIKE_WAS_NOT_FOUND_BY_ID_MESSAGE, entityId));
        }
        final Like foundLike = optionalLike.get();
        logger.info(String.format(SAVED_LIKE_WAS_FOUND_BY_UD_MESSAGE, entityId));
        return foundLike;
    }

    @Override
    public Like register(Like entity) {
        final Like savedLike = likeDao.save(entity);
        logger.info(String.format(LIKE_WAS_SAVED_MESSAGE, savedLike));
        return savedLike;
    }

    @Override
    public void delete(Long entityId) {
        likeDao.delete(entityId);
        logger.info(String.format(LIKE_WAS_DELETED_MESSAGE, entityId));
    }

    @Override
    public int getPagesAmount() {
        return likeDao.getPagesAmount();
    }

    private static class Singleton {
        private static final SimpleLikeService INSTANCE = new SimpleLikeService();
    }
}
