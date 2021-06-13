package by.epam.jwd.web.listener;

import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.exception.ConnectionPoolInitializationException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ApplicationLifecycleListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            ConnectionPool.getConnectionPool().init();
        } catch (ConnectionPoolInitializationException e) {
            sce.getServletContext().log("Connection Pool was not initialized", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ConnectionPool.getConnectionPool().destroy();
    }
}
