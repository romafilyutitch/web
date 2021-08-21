package by.epam.jwd.web.service;

import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.exception.ConnectionPoolInitializationException;
import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;
import by.epam.jwd.web.model.Order;
import by.epam.jwd.web.model.Status;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.impl.SimpleBookService;
import by.epam.jwd.web.service.impl.SimpleOrderService;
import by.epam.jwd.web.service.impl.SimpleUserService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SimpleOrderServiceTest {
    private final SimpleOrderService testService = SimpleOrderService.getInstance();
    private User testUser = new User("test user", "test user");
    private Book testBook = new Book("test book", "test book", Genre.FANTASY, 1, "text");

    private Order testOrder;

    @BeforeClass
    public static void initPool() throws ConnectionPoolInitializationException {
        ConnectionPool.getConnectionPool().init();
    }

    @AfterClass
    public static void destroyPool() {
        ConnectionPool.getConnectionPool().destroy();
    }

    @Before
    public void setUp() throws Exception {
        testUser = SimpleUserService.getInstance().save(testUser);
        testBook = SimpleBookService.getInstance().save(testBook);
        testOrder = new Order(testUser, testBook);
        testOrder = testService.save(testOrder);
    }

    @After
    public void tearDown() throws Exception {
        testService.delete(testOrder);
        SimpleUserService.getInstance().delete(testUser);
        SimpleBookService.getInstance().delete(testBook);
    }

    @Test
    public void getInstance_mustReturnSameInstance() {
        final SimpleOrderService instance = SimpleOrderService.getInstance();
        assertEquals(instance, testService);
    }

    @Test
    public void findAll_mustReturnNotNullList() {
        final List<Order> allOrders = testService.findAll();
        assertNotNull(allOrders);
        assertTrue(allOrders.contains(testOrder));
    }

    @Test
    public void register_mustReturnOrderWithAssignedIdStatusAndDate() {
        assertNotNull(testOrder);
        assertNotNull(testOrder.getId());
        assertTrue(testOrder.getStatus().equals(Status.ORDERED) || testOrder.getStatus().equals(Status.APPROVED));
        assertEquals(LocalDate.now(), testOrder.getOrderDate());
    }

    @Test
    public void findPage_mustReturnNotNullPageList() {
        final int pagesAmount = testService.getPagesAmount();
        final List<Order> foundPage = testService.findPage(pagesAmount);
        assertNotNull(foundPage);
    }

    @Test
    public void getPagesAmount_mustReturnNotNegativeNumber() {
        final int pagesAmount = testService.getPagesAmount();
        assertTrue(pagesAmount >= 0);
    }

    @Test
    public void approveOrder_mustChangeOrderStatusToApprovedStatus() {
        testService.approveOrder(testOrder);
        final Order foundOrder = testService.findById(testOrder.getId());

        assertNotNull(foundOrder);
        assertEquals(testOrder.getId(), foundOrder.getId());
        assertEquals(Status.APPROVED, foundOrder.getStatus());
    }

    @Test
    public void delete_mustDeleteOrderWithSpecifiedId() {
        testService.delete(testOrder);
        final List<Order> allOrders = testService.findAll();
        assertFalse(allOrders.contains(testOrder));
    }

    @Test
    public void returnOrder_mustChangeStatusToReturnedStatus() {
        testService.returnOrder(testOrder);
        final Order foundOrder = testService.findById(testOrder.getId());

        assertNotNull(foundOrder);
        assertEquals(testOrder.getId(), foundOrder.getId());
        assertEquals(Status.RETURNED, foundOrder.getStatus());
    }

    @Test
    public void findByBook_mustReturnNotNullOrdersList() {
        final List<Order> foundOrders = testService.findByBook(testOrder.getBook());
        assertNotNull(foundOrders);
        for (Order foundOrder : foundOrders) {
            assertEquals(testOrder.getUser().getId(), foundOrder.getUser().getId());
        }
    }

    @Test
    public void findByUser_mustReturnNotNullOrdersList() {
        final List<Order> foundOrders = testService.findByUser(testOrder.getUser());
        assertNotNull(foundOrders);
        for (Order foundOrder : foundOrders) {
            assertEquals(testOrder.getBook().getId(), foundOrder.getBook().getId());
        }
    }

    @Test
    public void findById_mustReturnSavedOrderById() {
        final Order foundOrder = testService.findById(testOrder.getId());
        assertNotNull(foundOrder);
        assertEquals(testOrder, foundOrder);
    }

    @Test(expected = ServiceException.class)
    public void findById_mustThrowException_whenThereIsNoOrderWithSpecifiedId() {
        testService.delete(testOrder);
        testService.findById(testOrder.getId());
    }
}