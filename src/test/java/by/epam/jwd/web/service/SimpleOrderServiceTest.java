package by.epam.jwd.web.service;

import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.exception.ConnectionPoolInitializationException;
import by.epam.jwd.web.exception.RegisterException;
import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Order;
import by.epam.jwd.web.model.Status;
import by.epam.jwd.web.model.User;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class SimpleOrderServiceTest {
    private static final ConnectionPool POOL = ConnectionPool.getConnectionPool();
    private final SimpleOrderService testService = SimpleOrderService.getInstance();
    private final User testUser = SimpleUserService.getInstance().findAll().stream().findAny().get();
    private final Book testBook = SimpleBookService.getInstance().findAll().stream().findAny().get();

    private Order testOrder = new Order(testUser, testBook);

    @BeforeClass
    public static void initPool() throws ConnectionPoolInitializationException {
        POOL.init();
    }

    @AfterClass
    public static void destroyPool() {
        POOL.destroy();
    }

    @Before
    public void setUp() throws Exception {
        testOrder = testService.register(testOrder);
    }

    @After
    public void tearDown() throws Exception {
        testService.delete(testOrder.getId());
    }

    @Test
    public void getInstance_mustReturnSameInstance() {
        final SimpleOrderService instance = SimpleOrderService.getInstance();
        Assert.assertEquals("Test order service must be equal to get instance", instance, testService);
    }

    @Test
    public void findAll_mustReturnNotNullList() {
        final List<Order> allOrders = testService.findAll();
        Assert.assertNotNull("All orders list must be not null", allOrders);
        Assert.assertTrue("All orders list must contain saved order", allOrders.contains(testOrder));
    }

    @Test
    public void register_mustReturnOrderWithAssignedIdStatusAndDate() {
        Assert.assertNotNull("Registered order must be not null", testOrder);
        Assert.assertNotNull("Registered order id must be not null", testOrder.getId());
        Assert.assertTrue("Registered order status must be ordered or approved", testOrder.getStatus().equals(Status.ORDERED) || testOrder.getStatus().equals(Status.APPROVED));
        Assert.assertEquals("Registered order date must be current date", LocalDate.now(), testOrder.getOrderDate());
    }

    @Test
    public void findPage_mustReturnNotNullPageList() {
        final int pagesAmount = testService.getPagesAmount();
        final List<Order> foundPage = testService.findPage(pagesAmount);
        Assert.assertNotNull("Found page list must be not null", foundPage);
    }

    @Test
    public void getPagesAmount_mustReturnNotNegativeNumber() {
        final int pagesAmount = testService.getPagesAmount();
        Assert.assertTrue("Pages amount must be not negative", pagesAmount >= 0);
    }

    @Test
    public void approveOrder_mustChangeOrderStatusToApprovedStatus() {
        final Order approvedOrder = testService.approveOrder(testOrder.getId());
        Assert.assertNotNull("Approved order must be not null", approvedOrder);
        Assert.assertEquals("Approved order is must be equal to test order id",testOrder.getId(), approvedOrder.getId());
        Assert.assertEquals("Approved order status must be changed to Approved", Status.APPROVED, approvedOrder.getStatus());
    }

    @Test
    public void delete_mustDeleteOrderWithSpecifiedId() {
        testService.delete(testOrder.getId());
        final List<Order> allOrders = testService.findAll();
        Assert.assertFalse("All orders list must not contain deleted order", allOrders.contains(testOrder));
    }

    @Test
    public void returnOrder_mustChangeStatusToReturnedStatus() {
        final Order returnedOrder = testService.returnOrder(testOrder.getId());
        Assert.assertNotNull("Returned order must be not null", returnedOrder);
        Assert.assertEquals("Returned order id must be equal to test order id", testOrder.getId(), returnedOrder.getId());
        Assert.assertEquals("Returned order status must be changed", Status.RETURNED, returnedOrder.getStatus());
    }

    @Test
    public void findByReaderId_mustReturnNotNullOptionalOrder() {
        final List<Order> optionalOrder = testService.findByReaderId(testUser.getId());
        Assert.assertNotNull("Returned value must be not null", optionalOrder);
    }

    @Test
    public void findByBookId_mustReturnNotNullOptionalOrder() {
        final List<Order> optionalOrder = testService.findByBookId(testBook.getId());
        Assert.assertNotNull("Returned value must be not null", optionalOrder);
    }

    @Test
    public void findById_mustReturnSavedOrderById() {
        final Order foundOrder = testService.findById(testOrder.getId());
        Assert.assertNotNull("Found order must be not null", foundOrder);
        Assert.assertEquals("Found order id must be equal to test order id", testOrder.getId(), foundOrder.getId());
    }

    @Test(expected = ServiceException.class)
    public void findById_mustThrowException_whenThereIsNoOrderWithSpecifiedId() {
        testService.findById(0L);
    }
}