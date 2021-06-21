package by.epam.jwd.web.connectionPool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConnectionPoolProperties {
    private static final Logger logger = LogManager.getLogger(ConnectionPoolProperties.class);

    private ConnectionPoolProperties() {}

    public static ConnectionPoolProperties getInstance() {
        return Singleton.INSTANCE;
    }

    public Properties getConnectionPoolData() {
        final Properties connectionsProperties = new Properties();
        final String fileName = "connectionProperties.properties";
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(fileName)) {
            connectionsProperties.load(in);
        } catch (IOException e) {
            logger.error("Properties was not found", e);
        }
        return connectionsProperties;
    }

    private static class Singleton {
        private static final ConnectionPoolProperties INSTANCE = new ConnectionPoolProperties();
    }
}
