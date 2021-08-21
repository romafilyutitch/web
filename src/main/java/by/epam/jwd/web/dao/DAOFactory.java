package by.epam.jwd.web.dao;

/**
 * Data access object factory interface.
 * Used for abstract factory pattern implementation.
 * @author roma0
 * @version 1.0
 * @since 1.0
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
    static DAOFactory getFactory() {
        return MySQLDAOFactory.getInstance();
    }
}
