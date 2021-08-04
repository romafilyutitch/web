package by.epam.jwd.web.dao.mysql;

import by.epam.jwd.web.dao.AuthorDao;
import by.epam.jwd.web.dao.BookDao;
import by.epam.jwd.web.dao.CommentDao;
import by.epam.jwd.web.dao.DAOFactory;
import by.epam.jwd.web.dao.LikeDao;
import by.epam.jwd.web.dao.OrderDao;
import by.epam.jwd.web.dao.SubscriptionDao;
import by.epam.jwd.web.dao.UserDao;

/**
 * Data access object mysql implementations factory. Returns doa implementations
 * that related to MySQL database. Implementation of abstract factory pattern.
 * @see "Abstract factory pattern"
 */
public class MySQLDAOFactory implements DAOFactory {
    private MySQLDAOFactory() {
    }

    /**
     * Returns singleton from nested class that has single {@link MySQLDAOFactory} instance.
     * @return class instance
     */
    public static MySQLDAOFactory getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Returns {@link UserDao} for MySQL database implementation.
     * @return {@link UserDao} instance.
     */
    @Override
    public UserDao getUserDao() {
        return MySQLUserDao.getInstance();
    }

    /**
     * Returns {@link BookDao} for MySQL database implementation.
     * @return {@link BookDao} instance.
     */
    @Override
    public BookDao getBookDao() {
        return MySQLBookDao.getInstance();
    }

    /**
     * Returns {@link OrderDao} for MySQL database implementation.
     * @return {@link OrderDao} instance.
     */
    @Override
    public OrderDao getOrderDao() {
        return MySQLOrderDao.getInstance();
    }

    /**
     * Returns {@link AuthorDao} for MySQL database implementation.
     * @return {@link AuthorDao} instance.
     */
    @Override
    public AuthorDao getAuthorDao() {
        return MySQLAuthorDao.getInstance();
    }

    /**
     * Returns {@link CommentDao} for MySQL database implementation.
     * @return {@link CommentDao} instance
     */
    @Override
    public CommentDao getCommentDao() {return MySQLCommentDao.getInstance();}

    /**
     * Returns {@link SubscriptionDao} for MySQL database implementation.
     * @return {@link SubscriptionDao} instance.
     */
    @Override
    public SubscriptionDao getSubscriptionDao() {
        return MySQLSubscriptionDao.getInstance();
    }

    /**
     * Returns {@link LikeDao} for MySQL database implementation.
     * @return {@link LikeDao} instance.
     */
    @Override
    public LikeDao getLikeDao() {
        return MySQLLikeDao.getInstance();
    }

    /**
     * Nested class that encapsulates single {@link MySQLDAOFactory} instance. Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final MySQLDAOFactory INSTANCE = new MySQLDAOFactory();
    }
}
