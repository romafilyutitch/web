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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;

class OrdinaryConnectionPool implements ConnectionPool {

    private static final Logger logger = LogManager.getLogger(OrdinaryConnectionPool.class);

    private static final String POOL_IS_NOT_INITIALIZED_MESSAGE = "Pool is not initialized";
    private static final String RETURNED_CONNECTION_IS_NOT_TAKEN_CONNECTION_MESSAGE = "Returned connection is not taken connection";
    private static final String DRIVERS_REGISTRATION_FAILED_MESSAGE = "Driver registration failed";
    private static final String POOL_WAS_INITIALIZED_MESSAGE = "Connection pool was initialized";
    private static final String POOL_WAS_NOT_INITIALIZED_MESSAGE = "Connection pool was not initialized";
    private static final String POOL_WAS_DESTROYED_MESSAGE = "Connection pool was destroyed";
    private static final String CONNECTION_POOL_INITIALIZED_MESSAGE = "Connection pool is already initialized";
    private static final String CONNECTION_POOL_DESTROYED_MESSAGE = "Connection Pool is already destroyed";
    private static final String THREAD_WAS_INTERRUPTED_MESSAGE = "Thread was interrupted";
    private static final String SQL_EXCEPTION_HAPPENED_MESSAGE = "SQLException happened";

    private final ConnectionPoolProperties properties = ConnectionPoolProperties.getInstance();
    private final BlockingQueue<Connection> freeConnectionsQueue = new ArrayBlockingQueue<>(properties.getMaxPoolSize());
    private final CopyOnWriteArraySet<Connection> takenConnections = new CopyOnWriteArraySet<>();
    private final AtomicBoolean isInitialized = new AtomicBoolean(false);

    private OrdinaryConnectionPool() {
        final Timer timer = new Timer(true);
        final PoolResizeTimerTask poolResizeTimerTask = new PoolResizeTimerTask();
        timer.schedule(poolResizeTimerTask, properties.getCheckResizeDelayTime(), properties.getCheckResizePeriodTime());
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
            return freeConnection;
        } catch (InterruptedException e) {
            logger.error(THREAD_WAS_INTERRUPTED_MESSAGE, e);
            throw new ConnectionPoolActionException(e);
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
            logger.error(THREAD_WAS_INTERRUPTED_MESSAGE, e);
            throw new ConnectionPoolActionException(e);
        }
    }

    @Override
    public void init() throws ConnectionPoolInitializationException {
        if (isInitialized.compareAndSet(false, true)) {
            registerDrivers();
            try {
                addFreeConnectionsToPool(properties.getMinPoolSize());
                logger.info(POOL_WAS_INITIALIZED_MESSAGE);
            } catch (SQLException | InterruptedException e) {
                isInitialized.set(false);
                deregisterDrivers();
                logger.error(POOL_WAS_NOT_INITIALIZED_MESSAGE, e);
                throw new ConnectionPoolInitializationException(e);
            }
        } else {
            throw new IllegalStateException(CONNECTION_POOL_INITIALIZED_MESSAGE);
        }
    }

    @Override
    public void destroy() {
        if (isInitialized.compareAndSet(true, false)) {
            closeFreeConnections();
            freeConnectionsQueue.clear();
            closeTakenConnections();
            takenConnections.clear();
            deregisterDrivers();
            logger.info(POOL_WAS_DESTROYED_MESSAGE);
        } else {
            throw new IllegalStateException(CONNECTION_POOL_DESTROYED_MESSAGE);
        }
    }

    private void addFreeConnectionsToPool(int amountOfAddedConnections) throws SQLException, InterruptedException {
        for (int i = 0; i < amountOfAddedConnections; i++) {
            final Connection connection = DriverManager.getConnection(properties.getDatabaseUrl(), properties.getUser(), properties.getPassword());
            final ProxyConnection proxyConnection = new ProxyConnection(connection);
            freeConnectionsQueue.put(proxyConnection);
        }
    }

    private void removeFreeConnectionsFromPool(int amountOfRemovedConnections) throws SQLException, InterruptedException {
        for (int i = 0; i < amountOfRemovedConnections; i++) {
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
                logger.error(SQL_EXCEPTION_HAPPENED_MESSAGE, e);
            }
        }
    }

    private void closeFreeConnections() {
        for (Connection connection : freeConnectionsQueue) {
            try {
                ProxyConnection proxyConnection = (ProxyConnection) connection;
                proxyConnection.getConnection().close();
            } catch (SQLException e) {
                logger.error(SQL_EXCEPTION_HAPPENED_MESSAGE, e);
            }
        }
    }

    private void registerDrivers() throws ConnectionPoolInitializationException {
        try {
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());
        } catch (SQLException e) {
            isInitialized.set(false);
            logger.error(SQL_EXCEPTION_HAPPENED_MESSAGE, e);
            throw new ConnectionPoolInitializationException(DRIVERS_REGISTRATION_FAILED_MESSAGE, e);
        }
    }

    private void deregisterDrivers() {
        Enumeration<Driver> iterator = DriverManager.getDrivers();
        while (iterator.hasMoreElements()) {
            try {
                DriverManager.deregisterDriver(iterator.nextElement());
            } catch (SQLException e) {
                logger.error(SQL_EXCEPTION_HAPPENED_MESSAGE, e);
            }
        }
    }

    private class PoolResizeTimerTask extends TimerTask {

        private static final String RESIZE_INFO = "Check connection pool state. Free connections = %d, taken connections = %d, total connections amount = %d";
        private static final String FREE_CONNECTIONS_WAS_ADDED_MESSAGE = "New %d free connections was added to connection pool";
        private static final String FREE_CONNECTIONS_WAS_REMOVED_MESSAGE = "%d free connections was removed from connection pool";

        @Override
        public void run() {
            logger.info(String.format(RESIZE_INFO, freeConnectionsQueue.size(), takenConnections.size(), freeConnectionsQueue.size() + takenConnections.size()));
            try {
                if (isNeedToGrowPool()) {
                    addFreeConnectionsToPool(properties.getResizeQuantity());
                    logger.info(String.format(FREE_CONNECTIONS_WAS_ADDED_MESSAGE, properties.getResizeQuantity()));
                } else if (isNeedToTrimPool()) {
                    removeFreeConnectionsFromPool(properties.getResizeQuantity());
                    logger.info(String.format(FREE_CONNECTIONS_WAS_REMOVED_MESSAGE, properties.getResizeQuantity()));
                }
            } catch (SQLException e) {
                logger.error(SQL_EXCEPTION_HAPPENED_MESSAGE, e);
            } catch (InterruptedException e) {
                logger.error(THREAD_WAS_INTERRUPTED_MESSAGE, e);
            }
        }

        private boolean isNeedToTrimPool() {
            return takenConnections.size() < (freeConnectionsQueue.size() + takenConnections.size()) * properties.getResizeFactor()
                    && freeConnectionsQueue.size() > properties.getMinPoolSize();
        }

        private boolean isNeedToGrowPool() {
            return freeConnectionsQueue.size() < (takenConnections.size() + freeConnectionsQueue.size()) * properties.getResizeFactor()
                    && freeConnectionsQueue.size() + takenConnections.size() < properties.getMaxPoolSize();
        }
    }

    private static class ConnectionPoolSingleton {
        private static final OrdinaryConnectionPool INSTANCE = new OrdinaryConnectionPool();
    }
}
