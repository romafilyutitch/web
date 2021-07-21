package by.epam.jwd.web.connectionPool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

public class ConnectionPoolProperties {
    private static final Logger logger = LogManager.getLogger(ConnectionPoolProperties.class);
    private static final String PROPERTIES_FILE_PATH = "connectionProperties.properties";
    private static final String PROPERTIES_FILE_WAS_NOT_FOUND_MESSAGE = "Connection pool properties file was not found";

    private final Properties connectionPoolProperties = new Properties();

    private ConnectionPoolProperties() {
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE_PATH)) {
            connectionPoolProperties.load(input);
        } catch (IOException e) {
            logger.info(PROPERTIES_FILE_WAS_NOT_FOUND_MESSAGE, e);
            throw new RuntimeException(PROPERTIES_FILE_WAS_NOT_FOUND_MESSAGE, e);
        }
    }

    public static ConnectionPoolProperties getInstance() {
        return Singleton.INSTANCE;
    }


    public String getDatabaseUrl() {
        return connectionPoolProperties.getProperty("url");
    }

    public String getUser() {
        return connectionPoolProperties.getProperty("user");
    }

    public String getPassword() {
        return connectionPoolProperties.getProperty("password");
    }

    public int getMinPoolSize() {
        return Integer.parseInt(connectionPoolProperties.getProperty("minPoolSize"));
    }

    public int getMaxPoolSize() {
        return Integer.parseInt(connectionPoolProperties.getProperty("maxPoolSize"));
    }

    public int getResizeQuantity() {
        return Integer.parseInt(connectionPoolProperties.getProperty("resizeQuantity"));
    }

    public int getCheckResizeDelayTime() {
        return Integer.parseInt(connectionPoolProperties.getProperty("checkResizeDelayTime"));
    }

    public int getCheckResizePeriodTime() {
        return Integer.parseInt(connectionPoolProperties.getProperty("checkResizePeriodTime"));
    }

    public double getResizeFactor() {
        return Double.parseDouble(connectionPoolProperties.getProperty("resizeFactor"));
    }

    private static class Singleton {
        private static final ConnectionPoolProperties INSTANCE = new ConnectionPoolProperties();
    }
}
