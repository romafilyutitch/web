package by.epam.jwd.web.dao;

import by.epam.jwd.web.dao.mysql.MySQLDAOFactory;

public interface DAOFactory {

    UserDao getUserDao();

    BookDao getBookDao();

    OrderDao getOrderDao();

    AuthorDao getAuthorDao();

    SubscriptionDao getSubscriptionDao();

    CommentDao getCommentDao();

    LikeDao getLikeDao();

    static DAOFactory getInstance() {
        return MySQLDAOFactory.getInstance();
    }
}
