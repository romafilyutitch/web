package by.epam.jwd.web.dao;

public class MySQLDaoFactory implements DaoFactory {
    private MySQLDaoFactory() {
    }

    public static MySQLDaoFactory getInstance() {
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
    public GenreDao getGenreDao() {
        return MySQLGenreDao.getInstance();
    }

    @Override
    public RoleDao getRoleDao() {
        return MySQLRoleDao.getInstance();
    }

    @Override
    public SubscriptionDao getSubscriptionDao() {
        return MySQLSubscriptionDao.getInstance();
    }

    private static class Singleton {
        private static final MySQLDaoFactory INSTANCE = new MySQLDaoFactory();
    }
}
