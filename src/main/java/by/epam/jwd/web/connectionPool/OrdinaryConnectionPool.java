package by.epam.jwd.web.connectionPool;


import by.epam.jwd.web.exception.ConnectionPoolActionException;
import by.epam.jwd.web.exception.ConnectionPoolInitializationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;

class OrdinaryConnectionPool implements ConnectionPool {

    private static final Logger logger = LogManager.getLogger(OrdinaryConnectionPool.class);

    private static final String POOL_IS_NOT_INITIALIZED_MESSAGE = "Pool is not initialized";
    private static final String FAILED_TO_INIT_CONNECTION_POOL_MESSAGE = "failed to make connection pool initialization";
    private static final String RETURNED_CONNECTION_IS_NOT_TAKEN_CONNECTION_MESSAGE = "Returned connection is not taken connection";
    private static final String DRIVERS_REGISTRATION_FAILED_MESSAGE = "Driver registration failed";
    private static final String COULD_NOT_TAKE_FREE_CONNECTION_MESSAGE = "Could not take free connection";
    private static final String COULD_NOT_RETURN_CONNECTION_MESSAGE = "Could not return taken connection to connection pool";
    private static final String FREE_CONNECTION_WAS_TAKEN_MESSAGE = "Free connection was taken %s";
    private static final String TAKEN_CONNECTION_WAS_RETURNED_MESSAGE = "Taken connection was returned %s";
    private static final String POOL_WAS_INITIALIZED_MESSAGE = "Connection pool was initialized";
    private static final String POOL_WAS_NOT_INITIALIZED_MESSAGE = "Connection pool was not initialized";
    private static final String POOL_WAS_DESTROYED_MESSAGE = "Connection pool was destroyed";
    private static final String COULD_NOT_CLOSE_TAKEN_CONNECTION_MESSAGE = "Could not close taken connection";
    private static final String COULD_NOT_CLOSE_FREE_CONNECTION_MESSAGE = "Could not close free connection";
    private static final String COULD_NOT_REGISTER_DRIVER_MESSAGE = "Could not register driver";
    private static final String COULD_NOT_DEREGISTER_DRIVER_MESSAGE = "Could not deregister driver";

    private static final Properties POOL_PROPERTIES = ConnectionPoolProperties.getInstance().getConnectionPoolData();

    private static final String DATABASE_URL = POOL_PROPERTIES.getProperty("url");
    private static final String DATABASE_USERNAME = POOL_PROPERTIES.getProperty("user");
    private static final String DATABASE_PASSWORD = POOL_PROPERTIES.getProperty("password");
    private static final int MINIMUM_POOL_SIZE = Integer.parseInt(POOL_PROPERTIES.getProperty("minPoolSize"));
    private static final int MAXIMUM_POOL_SIZE = Integer.parseInt(POOL_PROPERTIES.getProperty("maxPoolSize"));
    private static final int RESIZE_QUANTITY = Integer.parseInt(POOL_PROPERTIES.getProperty("resizeQuantity"));
    private static final int POOL_RESIZE_CHECK_DELAY_TIME = Integer.parseInt(POOL_PROPERTIES.getProperty("checkResizeDelayTime"));
    private static final int POOL_RESIZE_CHECK_PERIOD_TIME = Integer.parseInt(POOL_PROPERTIES.getProperty("checkResizePeriodTime"));
    private static final double RESIZE_FACTOR = Double.parseDouble(POOL_PROPERTIES.getProperty("resizeFactor"));

    private final BlockingQueue<Connection> freeConnectionsQueue = new ArrayBlockingQueue<>(MAXIMUM_POOL_SIZE);
    private final CopyOnWriteArraySet<Connection> takenConnections = new CopyOnWriteArraySet<>();
    private final AtomicBoolean isInitialized = new AtomicBoolean(false);
    private final Timer timer = new Timer(true);
    private final PoolResizeTimerTask poolResizeTimerTask = new PoolResizeTimerTask();

    private OrdinaryConnectionPool() {

    }

    public static OrdinaryConnectionPool getInstance() {
        return ConnectionPoolSingleton.INSTANCE;
    }

    @Override
    public Connection takeFreeConnection() {
        if (!isInitialized.get()) {
            throw new IllegalStateException(POOL_IS_NOT_INITIALIZED_MESSAGE);
        }
        try {
            Connection freeConnection = freeConnectionsQueue.take();
            takenConnections.add(freeConnection);
            logger.info(String.format(FREE_CONNECTION_WAS_TAKEN_MESSAGE, freeConnection));
            return freeConnection;
        } catch (InterruptedException e) {
            logger.error(COULD_NOT_TAKE_FREE_CONNECTION_MESSAGE, e);
            throw new ConnectionPoolActionException(COULD_NOT_TAKE_FREE_CONNECTION_MESSAGE, e);
        }
    }

    @Override
    public void returnTakenConnection(Connection connection) {
        if (!isInitialized.get()) {
            throw new IllegalStateException(POOL_IS_NOT_INITIALIZED_MESSAGE);
        }
        checkReturnedConnection(connection);
        try {
            freeConnectionsQueue.put(connection);
            takenConnections.remove(connection);
            logger.info(String.format(TAKEN_CONNECTION_WAS_RETURNED_MESSAGE, connection));
        } catch (InterruptedException e) {
            logger.error(COULD_NOT_RETURN_CONNECTION_MESSAGE, e);
            throw new ConnectionPoolActionException(COULD_NOT_RETURN_CONNECTION_MESSAGE, e);
        }
    }

