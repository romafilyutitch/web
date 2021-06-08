package by.epam.jwd.web.dao;

import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.connectionPool.ConnectionPoolInitializationException;
import by.epam.jwd.web.dao.DAOException;
import by.epam.jwd.web.dao.MySQLSubscriptionDao;

import by.epam.jwd.web.model.Subscription;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class MySQLSubscriptionDaoTest {
    private static final ConnectionPool pool = ConnectionPool.getConnectionPool();
    private final MySQLSubscriptionDao testDao = MySQLSubscriptionDao.getInstance();
    private Subscription testSubscription = new Subscription(LocalDate.now(), LocalDate.now().plusYears(1));

    @BeforeClass
    public static void setUp() throws ConnectionPoolInitializationException {
        pool.init();
    }

    @AfterClass
    public static void tearDown() {
        pool.destroy();
    }

    @Before
    public void saveSubscription() throws DAOException {
        testSubscription = testDao.save(testSubscription);
    }

    @Test
    public void save() {
        Assert.assertNotNull(testSubscription);
        Assert.assertNotNull(testSubscription.getId());
    }

    @Test
    public void findAll() throws DAOException {
        final List<Subscription> allSubscriptions = testDao.findAll();
        Assert.assertNotNull(allSubscriptions);
        Assert.assertFalse(allSubscriptions.isEmpty());
        Assert.assertTrue(allSubscriptions.contains(testSubscription));
    }

    @Test
    public void findById() throws DAOException {
        final Optional<Subscription> byId = testDao.findById(testSubscription.getId());
        Assert.assertEquals(testSubscription.getId(), byId.get().getId());
    }

    @Test
    public void update() throws DAOException {
        Subscription subscriptionToUpdate = new Subscription(LocalDate.now(), LocalDate.now().plusYears(1));
        subscriptionToUpdate = testDao.save(subscriptionToUpdate);
        subscriptionToUpdate = subscriptionToUpdate.updateStartDate(LocalDate.now().plusYears(1));
        final Subscription updatedSubscription = testDao.update(subscriptionToUpdate);
        Assert.assertEquals(subscriptionToUpdate, updatedSubscription);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void delete() throws DAOException {
        testDao.delete(testSubscription.getId());
    }
}