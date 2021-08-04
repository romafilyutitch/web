package by.epam.jwd.web.dao;

import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.dao.mysql.MySQLSubscriptionDao;
import by.epam.jwd.web.exception.ConnectionPoolInitializationException;
import by.epam.jwd.web.model.Subscription;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;


public class MySQLSubscriptionDaoTest {
    private static final ConnectionPool POOL = ConnectionPool.getConnectionPool();
    private final MySQLSubscriptionDao testDao = MySQLSubscriptionDao.getInstance();
    private Subscription testSubscription = new Subscription(LocalDate.now(), LocalDate.now().plusWeeks(1));


    @BeforeClass
    public static void setUp() throws ConnectionPoolInitializationException {
        POOL.init();
    }

    @AfterClass
    public static void tearDown() {
        POOL.destroy();
    }

    @Before
    public void saveSubscription() {
        testSubscription = testDao.save(testSubscription);
    }

    @After
    public void deleteSubscription() {
        testDao.delete(testSubscription.getId());
    }

    @Test
    public void save_mustAssignIdToSavedSubscription() {
        assertNotNull("Saved subscription must be not null", testSubscription);
        assertNotNull("Saved subscription must have not null id", testSubscription.getId());
    }

    @Test
    public void findAll_mustReturnNotNullList() {
        final List<Subscription> allSubscriptions = testDao.findAll();
        assertNotNull("All subscription list must be not null", allSubscriptions);
    }

    @Test
    public void findById_mustReturnSavedSubscription_whenSavedSubscriptionIdPassed() {
        final Optional<Subscription> optionalSubscription = testDao.findById(testSubscription.getId());
        assertNotNull("Returned value must be not null", optionalSubscription);
        assertTrue("Optional subscription must be not empty", optionalSubscription.isPresent());
        assertEquals("Found subscription must be equal to saved subscription", testSubscription, optionalSubscription.get());
    }

    @Test
    public void findById_mustReturnEmptyOptionalSubscription_whenThereIsNoSubscriptionWithPassedId() {
        testDao.delete(testSubscription.getId());
        final Optional<Subscription> optionalSubscription = testDao.findById(testSubscription.getId());
        assertNotNull("Returned value must be not null", optionalSubscription);
        assertFalse("Optional subscription must be empty", optionalSubscription.isPresent());
    }

    @Test
    public void update_mustUpdateSavedSubscription() {
        final LocalDate updatedEndDate = LocalDate.now().plusDays(1);
        testSubscription = new Subscription(testSubscription.getId(), testSubscription.getStartDate(), updatedEndDate);
        final Subscription updatedSubscription = testDao.update(testSubscription);
        assertNotNull("Updated subscription must be not null", updatedSubscription);
        assertEquals("Updated subscription must have passed end date",updatedEndDate,updatedSubscription.getEndDate());
        assertEquals("Updated subscription must be equal to saved subscription", testSubscription, updatedSubscription);
    }

    @Test
    public void delete_mustDeleteTestSubscription() {
        testDao.delete(testSubscription.getId());
        final List<Subscription> allSubscription = testDao.findAll();
        assertFalse("All subscriptions list must not contain deleted subscription", allSubscription.contains(testSubscription));
    }

    @Test
    public void findPage_mustReturnNotNullPage() {
        final int pagesAmount = testDao.getPagesAmount();
        final List<Subscription> page = testDao.findPage(pagesAmount);
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
        final MySQLSubscriptionDao instance = MySQLSubscriptionDao.getInstance();
        assertNotNull("Returned instance must be not null", instance);
        assertSame("Returned instance must be same as testDao", testDao, instance);
    }

    @Test
    public void findByStartDate_mustReturnListOfSubscriptionsWithSpecifiedStartDate() {
        final List<Subscription> subscriptionsByStartDate = testDao.findByStartDate(testSubscription.getStartDate());
        assertNotNull("Subscriptions list must be not null", subscriptionsByStartDate);
        for (Subscription foundSubscription : subscriptionsByStartDate) {
            assertEquals("Found subscription must have passed start date", testSubscription.getStartDate(), foundSubscription.getStartDate());
        }
    }

    @Test
    public void findByEndDate_mustReturnListOfSubscriptionsWithSpecifiedEndDate() {
        final List<Subscription> subscriptionsByEndDate = testDao.findByEndDate(testSubscription.getEndDate());
        assertNotNull("Subscriptions list must be not null", subscriptionsByEndDate);
        for (Subscription subscription : subscriptionsByEndDate) {
            assertEquals("Found subscription must have passed end date", testSubscription.getEndDate(), subscription.getEndDate());
        }
    }
}