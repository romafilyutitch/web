package by.epam.jwd.web.connectionPool;

import by.epam.jwd.web.connectionPool.ConnectionPoolInitializationException;
import by.epam.jwd.web.connectionPool.OrdinaryConnectionPool;
import by.epam.jwd.web.connectionPool.ProxyConnection;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class OrdinaryConnectionPoolTest {
    private static final OrdinaryConnectionPool testPool = OrdinaryConnectionPool.getInstance();

    @BeforeClass
    public static void setUp() throws ConnectionPoolInitializationException {
        testPool.init();
    }

    @AfterClass
    public static void tearDown() {
        testPool.destroy();
    }

    @Test
    public void takeFreeConnection_mustReturnNutNullConnection() {
        final Connection testConnection = testPool.takeFreeConnection();
        Assert.assertNotNull("Not null connection will be taken", testConnection);
    }

    @Test
    public void returnTakenConnection() {
        final Connection testConnection = testPool.takeFreeConnection();
        testPool.returnTakenConnection(testConnection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void returnTakenConnection_mustThrowException_whenReturnedConnectionIsNotInTakenConnectionsSet() throws SQLException {
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarydb", "root", "050399");
        testPool.returnTakenConnection(connection);
    }

    @Test(expected = NullPointerException.class)
    public void returnTakenConnection_mustThrowException_whenReturnedConnectionIsNull() {
        testPool.returnTakenConnection(null);
    }

    @Test
    public void takeFreeConnection_mustReturnProxyConnection() {
        final Connection connection = testPool.takeFreeConnection();
        Assert.assertTrue("Taken connection must be instance of ProxyConnection class", connection instanceof ProxyConnection);
    }

    @Test(expected = IllegalArgumentException.class)
    public void returnTakenConnection_mustThrowException_whenReturnConnectionIsNotProxyConnection() throws SQLException {
        final Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/librarydb", "root", "050399");
        testPool.returnTakenConnection(connection);
    }
}