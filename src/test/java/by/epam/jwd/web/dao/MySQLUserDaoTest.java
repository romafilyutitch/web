package by.epam.jwd.web.dao;

import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.exception.ConnectionPoolInitializationException;
import by.epam.jwd.web.model.User;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class MySQLUserDaoTest {
    private static final ConnectionPool POOL = ConnectionPool.getConnectionPool();
    private final MySQLUserDao testDao = MySQLUserDao.getInstance();
    private User testUser = new User("login", "login");

    @BeforeClass
    public static void setUp() throws ConnectionPoolInitializationException {
        POOL.init();
    }

    @AfterClass
    public static void tearDown() {
        POOL.destroy();
    }

    @Before
    public void saveTestUser() {
        testUser = testDao.save(testUser);
    }

    @After
    public void deleteTestUser() {
        testDao.delete(testUser.getId());
    }

    @Test
    public void save() {
        final Optional<User> optionalUser = testDao.findById(testUser.getId());
        assertNotNull("Optional user i null", optionalUser);
        if (!optionalUser.isPresent()) {
            fail("Saved user was not found by id");
        }
        assertEquals("User was found by id but not equal to test user", testUser, optionalUser.get());
    }

    @Test
    public void findAll() {
        final List<User> allUsers = testDao.findAll();
        assertNotNull("All users list is null", allUsers);
    }

    @Test
    public void findById() {
        final Optional<User> optionalUser = testDao.findById(testUser.getId());
        assertNotNull("Returned value is null", optionalUser);
    }

    @Test
    public void update() {
        User userToUpdate = new User("update", "update");
        userToUpdate = testDao.save(userToUpdate);
        userToUpdate = userToUpdate.updateLogin("NEW UPDATED");
        final User updatedUser = testDao.update(userToUpdate);
        assertNotNull("Updated user is null", updatedUser);
        assertEquals("Updated user not equal to user to update", userToUpdate, updatedUser);
    }

    @Test
    public void delete() {
        testDao.delete(testUser.getId());
        final Optional<User> optionalUser = testDao.findById(testUser.getId());
        if (optionalUser.isPresent()) {
            fail("Test user was not deleted");
        }
     }

    @Test
    public void findPage() {
        final int pagesAmount = testDao.getPagesAmount();
        final List<User> page = testDao.findPage(pagesAmount);
        assertNotNull("Found page is null", page);
    }

    @Test
    public void getRowsAmount() {
        final int rowsAmount = testDao.getRowsAmount();
        assertTrue("Rows amount is negative", rowsAmount >= 0);
    }

    @Test
    public void getPagesAmount() {
        final int pagesAmount = testDao.getPagesAmount();
        assertTrue("Pages amount is negative", pagesAmount >= 0);
    }

    @Test
    public void getInstance() {
        final MySQLUserDao instance = MySQLUserDao.getInstance();
        assertNotNull("Instance is null", instance);
        assertEquals("Test user dao is not equal to instance", testDao, instance);
    }

    @Test
    public void findUserByLogin() {
        final Optional<User> optionalUserByLogin = testDao.findUserByLogin(testUser.getLogin());
        assertNotNull("Returned value is null", optionalUserByLogin);
    }

    @Test
    public void findUsersByRole() {
        final List<User> usersByRole = testDao.findUsersByRole(testUser.getRole());
        assertNotNull("Returned value is null", usersByRole);
        for (User user : usersByRole) {
            assertEquals("User by role have another role", user.getRole(), testUser.getRole());
        }
    }
}