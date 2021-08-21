package by.epam.jwd.web.service;

import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.connectionPool.ConnectionPoolInitializationException;
import by.epam.jwd.web.model.Subscription;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.model.UserRole;
import by.epam.jwd.web.service.impl.SimpleUserService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SimpleUserServiceTest {
    private final SimpleUserService testService = SimpleUserService.getInstance();
    private User testUser = new User("123", "123", UserRole.READER);

    @BeforeClass
    public static void initPool() throws ConnectionPoolInitializationException {
        ConnectionPool.getConnectionPool().init();

    }

    @AfterClass
    public static void destroyPool() {
        ConnectionPool.getConnectionPool().destroy();
    }

    @Before
    public void setUp() {
        testUser = testService.save(testUser);
    }

    @After
    public void tearDown() {
        testService.delete(testUser);
    }

    @Test
    public void getInstance_mustReturnSameInstance() {
        final SimpleUserService otherInstance = SimpleUserService.getInstance();
        assertEquals(testService, otherInstance);
    }

    @Test
    public void findAll_mustReturnNotNullList() {
        final List<User> allUsers = testService.findAll();
        assertNotNull(allUsers);
    }

    @Test
    public void loginUser_mustLogInExistUser() throws WrongLoginException, WrongPasswordException {
        final User loggedInUser = testService.login(new User("123", "123", UserRole.READER));
        assertNotNull(loggedInUser);
        assertEquals(loggedInUser, testUser);
    }

    @Test(expected = WrongLoginException.class)
    public void loginUser_mustThrowException_whenUserWithLoginDoesNotExist() throws WrongLoginException, WrongPasswordException {
        testService.delete(testUser);
        testService.login(testUser);
    }

    @Test(expected = WrongPasswordException.class)
    public void loginUser_mustThrowException_whenWrongPasswordPassed() throws WrongLoginException, WrongPasswordException {
        testService.login(new User("123", "WRONG_PASSWORD", UserRole.READER, null));
    }

    @Test
    public void findPage_mustReturnNotNullPage() {
        final int pagesAmount = testService.getPagesAmount();
        final List<User> foundPage = testService.findPage(pagesAmount);
        final List<User> allUsers = testService.findAll();
        assertNotNull(foundPage);
        assertTrue(allUsers.containsAll(foundPage));
    }

    @Test
    public void getPagesAmount_mustReturnNotNegativePagesAmount() {
        final int pagesAmount = testService.getPagesAmount();
        assertTrue(pagesAmount >= 0);
    }

    @Test
    public void register_mustSetIdToRegisteredUser() {
        assertNotNull(testUser);
        assertNotNull(testUser.getId());
    }

    @Test
    public void findById_mustReturnSavedUserById() {
        final User foundUser = testService.findById(testUser.getId());
        assertNotNull(foundUser);
        assertEquals(foundUser, testUser);
    }

    @Test(expected = ServiceException.class)
    public void findById_mustThrowException_whenUserWithIdWasNotFound() {
        testService.findById(0L);
    }

    @Test
    public void promoteUserRole_mustReturnUserWithPromotedRole() {
        final UserRole startUserRole = testUser.getRole();
        testService.promoteRole(testUser);
        final User foundUser = testService.findById(testUser.getId());
        final UserRole promotedRole = foundUser.getRole();

        assertTrue(promotedRole.compareTo(startUserRole) >= 0);
    }

    @Test
    public void demoteUserRole_mustReturnUserWithDemotedRole() {
        final UserRole startUserRole = testUser.getRole();
        testService.demoteRole(testUser);
        final User foundUser = testService.findById(testUser.getId());
        final UserRole demotedUserRole = foundUser.getRole();

        assertTrue(demotedUserRole.compareTo(startUserRole) <= 0);
    }

    @Test
    public void delete_mustDeleteSavedUser() {
        testService.delete(testUser);
        final List<User> all = testService.findAll();
        assertFalse(all.contains(testUser));
    }

    @Test
    public void setSubscription_mustReturnUserWithNewSubscription() {
        final Subscription subscription = new Subscription(LocalDate.now(), LocalDate.now().plusDays(2));
        testService.setSubscription(testUser, subscription);
        final User userWithNewSubscription = testService.findById(testUser.getId());

        assertNotNull(userWithNewSubscription);
    }

    @Test
    public void changeLogin_mustChangeLogin_ifNoUserWithSpecifiedLogin() throws UserWithLoginExistsException {
        final String newLogin = "NEW LOGIN";
        final User userWithChangedLogin = testService.changeLogin(testUser, newLogin);

        assertNotNull(userWithChangedLogin);
        assertEquals(userWithChangedLogin.getLogin(), newLogin);
    }

    @Test(expected = UserWithLoginExistsException.class)
    public void changeLogin_mustThrowException_whenUserWithThatLoginAlreadyExists() throws UserWithLoginExistsException {
        final String newLogin = "123";
        testService.changeLogin(testUser, newLogin);
    }

    @Test
    public void changePassword_mustReturnUserWithChangedPassword() {
        final String oldPassword = testUser.getPassword();
        final String newPassword = "NEW PASSWORD";
        final User userWithChangedPassword = testService.changePassword(testUser, newPassword);

        assertNotNull(userWithChangedPassword);
        assertNotEquals(userWithChangedPassword.getPassword(), oldPassword);
    }

    @Test
    public void findByLogin_mustReturnTestUser_whenTestUserLoginPassed() {
        final Optional<User> optionalUser = testService.findByLogin(testUser.getLogin());
        assertNotNull(optionalUser);
        assertTrue(optionalUser.isPresent());
        assertEquals(testUser, optionalUser.get());
    }

    @Test
    public void findByLogin_mustReturnEmptyOptional_whenThereIsNoUserWithPassedLogin() {
        testService.delete(testUser);
        final Optional<User> optionalUser = testService.findByLogin(testUser.getLogin());
        assertNotNull(optionalUser);
        assertFalse(optionalUser.isPresent());
    }
}