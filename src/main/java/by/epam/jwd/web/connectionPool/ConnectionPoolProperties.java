package by.epam.jwd.web.connectionPool;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ConnectionPoolProperties {
    private static final Logger logger = LogManager.getLogger(ConnectionPoolProperties.class);

    private ConnectionPoolProperties() {}

    public static Properties getConnectionPoolData() {
        final Properties connectionsProperties = new Properties();
        final String fileName = "src/main/resources/connectionPoolProperties.properties";
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            connectionsProperties.load(reader);
        } catch (IOException e) {
            logger.error("Properties was not found", e);
        }
        return connectionsProperties;
    }
}
