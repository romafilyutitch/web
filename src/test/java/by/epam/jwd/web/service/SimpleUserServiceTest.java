package by.epam.jwd.web.service;

import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.exception.ConnectionPoolInitializationException;
import by.epam.jwd.web.exception.NoUserWithLoginException;
import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.exception.UserWithLoginExistsException;
import by.epam.jwd.web.exception.WrongPasswordException;
import by.epam.jwd.web.exception.WrongSubscriptionException;
import by.epam.jwd.web.model.Subscription;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.model.UserRole;
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

    private final static ConnectionPool POOL = ConnectionPool.getConnectionPool();

    @BeforeClass
    public static void setUp() throws ConnectionPoolInitializationException {
        POOL.init();

    }

    @AfterClass
    public static void tearDown() {
        POOL.destroy();
    }

    @Before
    public void saveUser() {
        testUser = testService.register(testUser);
    }

    @After
    public void deleteUser() {
        testService.delete(testUser.getId());
    }

    @Test
    public void getInstance_mustReturnSameInstance() {
        final SimpleUserService otherInstance = SimpleUserService.getInstance();
        assertEquals("Test instance must be equal to other get instance", testService, otherInstance);
    }

    @Test
    public void findAll_mustReturnNotNullList() {
        final List<User> allUsers = testService.findAll();
        assertNotNull("All users list mus be not null", allUsers);
    }

    @Test
    public void loginUser_mustLogInExistUser() throws NoUserWithLoginException, WrongPasswordException {
        final User loggedInUser = testService.loginUser(new User("123", "123", UserRole.READER));
        assertNotNull("Logged in user instance must be not null", loggedInUser);
        assertEquals("Logged in user must be equal to test user", loggedInUser, testUser);
    }

    @Test(expected = NoUserWithLoginException.class)
    public void loginUser_mustThrowException_whenUserWithLoginDoesNotExist() throws NoUserWithLoginException, WrongPasswordException {
        testService.delete(testUser.getId());
        testService.loginUser(testUser);
    }

    @Test(expected = WrongPasswordException.class)
    public void loginUser_mustThrowException_whenWrongPasswordPassed() throws NoUserWithLoginException, WrongPasswordException {
        testService.loginUser(new User("123", "WRONG_PASSWORD", UserRole.READER, null));
    }

    @Test
    public void findPage_mustReturnNotNullPage() {
        final int pagesAmount = testService.getPagesAmount();
        final List<User> foundPage = testService.findPage(pagesAmount);
        final List<User> allUsers = testService.findAll();
        assertNotNull("Found users page list must be not null", foundPage);
        assertTrue("All users list must contain users on found page", allUsers.containsAll(foundPage));
    }

    @Test
    public void getPagesAmount_mustReturnNotNegativePagesAmount() {
        final int pagesAmount = testService.getPagesAmount();
        assertTrue("Pages amount must be positive or equal zero", pagesAmount >= 0);
    }

    @Test
    public void register_mustSetIdToRegisteredUser() {
        assertNotNull("Registered user must be not null", testUser);
        assertNotNull("Registered user id must be not null", testUser.getId());
    }

    @Test
    public void findById_mustReturnSavedUserById() {
        final User foundUser = testService.findById(testUser.getId());
        assertNotNull("Found user must be not null", foundUser);
        assertEquals("Found user must be equal to test user", foundUser, testUser);
    }

    @Test(expected = ServiceException.class)
    public void findById_mustThrowException_whenUserWithIdWasNotFound() {
        final User foundUser = testService.findById(0L);
    }

    @Test
    public void promoteUserRole_mustReturnUserWithPromotedRole() {
        final UserRole startUserRole = testUser.getRole();
        testService.promoteUserRole(testUser);
        final User foundUser = testService.findById(testUser.getId());
        final UserRole promotedRole = foundUser.getRole();

        assertTrue("Promoted role must be greater than or equal to start user role", promotedRole.compareTo(startUserRole) >= 0);
    }

    @Test
    public void demoteUserRole_mustReturnUserWithDemotedRole() {
        final UserRole startUserRole = testUser.getRole();
        testService.demoteUserRole(testUser);
        final User foundUser = testService.findById(testUser.getId());
        final UserRole demotedUserRole = foundUser.getRole();

        assertTrue("Demoted role must be less than or equal to start user role", demotedUserRole.compareTo(startUserRole) <= 0);
    }

    @Test
    public void delete_mustDeleteSavedUser() {
        testService.delete(testUser.getId());
        final List<User> all = testService.findAll();
        assertFalse("All users list must not contain deleted user", all.contains(testUser));
    }

    @Test
    public void setSubscription_mustReturnUserWithNewSubscription() throws WrongSubscriptionException {
        final Subscription subscription = new Subscription(LocalDate.now(), LocalDate.now().plusDays(2));
        testService.setSubscription(testUser, subscription);
        final User userWithNewSubscription = testService.findById(testUser.getId());

        assertNotNull("User with subscription must be not null", userWithNewSubscription);
    }

    @Test(expected = WrongSubscriptionException.class)
    public void setSubscription_mustThrowException_ifStartDateIsAfterEndDate() throws WrongSubscriptionException {
        final Subscription subscription = new Subscription(LocalDate.now(), LocalDate.now().minusMonths(1));
        testService.setSubscription(testUser, subscription);
    }

    @Test
    public void changeLogin_mustChangeLogin_ifNoUserWithSpecifiedLogin() throws NoUserWithLoginException, UserWithLoginExistsException {
        final String newLogin = "NEW LOGIN";
        final User userWithChangedLogin = testService.changeLogin(testUser, newLogin);

        assertNotNull("User with changed login must be not null", userWithChangedLogin);
        assertEquals("User with changed login must have new login", userWithChangedLogin.getLogin(), newLogin);
    }

    @Test(expected = UserWithLoginExistsException.class)
    public void changeLogin_mustThrowException_whenUserWithThatLoginAlreadyExists() throws NoUserWithLoginException, UserWithLoginExistsException {
        final String newLogin = "123";
        final User userWithChangedLogin = testService.changeLogin(testUser, newLogin);
    }

    @Test
    public void changePassword_mustReturnUserWithChangedPassword() {
        final String oldPassword = testUser.getPassword();
        final String newPassword = "NEW PASSWORD";
        final User userWithChangedPassword = testService.changePassword(testUser, newPassword);

        assertNotNull("User with changed password must be not null", userWithChangedPassword);
        assertNotEquals("new Password must be not equal to old password", userWithChangedPassword.getPassword(), oldPassword);
    }

    @Test
    public void findByLogin_mustReturnTestUser_whenTestUserLoginPassed() {
        final Optional<User> optionalUser = testService.findByLogin(testUser.getLogin());
        assertNotNull("Returned value must be not null", optionalUser);
        assertTrue("Returned value must be not empty", optionalUser.isPresent());
        assertEquals("Found user must be equal to test user", testUser, optionalUser.get());
    }

    @Test
    public void findByLogin_mustReturnEmptyOptional_whenThereIsNoUserWithPassedLogin() {
        testService.delete(testUser.getId());
        final Optional<User> optionalUser = testService.findByLogin(testUser.getLogin());
        assertNotNull("Returned value must be not null", optionalUser);
        assertFalse("Returned value must be empty", optionalUser.isPresent());
    }
}