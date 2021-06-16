package by.epam.jwd.web.dao;

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
    public SubscriptionDao getSubscriptionDao() {
        return MySQLSubscriptionDao.getInstance();
    }

    private static class Singleton {
        private static final MySQLDAOFactory INSTANCE = new MySQLDAOFactory();
    }
}