    @Override
    public void init() throws ConnectionPoolInitializationException {
        if (isInitialized.compareAndSet(false, true)) {
            registerDrivers();
            try {
                addFreeConnectionsToPool(MINIMUM_POOL_SIZE);
                timer.schedule(poolResizeTimerTask, POOL_RESIZE_CHECK_DELAY_TIME, POOL_RESIZE_CHECK_PERIOD_TIME);
                logger.info(POOL_WAS_INITIALIZED_MESSAGE);
            } catch (SQLException | InterruptedException e) {
                isInitialized.set(false);
                deregisterDrivers();
                logger.error(POOL_WAS_NOT_INITIALIZED_MESSAGE, e);
                throw new ConnectionPoolInitializationException(FAILED_TO_INIT_CONNECTION_POOL_MESSAGE, e);
            }
        }
    }

    @Override
    public void destroy() {
        if (isInitialized.compareAndSet(true, false)) {
            timer.cancel();
            closeFreeConnections();
            freeConnectionsQueue.clear();
            closeTakenConnections();
            takenConnections.clear();
            deregisterDrivers();
            logger.info(POOL_WAS_DESTROYED_MESSAGE);
        }
    }

    private void addFreeConnectionsToPool(int amountOfAddedConnections) throws SQLException, InterruptedException {
        for (int i = 0; i < amountOfAddedConnections; i++) {
            final Connection connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USERNAME, DATABASE_PASSWORD);
            final ProxyConnection proxyConnection = new ProxyConnection(connection);
            freeConnectionsQueue.put(proxyConnection);
        }
    }

    private void removeFreeConnectionsFromPool() throws SQLException, InterruptedException {
        for (int i = 0; i < RESIZE_QUANTITY; i++) {
            ProxyConnection proxyConnection = (ProxyConnection) freeConnectionsQueue.take();
            proxyConnection.getConnection().close();
        }
    }

    private void checkReturnedConnection(Connection connection) {
        if (!takenConnections.contains(connection)) {
            throw new IllegalArgumentException(RETURNED_CONNECTION_IS_NOT_TAKEN_CONNECTION_MESSAGE);
        }
    }

    private void closeTakenConnections() {
        for (Connection connection : takenConnections) {
            try {
                ProxyConnection proxyConnection = (ProxyConnection) connection;
                proxyConnection.getConnection().close();
            } catch (SQLException e) {
                logger.error(COULD_NOT_CLOSE_TAKEN_CONNECTION_MESSAGE, e);
            }
        }
    }

    private void closeFreeConnections() {
        for (Connection connection : freeConnectionsQueue) {
            try {
                ProxyConnection proxyConnection = (ProxyConnection) connection;
                proxyConnection.getConnection().close();
            } catch (SQLException e) {
                logger.error(COULD_NOT_CLOSE_FREE_CONNECTION_MESSAGE, e);
            }
        }
    }

    private void registerDrivers() throws ConnectionPoolInitializationException {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        } catch (SQLException e) {
            isInitialized.set(false);
            logger.error(COULD_NOT_REGISTER_DRIVER_MESSAGE, e);
            throw new ConnectionPoolInitializationException(DRIVERS_REGISTRATION_FAILED_MESSAGE, e);
        }
    }

    private void deregisterDrivers() {
        Enumeration<Driver> iterator = DriverManager.getDrivers();
        while (iterator.hasMoreElements()) {
            try {
                DriverManager.deregisterDriver(iterator.nextElement());
            } catch (SQLException e) {
                logger.error(COULD_NOT_DEREGISTER_DRIVER_MESSAGE, e);
            }
        }
    }

    private class PoolResizeTimerTask extends TimerTask {

        private static final String RESIZE_INFO = "Check connection pool state. Free connections = %d, taken connections = %d, total connections amount = %d";
        private static final String COULD_NOT_PERFORM_RESIZE_POOL_MESSAGE = "Could not perform resize connection pool action";
        private static final String FREE_CONNECTIONS_WAS_ADDED_MESSAGE = "New %d free connections was added to connection pool";
        private static final String FREE_CONNECTIONS_WAS_REMOVED_MESSAGE = "%d free connections was removed from connection pool";

        @Override
        public void run() {
            logger.info(String.format(RESIZE_INFO, freeConnectionsQueue.size(), takenConnections.size(), freeConnectionsQueue.size() + takenConnections.size()));
            try {
                if (isNeedToGrowPool()) {
                    addFreeConnectionsToPool(RESIZE_QUANTITY);
                    logger.info(String.format(FREE_CONNECTIONS_WAS_ADDED_MESSAGE, RESIZE_QUANTITY));
                } else if (isNeedToTrimPool()) {
                    removeFreeConnectionsFromPool();
                    logger.info(String.format(FREE_CONNECTIONS_WAS_REMOVED_MESSAGE, RESIZE_QUANTITY));
                }
            } catch (SQLException | InterruptedException e) {
                logger.error(COULD_NOT_PERFORM_RESIZE_POOL_MESSAGE, e);
            }
        }

        private boolean isNeedToTrimPool() {
            return takenConnections.size() < (freeConnectionsQueue.size() + takenConnections.size()) * RESIZE_FACTOR
                    && freeConnectionsQueue.size() > MINIMUM_POOL_SIZE;
        }

        private boolean isNeedToGrowPool() {
            return freeConnectionsQueue.size() < (takenConnections.size() + freeConnectionsQueue.size()) * RESIZE_FACTOR
                    && freeConnectionsQueue.size() + takenConnections.size() < MAXIMUM_POOL_SIZE;
        }
    }

    private static class ConnectionPoolSingleton {
        private static final OrdinaryConnectionPool INSTANCE = new OrdinaryConnectionPool();
    }
}
