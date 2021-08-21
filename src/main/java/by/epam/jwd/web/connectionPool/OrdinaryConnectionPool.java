package by.epam.jwd.web.connectionPool;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Connection pool class that encapsulates free database connections for
 * connections economy and performance.
 *
 * Uses {@link ConnectionPoolProperties} to get initialization values to make
 * initialization.
 *
 * Uses {@link BlockingQueue} collection for free connections encapsulates.
 * That collection is used for work in multi thread environment for avoid problems
 * that occurs in multi thread environment. That collection allows to blocks thread that
 * puts used connection when collection is full and blocks thread that gets free connection
 * from it when there is no free connection in it. Also uses {@link CopyOnWriteArraySet} to
 * collect taken connections to check if real taken connection is returned. Collection is
 * usable for multi thread environment because copies its content when thread puts connection
 * in it thant avoid {@link java.util.ConcurrentModificationException}
 *
 * Class uses {@link Timer} and {@link TimerTask} to make connection pool resize check action
 * in time based on connection properties values. {@link Timer} is daemon thread and used to
 * performs connection pool resize check actions in time. Action parameters may configurable
 * through setting parameters in properties file. {@link TimerTask} is used to calculate whether
 * it need to add free connections or remove unused free connections when check connection
 * pool resize time comes base on Timer.
 * @author roma0
 * @version 1.0
 * @since 1.0
 * @see ConnectionPoolProperties
 * @see BlockingQueue
 * @see CopyOnWriteArraySet
 * @see Timer
 * @see TimerTask
 */
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
    private final Set<Connection> takenConnections = new CopyOnWriteArraySet<>();
    private final AtomicBoolean isInitialized = new AtomicBoolean(false);
    private final Timer checkPoolResizeTimer = new Timer(true);
    private PoolResizeTimerTask checkPoolResizeTimerTask;

    private OrdinaryConnectionPool() {}

    /**
     * Uses singleton pattern to return connection pool class instance
     * @return class instance
     */
    public static OrdinaryConnectionPool getInstance() {
        return ConnectionPoolSingleton.INSTANCE;
    }

    /**
     * Return free connection from connection pool.
     * Blocks current thread when there is no free connection until free connections appears in connection pool.
     * Free connection is added to pool when other thread puts free connection to connection pool
     * or check connection pool resize time comes and and puts new free connections to connection pool
     * @throws IllegalStateException when connection pool is not initialized
     * @throws ConnectionActionException when current thread  is interrupted
     * @return free database connection
     */
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
            throw new ConnectionActionException(e);
        }
    }

    /**
     * Puts used database connection to connection pool.
     * Blocks current thread when connection pool is full until free space for connection appeared.
     * Free space for connection pool may be in connection pool where other thread take free connection from pool
     * or resize connection pool check time comes and removes unused free connections from pool.
     * @throws IllegalStateException when connection pool is not initialized
     * @throws ConnectionActionException when current thread is interrupted
     * @param connection used database connection that need to be put in connection pool
     */
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
            throw new ConnectionActionException(e);
        }
    }

    /**
     * Performs connection pool initialization and puts initial free database connections to connection pool.
     * @throws ConnectionPoolInitializationException when it's not possible to perform initialization
     * because of database SQL exceptions or driver registration problems
     * @throws IllegalStateException when connection pool is already initialized
     */
    @Override
    public void init() throws ConnectionPoolInitializationException {
        if (isInitialized.compareAndSet(false, true)) {
            registerDrivers();
            try {
                addFreeConnectionsToPool(properties.getMinPoolSize());
                checkPoolResizeTimerTask = new PoolResizeTimerTask();
                checkPoolResizeTimer.schedule(checkPoolResizeTimerTask, properties.getCheckResizeDelayTime(), properties.getCheckResizePeriodTime());
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

    /**
     * Perform connection pool stop. Closes free and taken connections and deregister drivers
     * @throws IllegalStateException when connection pool already destroyed
     */
    @Override
    public void destroy() {
        if (isInitialized.compareAndSet(true, false)) {
            checkPoolResizeTimerTask.cancel();
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

    /**
     * Check connection pool resize action class. Performs resize action if resize condition is {@code true} or
     * not otherwise. Condition is based on connection pool properties resize factor value.
     * When check connection pool resize time comes resize pool condition is evaluated and if result id {@code true}
     * task add new free database connections to pool when there is not enough free connections in connection pool presents
     * or closes and removes free database connections from pool when it's to many connections in it or
     * do not any actions when condition evaluation result is {@code false}
     * @author roma0
     * version 1.0
     * since 1.0
     */
    private class PoolResizeTimerTask extends TimerTask {

        private static final String RESIZE_INFO = "Check connection pool state. Free connections = %d, taken connections = %d, total connections amount = %d";
        private static final String FREE_CONNECTIONS_WAS_ADDED_MESSAGE = "New %d free connections was added to connection pool";
        private static final String FREE_CONNECTIONS_WAS_REMOVED_MESSAGE = "%d free connections was removed from connection pool";

        /**
         * Puts free connections to connection pool if check connection pool resize
         * time comes and need to grow pool or removes unused free connection from pool
         * when check connection pool resize time comes and need to remove free unused connections or
         * do not anything when check connection pool resize time comes and resize conditions are both {@code false}
         */
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

    /**
     * Nested class to implement Singleton pattern. encapsulates single {@link OrdinaryConnectionPool} instance
     * @see "Singleton pattern"
     */
    private static class ConnectionPoolSingleton {
        private static final OrdinaryConnectionPool INSTANCE = new OrdinaryConnectionPool();
    }
}
