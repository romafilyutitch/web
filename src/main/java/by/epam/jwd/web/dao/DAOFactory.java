package by.epam.jwd.web.dao;

public interface DAOFactory {

    UserDao getUserDao();

    BookDao getBookDao();

    OrderDao getOrderDao();

    AuthorDao getAuthorDao();

    SubscriptionDao getSubscriptionDao();

    CommentDao getCommentDao();

    static DAOFactory getInstance() {
        return MySQLDAOFactory.getInstance();
    }
}
