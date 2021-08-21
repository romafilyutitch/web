package by.epam.jwd.web.connectionPool;

import by.epam.jwd.web.exception.ConnectionPoolInitializationException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


public class OrdinaryConnectionPoolTest {
    private static final OrdinaryConnectionPool TEST_POOL = OrdinaryConnectionPool.getInstance();

    @BeforeClass
    public static void initPool() throws ConnectionPoolInitializationException {
        TEST_POOL.init();
    }

    @AfterClass
    public static void destroyPool() {
        TEST_POOL.destroy();
    }

    @Test
    public void takeFreeConnection_mustReturnNutNullConnection() {
        final Connection testConnection = TEST_POOL.takeFreeConnection();
        assertNotNull("Not null connection must be taken", testConnection);
    }

    @Test
    public void returnTakenConnection() {
        final Connection testConnection = TEST_POOL.takeFreeConnection();
        TEST_POOL.returnTakenConnection(testConnection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void returnTakenConnection_mustThrowException_whenReturnedConnectionIsNotInTakenConnectionsSet() throws SQLException {
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarydb?serverTimezone=GMT%2B8", "root", "050399");
        TEST_POOL.returnTakenConnection(connection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void returnTakenConnection_mustThrowException_whenReturnedConnectionIsNull() {
        TEST_POOL.returnTakenConnection(null);
    }

    @Test
    public void takeFreeConnection_mustReturnProxyConnection() {
        final Connection connection = TEST_POOL.takeFreeConnection();
        assertTrue("Taken connection must be instance of ProxyConnection class", connection instanceof ProxyConnection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void returnTakenConnection_mustThrowException_whenReturnConnectionIsNotProxyConnection() throws SQLException {
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarydb?serverTimezone=GMT%2B8", "root", "050399");
        TEST_POOL.returnTakenConnection(connection);
    }
}