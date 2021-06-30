package by.epam.jwd.web.service;

import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.exception.ConnectionPoolInitializationException;
import by.epam.jwd.web.exception.LoginExistsException;
import by.epam.jwd.web.exception.NoLoginException;
import by.epam.jwd.web.exception.RegisterException;
import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.exception.SubscriptionException;
import by.epam.jwd.web.exception.WrongPasswordException;
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

import static org.junit.Assert.*;

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
    public void saveUser() throws RegisterException {
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
    public void loginUser_mustLogInExistUser() throws NoLoginException, WrongPasswordException {
        final User loggedInUser = testService.loginUser(new User("123", "123", UserRole.READER));
        assertNotNull("Logged in user instance must be not null", loggedInUser);
        assertEquals("Logged in user must be equal to test user", loggedInUser, testUser);
    }

    @Test(expected = NoLoginException.class)
    public void loginUser_mustThrowException_whenUserWithLoginDoesNotExist() throws NoLoginException, WrongPasswordException {
        testService.delete(testUser.getId());
        testService.loginUser(testUser);
    }

    @Test(expected = WrongPasswordException.class)
    public void loginUser_mustThrowException_whenWrongPasswordPassed() throws NoLoginException, WrongPasswordException {
        testService.loginUser(new User("123", "WRONG_PASSWORD"));
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
        final User userWithPromotedRole = testService.promoteUserRole(testUser.getId());
        final UserRole promotedRole = userWithPromotedRole.getRole();

        assertNotNull("User with promoted role must be not null", userWithPromotedRole);
        assertTrue("Promoted role must be greater than or equal to start user role", promotedRole.compareTo(startUserRole) >= 0);
    }

    @Test
    public void demoteUserRole_mustReturnUserWithDemotedRole() {
        final UserRole startUserRole = testUser.getRole();
        final User userWithDemotedRole = testService.demoteUserRole(testUser.getId());
        final UserRole demotedUserRole = userWithDemotedRole.getRole();

        assertNotNull("User with demoted role must be not null", userWithDemotedRole);
        assertTrue("Demoted role must be less than or equal to start user role", demotedUserRole.compareTo(startUserRole) <= 0);
    }

    @Test
    public void delete_mustDeleteSavedUser() {
        testService.delete(testUser.getId());
        final List<User> all = testService.findAll();
        assertFalse("All users list must not contain deleted user", all.contains(testUser));
    }

    @Test
    public void setSubscription_mustReturnUserWithNewSubscription() throws SubscriptionException {
        final Subscription subscription = new Subscription(LocalDate.now(), LocalDate.now().plusDays(2));
        final User userWithNewSubscription = testService.setSubscription(testUser.getId(), subscription);

        assertNotNull("User with subscription must be not null", userWithNewSubscription);
    }

    @Test(expected = SubscriptionException.class)
    public void setSubscription_mustThrowException_ifStartDateIsAfterEndDate() throws SubscriptionException {
        final Subscription subscription = new Subscription(LocalDate.now(), LocalDate.now().minusMonths(1));
        testService.setSubscription(testUser.getId(), subscription);
    }

    @Test
    public void changeLogin_mustChangeLogin_ifNoUserWithSpecifiedLogin() throws NoLoginException, LoginExistsException {
        final String newLogin = "NEW LOGIN";
        final User userWithChangedLogin = testService.changeLogin(testUser.getId(), newLogin);

        assertNotNull("User with changed login must be not null", userWithChangedLogin);
        assertEquals("User with changed login must have new login", userWithChangedLogin.getLogin(), newLogin);
    }

    @Test(expected = LoginExistsException.class)
    public void changeLogin_mustThrowException_whenUserWithThatLoginAlreadyExists() throws NoLoginException, LoginExistsException {
        final String newLogin = "123";
        final User userWithChangedLogin = testService.changeLogin(testUser.getId(), newLogin);
    }

    @Test
    public void changePassword_mustReturnUserWithChangedPassword() {
        final String oldPassword = testUser.getPassword();
        final String newPassword = "NEW PASSWORD";
        final User userWithChangedPassword = testService.changePassword(testUser.getId(), newPassword);

        assertNotNull("User with changed password must be not null", userWithChangedPassword);
        assertNotEquals("new Password must be not equal to old password", userWithChangedPassword.getPassword(), oldPassword);
    }
}