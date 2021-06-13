package by.epam.jwd.web.connectionPool;

import by.epam.jwd.web.exception.ConnectionPoolInitializationException;

import java.sql.Connection;

public interface ConnectionPool {

    Connection takeFreeConnection();

    void returnTakenConnection(Connection connection);

    void init() throws ConnectionPoolInitializationException;

    void destroy();

    static ConnectionPool getConnectionPool() {
        return OrdinaryConnectionPool.getInstance();
    }
}
