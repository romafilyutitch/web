package by.epam.jwd.web.connectionPool;

import java.sql.Connection;

/**
 * Connection Pool interface manages retrieving and returning connections.
 * It's need to invoke {@code init()} method to make initialization of connection pool
 * and invoke @{code destroy()} method to stop connection pool work
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public interface ConnectionPool {

    /**
     * Returns free connection from connection pool.
     * @throws IllegalStateException when connection pool is not initialized.
     * @throws ConnectionActionException when thread that takes connection is interrupted.
     * @return free connection
     */
    Connection takeFreeConnection();

    /**
     * Puts used connection pool to connection pool
     * @throws IllegalStateException when connection pool is not initialized.
     * @throws ConnectionActionException when thread that returns connection is interrupted.
     * @param connection used connection that needs to
     *                   be returned to connection pool
     */
    void returnTakenConnection(Connection connection);

    /**
     * Performs connection pool initialization.
     * Method is invoked when connection pool is not running
     * @throws ConnectionPoolInitializationException when connection pool
     * cannot be initialized
     * @throws IllegalStateException when connection pool already initialized
     */
    void init() throws ConnectionPoolInitializationException;

    /**
     * Performs connection pool stop.
     * Method is invoked when connection pool is running.
     * @exception IllegalStateException when connection pool already destroyed
     */
    void destroy();

    /**
     * Uses singleton pattern to return
     * connection pool implementation instance
     * @return connection pool instance
     */
    static ConnectionPool getConnectionPool() {
        return OrdinaryConnectionPool.getInstance();
    }
}
