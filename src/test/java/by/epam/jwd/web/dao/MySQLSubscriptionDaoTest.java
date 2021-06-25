package by.epam.jwd.web.dao;

import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.exception.ConnectionPoolInitializationException;
import by.epam.jwd.web.model.Subscription;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


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
    public void save() {
        final Optional<Subscription> optionalSubscription = testDao.findById(testSubscription.getId());
        if (!optionalSubscription.isPresent()) {
            Assert.fail("Saved subscription was not found by id");
        }
        Assert.assertEquals("test subscription not equal to found by subscription by id", testSubscription, optionalSubscription.get());
    }

    @Test
    public void findAll() {
        final List<Subscription> allSubscriptions = testDao.findAll();
        Assert.assertNotNull("all subscriptions is null", allSubscriptions);
    }

    @Test
    public void findById() {
        final Optional<Subscription> optionalSubscription = testDao.findById(testSubscription.getId());
        Assert.assertNotNull("Optional subscription is null", optionalSubscription);
    }

    @Test
    public void update() {
        Subscription subscription = new Subscription(LocalDate.now(), LocalDate.now());
        subscription = testDao.save(subscription);
        subscription = subscription.updateEndDate(LocalDate.now().plusDays(1));
        final Subscription updatedSubscription = testDao.update(subscription);
        Assert.assertNotNull("Updated subscription is null", updatedSubscription);
        Assert.assertEquals("subscription and updated subscription are not equals", subscription, updatedSubscription);
    }

    @Test
    public void delete() {
        testDao.delete(testSubscription.getId());
        final Optional<Subscription> optionalSubscription = testDao.findById(testSubscription.getId());
        if (optionalSubscription.isPresent()) {
            Assert.fail("Saved subscription was not deleted");
        }
    }

    @Test
    public void findPage() {
        final int pagesAmount = testDao.getPagesAmount();
        final List<Subscription> page = testDao.findPage(pagesAmount);
        Assert.assertNotNull("Found page is null", page);
    }

    @Test
    public void getRowsAmount() {
        final int rowsAmount = testDao.getRowsAmount();
        if (rowsAmount < 0) {
            Assert.fail("rows amount is negative");
        }
    }

    @Test
    public void getPagesAmount() {
        final int pagesAmount = testDao.getPagesAmount();
        if (pagesAmount < 0) {
            Assert.fail("pages amount is negative");
        }
    }

    @Test
    public void getInstance() {
        final MySQLSubscriptionDao instance = MySQLSubscriptionDao.getInstance();
        Assert.assertNotNull("Instance is null", instance);
        Assert.assertEquals("test subscription dao is not equal to instance", testDao, instance);
    }

    @Test
    public void findByStartDate() {
        final List<Subscription> subscriptionsByStartDate = testDao.findByStartDate(testSubscription.getStartDate());
        Assert.assertNotNull("subscriptions list is null", subscriptionsByStartDate);
        for (Subscription subscription : subscriptionsByStartDate) {
            if (!subscription.getStartDate().isEqual(testSubscription.getStartDate())) {
                Assert.fail("Start date in subscription is not equal to specified start date");
            }
        }
    }

    @Test
    public void findByEndDate() {
        final List<Subscription> subscriptionsByEndDate = testDao.findByEndDate(testSubscription.getEndDate());
        Assert.assertNotNull("subscriptions list is null", subscriptionsByEndDate);
        for (Subscription subscription : subscriptionsByEndDate) {
            if (!subscription.getEndDate().isEqual(testSubscription.getEndDate())) {
                Assert.fail("End date in subscription is not equal to specified end date");
            }
        }
    }
}