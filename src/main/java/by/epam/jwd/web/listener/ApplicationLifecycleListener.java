package by.epam.jwd.web.listener;

import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.connectionPool.ConnectionPoolInitializationException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

/**
 * Listener to initialize and destroy connection pool.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
@WebListener
public class ApplicationLifecycleListener implements ServletContextListener {
    /**
     * Make connection pool initialization when servlet starts
     * @param sce context event.
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            ConnectionPool.getConnectionPool().init();
        } catch (ConnectionPoolInitializationException e) {
            sce.getServletContext().log("Connection Pool was not initialized", e);
        }
    }

    /**
     * Make connection destroy when servlet stops.
     * @param sce context event.
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConnectionPool.getConnectionPool().destroy();
    }
}
