package by.epam.jwd.web.dao;

import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.connectionPool.ConnectionPoolInitializationException;
import by.epam.jwd.web.dao.DAOException;
import by.epam.jwd.web.dao.MySQLUserDao;

import by.epam.jwd.web.model.User;
import by.epam.jwd.web.model.UserRole;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

public class MySQLUserDaoTest {
    private static final ConnectionPool pool = ConnectionPool.getConnectionPool();
    private final MySQLUserDao testDao = MySQLUserDao.getInstance();
    private User testUser = new User("Van", "AAA", UserRole.READER);

    @BeforeClass
    public static void setUp() throws ConnectionPoolInitializationException {
        pool.init();
    }

    @AfterClass
    public static void tearDown() {
        pool.destroy();
    }

    @Before
    public void saveTestUser() throws DAOException {
        testUser = testDao.save(testUser);
    }

    @After
    public void deleteTestUser() throws DAOException {
        testDao.delete(testUser.getId());
    }

    @Test
    public void save() {
        Assert.assertNotNull(testUser);
        Assert.assertNotNull(testUser.getId());
    }

    @Test
    public void findAll() throws DAOException {
        final List<User> allUsers = testDao.findAll();
        Assert.assertNotNull(allUsers);
        Assert.assertFalse(allUsers.isEmpty());
        Assert.assertTrue(allUsers.contains(testUser));
    }

    @Test
    public void findById() throws DAOException {
        final Optional<User> byId = testDao.findById(testUser.getId());
        Assert.assertEquals(testUser, byId.get());
    }

    @Test
    public void update() throws DAOException {
        testUser = testUser.updateLogin("Alex 3000");
        final User updatedUser = testDao.update(testUser);
        Assert.assertNotNull(testUser);
        Assert.assertEquals(testUser, updatedUser);
    }

    @Test
    public void delete() throws DAOException {
        testDao.delete(testUser.getId());
    }
}