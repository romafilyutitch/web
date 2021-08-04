package by.epam.jwd.web.connectionPool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Class contains connection pool properties from properties file.
 * Used for perform right connection pool initialization for {@link OrdinaryConnectionPool}.
 * @author roma0
 * version 1.0
 * since 1.0
 */
public class ConnectionPoolProperties {
    private static final Logger logger = LogManager.getLogger(ConnectionPoolProperties.class);
    private static final String PROPERTIES_FILE_PATH = "connectionProperties.properties";
    private static final String PROPERTIES_FILE_WAS_NOT_FOUND_MESSAGE = "Connection pool properties file was not found";
    private static final String DATABASE_URL_KEY = "url";
    private static final String DATABASE_USER_KEY = "user";
    private static final String DATABASE_USER_PASSWORD_KEY = "password";
    private static final String MIN_POOL_SIZE_KEY = "minPoolSize";
    private static final String MAX_POOL_SIZE_KEY = "maxPoolSize";
    private static final String POOL_RESIZE_QUANTITY_KEY = "resizeQuantity";
    private static final String CHECK_POOL_RESIZE_DELAY_TIME_KEY = "checkResizeDelayTime";
    private static final String CHECK_POOL_RESIZE_PERIOD_TIME_KEY = "checkResizePeriodTime";
    private static final String POOL_RESIZE_FACTOR_KEY = "resizeFactor";

    private final Properties connectionPoolProperties = new Properties();

    private ConnectionPoolProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE_PATH)) {
            connectionPoolProperties.load(input);
        } catch (IOException e) {
            logger.info(PROPERTIES_FILE_WAS_NOT_FOUND_MESSAGE, e);
            throw new RuntimeException(PROPERTIES_FILE_WAS_NOT_FOUND_MESSAGE, e);
        }
    }

    /**
     * Uses singleton pattern to return class instance.
     * Singleton with nested class is used
     * @return class instance
     */
    public static ConnectionPoolProperties getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Finds database url value in properties file for connection pool initialization
     * @return database URL value from properties file
     */
    public String getDatabaseUrl() {
        return connectionPoolProperties.getProperty(DATABASE_URL_KEY);
    }

    /**
     * Finds database user value in properties file for connection pool initialization
     * @return database user's name value from properties file
     */
    public String getUser() {
        return connectionPoolProperties.getProperty(DATABASE_USER_KEY);
    }

    /**
     * Finds database user password in properties file for connection pool initialization
     * @return database user's password value from properties file
     */
    public String getPassword() {
        return connectionPoolProperties.getProperty(DATABASE_USER_PASSWORD_KEY);
    }

    /**
     * Finds connection pool minimum size value in file for connection pool initialization
     * @return minimum connection pool size value from properties file
     */
    public int getMinPoolSize() {
        return Integer.parseInt(connectionPoolProperties.getProperty(MIN_POOL_SIZE_KEY));
    }

    /**
     * Finds connection pool maximum size value for connection pool initialization
     * @return maximum connection pool size value from properties file
     */
    public int getMaxPoolSize() {
        return Integer.parseInt(connectionPoolProperties.getProperty(MAX_POOL_SIZE_KEY));
    }

    /**
     * Finds connection pool resize connection quantity value for connection pool resize timer task.
     * Resize quantity is need to know how many free connections need to add
     * when there is little free connections in connection pool left
     * and how many free connections need to remove and close when
     * there are many free unused connections in connections pool left when resize time comes
     * @return connection pool resize quantity value from properties file
     */
    public int getResizeQuantity() {
        return Integer.parseInt(connectionPoolProperties.getProperty(POOL_RESIZE_QUANTITY_KEY));
    }

    /**
     * Finds resize delay time for resize task when connection pool check resize time comes.
     * Resize delay time is need to know delay period of start pool check resize timer task.
     * @return delay time for check resize connection pool timer task from properties file
     */
    public int getCheckResizeDelayTime() {
        return Integer.parseInt(connectionPoolProperties.getProperty(CHECK_POOL_RESIZE_DELAY_TIME_KEY));
    }

    /**
     * Finds resize period time for connection pool check resize timer task.
     * @return period time for check resize connection pool timer task from properties file
     */
    public int getCheckResizePeriodTime() {
        return Integer.parseInt(connectionPoolProperties.getProperty(CHECK_POOL_RESIZE_PERIOD_TIME_KEY));
    }

    /**
     * Finds connection pool resize factor for connection pool check resize timer task.
     * Resize factor is used to calculate whether it's time to perform resize action with
     * connection pool or not when check resize time comes.
     * @return resize factor for connection pool check resize timer task from properties file
     */
    public double getResizeFactor() {
        return Double.parseDouble(connectionPoolProperties.getProperty(POOL_RESIZE_FACTOR_KEY));
    }

    /**
     * Nested class that encapsulates singleton instance.
     * Singleton pattern with nested class is used
     */
    private static class Singleton {
        private static final ConnectionPoolProperties INSTANCE = new ConnectionPoolProperties();
    }
}
