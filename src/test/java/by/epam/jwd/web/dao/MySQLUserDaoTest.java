package by.epam.jwd.web.dao;

import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.dao.mysql.MySQLUserDao;
import by.epam.jwd.web.connectionPool.ConnectionPoolInitializationException;
import by.epam.jwd.web.model.User;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class MySQLUserDaoTest {
    private final MySQLUserDao testDao = MySQLUserDao.getInstance();
    private User testUser = new User("login", "login");

    @BeforeClass
    public static void initPool() throws ConnectionPoolInitializationException {
        ConnectionPool.getConnectionPool().init();
    }

    @AfterClass
    public static void destroyPool() {
        ConnectionPool.getConnectionPool().destroy();
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
        assertNotNull(testUser);
        assertNotNull(testUser.getId());
    }

    @Test
    public void findAll_mustReturnNotNullList() {
        final List<User> allUsers = testDao.findAll();
        assertNotNull(allUsers);
    }

    @Test
    public void findById_mustReturnSavedUser_whenSavedUserIdPassed() {
        final Optional<User> optionalUser = testDao.findById(testUser.getId());
        assertNotNull(optionalUser);
        assertTrue(optionalUser.isPresent());
        assertEquals(testUser, optionalUser.get());
    }

    @Test
    public void findById_mustReturnEmptyOptionalUser_whenThereIsNoUserWithPassedId() {
        testDao.delete(testUser.getId());
        final Optional<User> optionalUser = testDao.findById(testUser.getId());
        assertNotNull(optionalUser);
        assertFalse(optionalUser.isPresent());
    }

    @Test
    public void update_mustUpdateUser() {
        final String updatedLogin = "UPDATED";
        testUser = new User(testUser.getId(), updatedLogin, testUser.getPassword(), testUser.getRole(), testUser.getSubscription());
        final User updatedUser = testDao.update(testUser);
        assertNotNull(updatedUser);
        assertEquals(updatedLogin, updatedUser.getLogin());
        assertEquals(testUser, updatedUser);
    }

    @Test
    public void delete_mustDeleteTestUser() {
        testDao.delete(testUser.getId());
        final List<User> allUsers = testDao.findAll();
        assertFalse(allUsers.contains(testUser));
    }

    @Test
    public void findPage_mustReturnNotNullPage() {
        final int pagesAmount = testDao.getPagesAmount();
        final List<User> page = testDao.findPage(pagesAmount);
        assertNotNull(page);
    }

    @Test
    public void getRowsAmount_mustReturnNotNegativeNumber() {
        final int rowsAmount = testDao.getRowsAmount();
        assertTrue(rowsAmount >= 0);
    }

    @Test
    public void getPagesAmount_mustReturnNotNegativeNumber() {
        final int pagesAmount = testDao.getPagesAmount();
        assertTrue(pagesAmount >= 0);
    }

    @Test
    public void getInstance_mustReturnSameInstanceAsTestDao() {
        final MySQLUserDao instance = MySQLUserDao.getInstance();
        assertNotNull(instance);
        assertSame(testDao, instance);
    }

    @Test
    public void findUserByLogin_mustReturnSavedUser_whenSavedUserLoginPassed() {
        final Optional<User> optionalUser = testDao.findUserByLogin(testUser.getLogin());
        assertNotNull(optionalUser);
        assertTrue(optionalUser.isPresent());
        assertEquals(testUser, optionalUser.get());
    }

    @Test
    public void findUserByLogin_mustReturnEmptyOptionalUser_whenWrongLoginPassed() {
        testDao.delete(testUser.getId());
        final Optional<User> optionalUser = testDao.findUserByLogin(testUser.getLogin());
        assertNotNull(optionalUser);
        assertFalse(optionalUser.isPresent());
    }

    @Test
    public void findUsersByRole_mustReturnUsersWithSpecifiedRole() {
        final List<User> foundUsers = testDao.findUsersByRole(testUser.getRole());
        assertNotNull(foundUsers);
        for (User foundUser : foundUsers) {
            assertEquals(testUser.getRole(), foundUser.getRole());
        }
    }
}