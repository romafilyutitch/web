package by.epam.jwd.web.connectionPool;


import by.epam.jwd.web.properties.ConnectionPoolProperties;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;

class OrdinaryConnectionPool implements ConnectionPool {

    private static final Logger logger = LogManager.getLogger(OrdinaryConnectionPool.class);

    private static final String POOL_IS_NOT_INITIALIZED_MESSAGE = "Pool is not initialized";
    private static final String CONNECTION_IS_NOT_PROXY_CONNECTION_MESSAGE = "Connection is not proxy connection";
    private static final String FAILED_TO_INIT_CONNECTION_POOL_MESSAGE = "failed to make connection pool initialization";
    private static final String NEGATIVE_ARGUMENT_MESSAGE = "negative argument";
    private static final String CONNECTION_IS_NULL_MESSAGE = "Connection is null";
    private static final String RETURNED_CONNECTION_IS_NOT_TAKEN_CONNECTION_MESSAGE = "Returned connection is not taken connection";
    private static final String DRIVERS_REGISTRATION_FAILED_MESSAGE = "Driver registration failed";

    private static final String DATABASE_URL = ConnectionPoolProperties.getUrl();
    private static final String DATABASE_USERNAME = ConnectionPoolProperties.getUserName();
    private static final String DATABASE_PASSWORD = ConnectionPoolProperties.getPassword();
    private static final int MINIMUM_POOL_SIZE = ConnectionPoolProperties.getMinimalPoolSize();
    private static final int MAXIMUM_POOL_SIZE = ConnectionPoolProperties.getMaximalPoolSize();
    private static final int RESIZE_QUANTITY = ConnectionPoolProperties.getResizeQuantity();
    private static final int POOL_RESIZE_CHECK_DELAY_TIME = ConnectionPoolProperties.getPoolResizeTimerTaskCheckDelayTime();
    private static final int POOL_RESIZE_CHECK_PERIOD_TIME = ConnectionPoolProperties.getPoolResizeTimerTaskCheckPeriodTime();
    private static final double RESIZE_FACTOR = ConnectionPoolProperties.getPoolResizeTimerTaskResizeFactor();

    private final BlockingQueue<Connection> freeConnectionsQueue = new ArrayBlockingQueue<>(MAXIMUM_POOL_SIZE);
    private final CopyOnWriteArraySet<Connection> takenConnections = new CopyOnWriteArraySet<>();
    private final AtomicBoolean isInitialized = new AtomicBoolean(false);
    private final Timer timer = new Timer(true);
    private final PoolResizeTimerTask poolResizeTimerTask = new PoolResizeTimerTask();

    private OrdinaryConnectionPool() {}

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
            return freeConnection;
        } catch (InterruptedException e) {
            logger.error(e);
            throw new ConnectionPoolActionException("Cannot take free connection", e);
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
        } catch (InterruptedException e) {
            logger.error(e);
            throw new ConnectionPoolActionException("Cannot return taken connection to connection pool", e);
        }
    }

    @Override
    public void init() throws ConnectionPoolInitializationException {
        if (isInitialized.compareAndSet(false, true)) {
            registerDrivers();
            try {
                addFreeConnectionsToPool(MINIMUM_POOL_SIZE);
                timer.schedule(poolResizeTimerTask, POOL_RESIZE_CHECK_DELAY_TIME, POOL_RESIZE_CHECK_PERIOD_TIME);
            } catch (SQLException | InterruptedException e) {
                logger.error(e);
                isInitialized.set(false);
                deregisterDrivers();
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
        }
    }

    private void addFreeConnectionsToPool(int amountOfAddedConnections) throws SQLException, InterruptedException {
        if (amountOfAddedConnections < 0) {
            throw new IllegalArgumentException(NEGATIVE_ARGUMENT_MESSAGE);
        }
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
        if (connection == null) {
            throw new NullPointerException(CONNECTION_IS_NULL_MESSAGE);
        }
        if (connection instanceof ProxyConnection) {
            if (!takenConnections.contains(connection)) {
                throw new IllegalArgumentException(RETURNED_CONNECTION_IS_NOT_TAKEN_CONNECTION_MESSAGE);
            }
        } else {
            throw new IllegalArgumentException(CONNECTION_IS_NOT_PROXY_CONNECTION_MESSAGE);
        }
    }

    private void closeTakenConnections() {
        for (Connection connection : takenConnections) {
            try {
                ProxyConnection proxyConnection = (ProxyConnection) connection;
                proxyConnection.getConnection().close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
    }

    private void closeFreeConnections() {
        for (Connection connection : freeConnectionsQueue) {
            try {
                ProxyConnection proxyConnection = (ProxyConnection) connection;
                proxyConnection.getConnection().close();
            } catch (SQLException e) {
                logger.error(e);
            }
        }
    }

    private void registerDrivers() throws ConnectionPoolInitializationException {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        } catch (SQLException e) {
            logger.error(e);
            isInitialized.set(false);
            throw new ConnectionPoolInitializationException(DRIVERS_REGISTRATION_FAILED_MESSAGE, e);
        }
    }

    private void deregisterDrivers() {
        Enumeration<Driver> iterator = DriverManager.getDrivers();
        while (iterator.hasMoreElements()) {
            try {
                DriverManager.deregisterDriver(iterator.nextElement());
            } catch (SQLException e) {
                logger.error(e);
            }
        }
    }

    private class PoolResizeTimerTask extends TimerTask {
        @Override
        public void run() {
            logger.info("Free connections = " + freeConnectionsQueue.size() +
                    ", taken connections = " + takenConnections.size() +
                    ", total connections amount = " + (freeConnectionsQueue.size() + takenConnections.size()));
            try {
                if (isNeedToGrowPool()) {
                    logger.info("Action is grow pool");
                    addFreeConnectionsToPool(RESIZE_QUANTITY);
                } else if (isNeedToTrimPool()) {
                    logger.info("Action is trim pool");
                    removeFreeConnectionsFromPool();
                }
            } catch (SQLException | InterruptedException e) {
                logger.error(e);
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
