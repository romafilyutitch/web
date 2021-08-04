package by.epam.jwd.web.dao.mysql;

import by.epam.jwd.web.dao.AuthorDao;
import by.epam.jwd.web.dao.BookDao;
import by.epam.jwd.web.dao.CommentDao;
import by.epam.jwd.web.dao.DAOFactory;
import by.epam.jwd.web.dao.LikeDao;
import by.epam.jwd.web.dao.OrderDao;
import by.epam.jwd.web.dao.SubscriptionDao;
import by.epam.jwd.web.dao.UserDao;

public class MySQLDAOFactory implements DAOFactory {
    private MySQLDAOFactory() {
    }

    public static MySQLDAOFactory getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public UserDao getUserDao() {
        return MySQLUserDao.getInstance();
    }

    @Override
    public BookDao getBookDao() {
        return MySQLBookDao.getInstance();
    }

    @Override
    public OrderDao getOrderDao() {
        return MySQLOrderDao.getInstance();
    }

    @Override
    public AuthorDao getAuthorDao() {
        return MySQLAuthorDao.getInstance();
    }

    @Override
    public CommentDao getCommentDao() {return MySQLCommentDao.getInstance();}

    @Override
    public SubscriptionDao getSubscriptionDao() {
        return MySQLSubscriptionDao.getInstance();
    }

    @Override
    public LikeDao getLikeDao() {
        return MySQLLikeDao.getInstance();
    }

    private static class Singleton {
        private static final MySQLDAOFactory INSTANCE = new MySQLDAOFactory();
    }
}
