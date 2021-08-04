package by.epam.jwd.web.dao;

import by.epam.jwd.web.dao.mysql.MySQLDAOFactory;

/**
 * Data access object factory interface.
 * Used for abstract factory pattern implementation.
 * @see "Abstract factory implementation"
 */
public interface DAOFactory {

    /**
     * Returns {@link UserDao} instance for use in services.
     * @return {@link UserDao} instance.
     */
    UserDao getUserDao();

    /**
     * Returns {@link BookDao} instance for use in services.
     * @return {@link UserDao} instance.
     */
    BookDao getBookDao();

    /**
     * Returns {@link OrderDao} instance for use in services.
     * @return {@link UserDao} instance.
     */
    OrderDao getOrderDao();

    /**
     * Returns {@link AuthorDao} instance for use in services.
     * @return {@link AuthorDao} instance
     */
    AuthorDao getAuthorDao();

    /**
     * Returns {@link SubscriptionDao} instance for use in services.
     * @return {@link SubscriptionDao} instance.
     */
    SubscriptionDao getSubscriptionDao();

    /**
     * Returns {@link CommentDao} instance for user in services.
     * @return {@link CommentDao} instance
     */
    CommentDao getCommentDao();

    /**
     * Returned {@link LikeDao} instance for user in services.
     * @return {@link LikeDao} instance.
     */
    LikeDao getLikeDao();

    /**
     * Returns factory implementation instance
     * @return implementation instance
     */
    static DAOFactory getInstance() {
        return MySQLDAOFactory.getInstance();
    }
}
