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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;


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
        assertNotNull(testSubscription);
        assertNotNull(testSubscription.getId());
    }

    @Test
    public void findAll_mustReturnNotNullList() {
        final List<Subscription> allSubscriptions = testDao.findAll();
        assertNotNull(allSubscriptions);
    }

    @Test
    public void findById_mustReturnSavedSubscription_whenSavedSubscriptionIdPassed() {
        final Optional<Subscription> optionalSubscription = testDao.findById(testSubscription.getId());
        assertNotNull(optionalSubscription);
        assertTrue(optionalSubscription.isPresent());
        assertEquals(testSubscription, optionalSubscription.get());
    }

    @Test
    public void findById_mustReturnEmptyOptionalSubscription_whenThereIsNoSubscriptionWithPassedId() {
        testDao.delete(testSubscription.getId());
        final Optional<Subscription> optionalSubscription = testDao.findById(testSubscription.getId());
        assertNotNull(optionalSubscription);
        assertFalse(optionalSubscription.isPresent());
    }

    @Test
    public void update_mustUpdateSavedSubscription() {
        final LocalDate updatedEndDate = LocalDate.now().plusDays(1);
        testSubscription = new Subscription(testSubscription.getId(), testSubscription.getStartDate(), updatedEndDate);
        final Subscription updatedSubscription = testDao.update(testSubscription);
        assertNotNull(updatedSubscription);
        assertEquals(updatedEndDate, updatedSubscription.getEndDate());
        assertEquals(testSubscription, updatedSubscription);
    }

    @Test
    public void delete_mustDeleteTestSubscription() {
        testDao.delete(testSubscription.getId());
        final List<Subscription> allSubscription = testDao.findAll();
        assertFalse(allSubscription.contains(testSubscription));
    }

    @Test
    public void findPage_mustReturnNotNullPage() {
        final int pagesAmount = testDao.getPagesAmount();
        final List<Subscription> page = testDao.findPage(pagesAmount);
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
        final MySQLSubscriptionDao instance = MySQLSubscriptionDao.getInstance();
        assertNotNull(instance);
        assertSame(testDao, instance);
    }

    @Test
    public void findByStartDate_mustReturnListOfSubscriptionsWithSpecifiedStartDate() {
        final List<Subscription> subscriptionsByStartDate = testDao.findByStartDate(testSubscription.getStartDate());
        assertNotNull(subscriptionsByStartDate);
        for (Subscription foundSubscription : subscriptionsByStartDate) {
            assertEquals(testSubscription.getStartDate(), foundSubscription.getStartDate());
        }
    }

    @Test
    public void findByEndDate_mustReturnListOfSubscriptionsWithSpecifiedEndDate() {
        final List<Subscription> subscriptionsByEndDate = testDao.findByEndDate(testSubscription.getEndDate());
        assertNotNull(subscriptionsByEndDate);
        for (Subscription subscription : subscriptionsByEndDate) {
            assertEquals(testSubscription.getEndDate(), subscription.getEndDate());
        }
    }
}