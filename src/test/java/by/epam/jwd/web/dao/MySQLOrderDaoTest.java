package by.epam.jwd.web.dao;

import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.connectionPool.ConnectionPoolInitializationException;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;
import by.epam.jwd.web.model.Order;
import by.epam.jwd.web.model.Status;
import by.epam.jwd.web.model.User;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class MySQLOrderDaoTest {
    private User testUser = new User("test user", "test user");
    private Book testBook = new Book("test book", "test book", Genre.FANTASY, 1, "text");
    private Order testOrder;

    private final MySQLOrderDao testDao = MySQLOrderDao.getInstance();

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
        testUser = MySQLUserDao.getInstance().save(testUser);
        testBook = MySQLBookDao.getInstance().save(testBook);
        testOrder = new Order(testUser, testBook);
        testOrder = testDao.save(testOrder);
    }

    @After
    public void tearDown() throws Exception {
        testDao.delete(testOrder.getId());
        MySQLUserDao.getInstance().delete(testUser.getId());
        MySQLBookDao.getInstance().delete(testBook.getId());
    }

    @Test
    public void save_mustAssignIdToSavedOrder() {
        assertNotNull(testOrder);
        assertNotNull(testOrder.getId());
    }

    @Test
    public void findAll_mustReturnNotNullOrdersList() {
        final List<Order> allOrders = testDao.findAll();
        assertNotNull(allOrders);
    }

    @Test
    public void findById_mustReturnSavedOrder_whenSavedOrderIdWasPassed() {
        final Optional<Order> optionalOrder = testDao.findById(testOrder.getId());
        assertNotNull(optionalOrder);
        assertTrue(optionalOrder.isPresent());
        assertEquals(testOrder, optionalOrder.get());
    }

    @Test
    public void findById_mustReturnEmptyOptionalOrder_whenThereIsNotOrderWithPassedId() {
        testDao.delete(testOrder.getId());
        final Optional<Order> optionalOrder = testDao.findById(testOrder.getId());
        assertNotNull(optionalOrder);
        assertFalse(optionalOrder.isPresent());
    }

    @Test
    public void update_mustUpdateOrderStatus() {
        Status status = Status.APPROVED;
        testOrder = new Order(testOrder.getId(), testOrder.getUser(), testOrder.getBook(), testOrder.getOrderDate(), status);
        final Order updatedOrder = testDao.update(testOrder);
        assertNotNull(updatedOrder);
        assertEquals(testOrder, updatedOrder);
    }

    @Test
    public void delete_mustDeleteTestOrder() {
        testDao.delete(testOrder.getId());
        final List<Order> allOrder = testDao.findAll();
        assertFalse(allOrder.contains(testOrder));
    }

    @Test
    public void findPage_mustReturnNotNullPage() {
        final int pagesAmount = testDao.getPagesAmount();
        final List<Order> foundPage = testDao.findPage(pagesAmount);
        assertNotNull(foundPage);
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
    public void getInstance_mustReturnSameInstanceAsTestInstance() {
        final MySQLOrderDao instance = MySQLOrderDao.getInstance();
        assertSame(testDao, instance);
    }

    @Test
    public void findOrdersByBookId_mustReturnNotNullList() {
        final List<Order> foundOrders = testDao.findByBook(testOrder.getBook());
        assertNotNull(foundOrders);
        assertFalse(foundOrders.contains(null));
        for (Order foundOrder : foundOrders) {
            assertEquals(testOrder.getBook().getId(), foundOrder.getBook().getId());
        }
    }

    @Test
    public void findOrdersByBookId_mustReturnListThatContainSavedOrder_whenSavedOrderBookIdPassed() {
        final List<Order> foundOrders = testDao.findByBook(testBook);
        assertTrue(foundOrders.contains(testOrder));
    }

    @Test
    public void findOrdersByOrderDate_mustReturnNotNullList() {
        final List<Order> foundOrders = testDao.findByOrderDate(testOrder.getOrderDate());
        assertNotNull(foundOrders);
        assertFalse(foundOrders.contains(null));
        for (Order foundOrder : foundOrders) {
            assertEquals(testOrder.getOrderDate(), foundOrder.getOrderDate());
        }
    }

    @Test
    public void findOrdersByOrderDate_mustReturnListThatContainSavedOrder_whenSavedOrderDatePassed() {
        final List<Order> foundOrders = testDao.findByOrderDate(testOrder.getOrderDate());
        assertTrue(foundOrders.contains(testOrder));
    }

    @Test
    public void findOrdersByUserId_mustReturnNotNullList() {
        final List<Order> foundOrders = testDao.findByUser(testOrder.getUser());
        assertNotNull(foundOrders);
        for (Order foundOrder : foundOrders) {
            assertEquals(testOrder.getUser().getId(), foundOrder.getUser().getId());
        }
    }

    @Test
    public void findOrdersByUserId_mustReturnListThatContainSavedOrder_whenSavedOrderUserIdPassed() {
        final List<Order> foundOrders = testDao.findByUser(testOrder.getUser());
        assertTrue(foundOrders.contains(testOrder));
    }
}