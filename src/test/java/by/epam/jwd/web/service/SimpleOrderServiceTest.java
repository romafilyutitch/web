package by.epam.jwd.web.service;

import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.exception.ConnectionPoolInitializationException;
import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Order;
import by.epam.jwd.web.model.Status;
import by.epam.jwd.web.model.User;
import org.junit.After;
import org.junit.AfterClass;
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

    private Order testOrder = new Order(testUser, testBook, LocalDate.now(), Status.ORDERED);

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
        testOrder = testService.save(testOrder);
    }

    @After
    public void tearDown() throws Exception {
        testService.delete(testOrder.getId());
    }

    @Test
    public void getInstance_mustReturnSameInstance() {
        final SimpleOrderService instance = SimpleOrderService.getInstance();
        assertEquals("Test order service must be equal to get instance", instance, testService);
    }

    @Test
    public void findAll_mustReturnNotNullList() {
        final List<Order> allOrders = testService.findAll();
        assertNotNull("All orders list must be not null", allOrders);
        assertTrue("All orders list must contain saved order", allOrders.contains(testOrder));
    }

    @Test
    public void register_mustReturnOrderWithAssignedIdStatusAndDate() {
        assertNotNull("Registered order must be not null", testOrder);
        assertNotNull("Registered order id must be not null", testOrder.getId());
        assertTrue("Registered order status must be ordered or approved", testOrder.getStatus().equals(Status.ORDERED) || testOrder.getStatus().equals(Status.APPROVED));
        assertEquals("Registered order date must be current date", LocalDate.now(), testOrder.getOrderDate());
    }

    @Test
    public void findPage_mustReturnNotNullPageList() {
        final int pagesAmount = testService.getPagesAmount();
        final List<Order> foundPage = testService.findPage(pagesAmount);
        assertNotNull("Found page list must be not null", foundPage);
    }

    @Test
    public void getPagesAmount_mustReturnNotNegativeNumber() {
        final int pagesAmount = testService.getPagesAmount();
        assertTrue("Pages amount must be not negative", pagesAmount >= 0);
    }

    @Test
    public void approveOrder_mustChangeOrderStatusToApprovedStatus() {
        testService.approveOrder(testOrder);
        final Order foundOrder = testService.findById(testOrder.getId());

        assertNotNull("Found order must be not null", foundOrder);
        assertEquals("Found order id must be equal to test order id", testOrder.getId(), foundOrder.getId());
        assertEquals("Approved order status must be changed to Approved", Status.APPROVED, foundOrder.getStatus());
    }

    @Test
    public void delete_mustDeleteOrderWithSpecifiedId() {
        testService.delete(testOrder.getId());
        final List<Order> allOrders = testService.findAll();
        assertFalse("All orders list must not contain deleted order", allOrders.contains(testOrder));
    }

    @Test
    public void returnOrder_mustChangeStatusToReturnedStatus() {
        testService.returnOrder(testOrder);
        final Order foundOrder = testService.findById(testOrder.getId());

        assertNotNull("Found order must be not null", foundOrder);
        assertEquals("Returned order id must be equal to test order id", testOrder.getId(),foundOrder.getId());
        assertEquals("Returned order status must be changed", Status.RETURNED, foundOrder.getStatus());
    }

    @Test
    public void findByBook_mustReturnNotNullOrdersList() {
        final List<Order> foundOrders = testService.findByBook(testOrder.getBook());
        assertNotNull("Returned value must be not null", foundOrders);
        for (Order foundOrder : foundOrders) {
            assertEquals("Found order must have passed reader id", testOrder.getUser().getId(), foundOrder.getUser().getId());
        }
    }

    @Test
    public void findByUser_mustReturnNotNullOrdersList() {
        final List<Order> foundOrders = testService.findByUser(testOrder.getUser());
        assertNotNull("Returned value must be not null", foundOrders);
        for (Order foundOrder : foundOrders) {
            assertEquals("Found order must have passed book id", testOrder.getBook().getId(), foundOrder.getBook().getId());
        }
    }

    @Test
    public void findById_mustReturnSavedOrderById() {
        final Order foundOrder = testService.findById(testOrder.getId());
        assertNotNull("Found order must be not null", foundOrder);
        assertEquals("Found order must be equal to saved order", testOrder, foundOrder);
    }

    @Test(expected = ServiceException.class)
    public void findById_mustThrowException_whenThereIsNoOrderWithSpecifiedId() {
        testService.delete(testOrder.getId());
        testService.findById(testOrder.getId());
    }
}