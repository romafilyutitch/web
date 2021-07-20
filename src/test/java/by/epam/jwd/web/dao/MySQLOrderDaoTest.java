package by.epam.jwd.web.dao;

import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.exception.ConnectionPoolInitializationException;
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
import java.util.Optional;

import static org.junit.Assert.*;

public class MySQLOrderDaoTest {
    private static final ConnectionPool POOL = ConnectionPool.getConnectionPool();
    private final User testUser = MySQLUserDao.getInstance().findAll().stream().findAny().get();
    private final Book testBook = MySQLBookDao.getInstance().findAll().stream().findAny().get();
    private Order testOrder = new Order(testUser, testBook, LocalDate.now(), Status.ORDERED);

    private final MySQLOrderDao testDao = MySQLOrderDao.getInstance();

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
        testOrder = testDao.save(testOrder);
    }

    @After
    public void tearDown() throws Exception {
        testDao.delete(testOrder.getId());
    }

    @Test
    public void save_mustAssignIdToSavedOrder() {
        assertNotNull("Saved order must be not null", testOrder);
        assertNotNull("Saved order id must be not null", testOrder.getId());
    }

    @Test
    public void findAll_mustReturnNotNullOrdersList() {
        final List<Order> allOrders = testDao.findAll();
        assertNotNull("Returned value must be not null", allOrders);
    }

    @Test
    public void findById_mustReturnSavedOrder_whenSavedOrderIdWasPassed() {
        final Optional<Order> optionalOrder = testDao.findById(testOrder.getId());
        assertNotNull("Returned value must be not null", optionalOrder);
        assertTrue("Optional order must contain saved order", optionalOrder.isPresent());
        assertEquals("Returned order must be equal to test order", testOrder, optionalOrder.get());
    }

    @Test
    public void findById_mustReturnEmptyOptionalOrder_whenThereIsNotOrderWithPassedId() {
        testDao.delete(testOrder.getId());
        final Optional<Order> optionalOrder = testDao.findById(testOrder.getId());
        assertNotNull("Returned value must be not null", optionalOrder);
        assertFalse("Optional order must be empty", optionalOrder.isPresent());
    }

    @Test
    public void update_mustUpdateOrderStatus() {
        Status status = Status.APPROVED;
        testOrder = new Order(testOrder.getId(), testOrder.getUser(), testOrder.getBook(), testOrder.getOrderDate(), status);
        final Order updatedOrder = testDao.update(testOrder);
        assertNotNull("Returned value must be not null", updatedOrder);
        assertEquals("Updated order must be equal to test order", testOrder, updatedOrder);
    }

    @Test
    public void delete_mustDeleteTestOrder() {
        testDao.delete(testOrder.getId());
        final List<Order> allOrder = testDao.findAll();
        assertFalse("All orders list must not contain test order", allOrder.contains(testOrder));
    }

    @Test
    public void findPage_mustReturnNotNullPage() {
        final int pagesAmount = testDao.getPagesAmount();
        final List<Order> foundPage = testDao.findPage(pagesAmount);
        assertNotNull("Found page must be not null", foundPage);
    }

    @Test
    public void getRowsAmount_mustReturnNotNegativeNumber() {
        final int rowsAmount = testDao.getRowsAmount();
        assertTrue("Returned value must be not negative", rowsAmount >= 0);
    }

    @Test
    public void getPagesAmount_mustReturnNotNegativeNumber() {
        final int pagesAmount = testDao.getPagesAmount();
        assertTrue("Returned value must be not negative", pagesAmount >= 0);
    }

    @Test
    public void getInstance_mustReturnSameInstanceAsTestInstance() {
        final MySQLOrderDao instance = MySQLOrderDao.getInstance();
        assertSame("Returned value must be same as test instance", testDao, instance);
    }

    @Test
    public void findOrdersByBookId_mustReturnNotNullList() {
        final List<Order> foundOrders = testDao.findByBook(testOrder.getBook());
        assertNotNull("Returned value must be not null", foundOrders);
        assertFalse("Returned list must not contain null", foundOrders.contains(null));
        for(Order foundOrder : foundOrders) {
            assertEquals("Found order book id must be equal to passed book id",testOrder.getBook().getId(), foundOrder.getBook().getId());
        }
    }

    @Test
    public void findOrdersByBookId_mustReturnListThatContainSavedOrder_whenSavedOrderBookIdPassed() {
        final List<Order> foundOrders = testDao.findByBook(testBook);
        assertTrue("Found orders list must contain saved order", foundOrders.contains(testOrder));
    }

    @Test
    public void findOrdersByOrderDate_mustReturnNotNullList() {
        final List<Order> foundOrders = testDao.findByOrderDate(testOrder.getOrderDate());
        assertNotNull("Returned value must be not null", foundOrders);
        assertFalse("Returned list must not contain null", foundOrders.contains(null));
        for (Order foundOrder : foundOrders) {
            assertEquals("Found order date must be equal to passed date",testOrder.getOrderDate(), foundOrder.getOrderDate());
        }
    }

    @Test
    public void findOrdersByOrderDate_mustReturnListThatContainSavedOrder_whenSavedOrderDatePassed() {
        final List<Order> foundOrders = testDao.findByOrderDate(testOrder.getOrderDate());
        assertTrue("Found orders list must contain saved order", foundOrders.contains(testOrder));
    }

    @Test
    public void findOrdersByUserId_mustReturnNotNullList() {
        final List<Order> foundOrders = testDao.findByUser(testOrder.getUser());
        assertNotNull("Returned value must be not null", foundOrders);
        assertFalse("Returned lust must not contain null", foundOrders.contains(null));
        for (Order foundOrder : foundOrders) {
            assertEquals("Found order user id must be equal to passed user id", testOrder.getUser().getId(), foundOrder.getUser().getId());
        }
    }

    @Test
    public void findOrdersByUserId_mustReturnListThatContainSavedOrder_whenSavedOrderUserIdPassed() {
        final List<Order> foundOrders = testDao.findByUser(testOrder.getUser());
        assertTrue("Found order list must contain saved order", foundOrders.contains(testOrder));
    }
}