package by.epam.jwd.web.dao;

public interface DAOFactory {

    UserDao getUserDao();

    BookDao getBookDao();

    OrderDao getOrderDao();

    AuthorDao getAuthorDao();

    SubscriptionDao getSubscriptionDao();

    static DAOFactory getInstance() {
        return MySQLDAOFactory.getInstance();
    }
}
