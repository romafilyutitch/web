package by.epam.jwd.web.dao;

import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.exception.ConnectionPoolInitializationException;
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
    public void save_mustAssignIdToSavedUser() {
        assertNotNull("Returned value must be not null", testUser);
        assertNotNull("Saved user must have not null id", testUser.getId());
    }

    @Test
    public void findAll_mustReturnNotNullList() {
        final List<User> allUsers = testDao.findAll();
        assertNotNull("All users list must be not null", allUsers);
    }

    @Test
    public void findById_mustReturnSavedUser_whenSavedUserIdPassed() {
        final Optional<User> optionalUser = testDao.findById(testUser.getId());
        assertNotNull("Returned value must be not null", optionalUser);
        assertTrue("Optional user must be not empty", optionalUser.isPresent());
        assertEquals("Found user must be equal to saved user", testUser, optionalUser.get());
    }

    @Test
    public void findById_mustReturnEmptyOptionalUser_whenThereIsNoUserWithPassedId() {
        testDao.delete(testUser.getId());
        final Optional<User> optionalUser = testDao.findById(testUser.getId());
        assertNotNull("Returned value must be not null", optionalUser);
        assertFalse("Optional user must be empty", optionalUser.isPresent());
    }

    @Test
    public void update_mustUpdateUser() {
        final String updatedLogin = "UPDATED";
        testUser = new User(testUser.getId(), updatedLogin, testUser.getPassword(), testUser.getRole(), testUser.getSubscription());
        final User updatedUser = testDao.update(testUser);
        assertNotNull("Returned value must be not null", updatedUser);
        assertEquals("Updated user must have updated value", updatedLogin, updatedUser.getLogin());
        assertEquals("Updated user must be equal to saved user", testUser, updatedUser);
    }

    @Test
    public void delete_mustDeleteTestUser() {
        testDao.delete(testUser.getId());
        final List<User> allUsers = testDao.findAll();
        assertFalse("All users list must not contain deleted user", allUsers.contains(testUser));
     }

    @Test
    public void findPage_mustReturnNotNullPage() {
        final int pagesAmount = testDao.getPagesAmount();
        final List<User> page = testDao.findPage(pagesAmount);
        assertNotNull("Found page must be not null", page);
    }

    @Test
    public void getRowsAmount_mustReturnNotNegativeNumber() {
        final int rowsAmount = testDao.getRowsAmount();
        assertTrue("Rows amount must be not negative", rowsAmount >= 0);
    }

    @Test
    public void getPagesAmount_mustReturnNotNegativeNumber() {
        final int pagesAmount = testDao.getPagesAmount();
        assertTrue("Pages amount must be not negative", pagesAmount >= 0);
    }

    @Test
    public void getInstance_mustReturnSameInstanceAsTestDao() {
        final MySQLUserDao instance = MySQLUserDao.getInstance();
        assertNotNull("Instance must be not null", instance);
        assertSame("Instance must be same as test dao", testDao, instance);
    }

    @Test
    public void findUserByLogin_mustReturnSavedUser_whenSavedUserLoginPassed() {
        final Optional<User> optionalUser = testDao.findUserByLogin(testUser.getLogin());
        assertNotNull("Returned value must be not null", optionalUser);
        assertTrue("Optional user must be not empty", optionalUser.isPresent());
        assertEquals("Found user must be equal to saved user", testUser, optionalUser.get());
    }

    @Test
    public void findUserByLogin_mustReturnEmptyOptionalUser_whenWrongLoginPassed() {
        testDao.delete(testUser.getId());
        final Optional<User> optionalUser = testDao.findUserByLogin(testUser.getLogin());
        assertNotNull("Returned value must be not null", optionalUser);
        assertFalse("Optional user must be empty", optionalUser.isPresent());
    }

    @Test
    public void findUsersByRole_mustReturnUsersWithSpecifiedRole() {
        final List<User> foundUsers = testDao.findUsersByRole(testUser.getRole());
        assertNotNull("Returned value must be not null", foundUsers);
        for (User foundUser : foundUsers) {
            assertEquals("Found user role must be equal to passed role", testUser.getRole(), foundUser.getRole());
        }
    }
}