package by.epam.jwd.web.service.impl;

import by.epam.jwd.web.dao.api.DAOFactory;
import by.epam.jwd.web.dao.api.OrderDao;
import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Order;
import by.epam.jwd.web.model.Status;
import by.epam.jwd.web.model.Subscription;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.api.OrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service implementation for order service interface.
 * Makes all operation related with book order.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class SimpleOrderService implements OrderService {
    private static final Logger logger = LogManager.getLogger(SimpleOrderService.class);

    private final OrderDao orderDao = DAOFactory.getInstance().getOrderDao();

    private static final String ALL_ORDERS_WERE_FOUND_MESSAGE = "All orders were found size = %d";
    private static final String ORDER_WAS_REGISTERED_MESSAGE = "Order was register %s";
    private static final String PAGE_OF_ORDERS_WAS_FOUND_MESSAGE = "Page of orders number %d was found size = %d";
    private static final String ORDER_WAS_APPROVED_MESSAGE = "Order was approved %s";
    private static final String ORDER_WAS_DELETED_MESSAGE = "Order was deleted %s";
    private static final String ORDER_WAS_RETURNED_MESSAGE = "Order was returned %s";
    private static final String ORDERS_BY_USER_WERE_FOUND_MESSAGE = "Orders by user was found %s size = %d";
    private static final String ORDERS_BY_BOOK_WERE_FOUND_MESSAGE = "Orders by book was found %s size = %d";
    private static final String ORDER_BY_ID_WAS_NOT_FOUND_MESSAGE = "Saved order with id %d was not found";
    private static final String ORDER_BY_ID_WAS_FOUND_MESSAGE = "Order was found by id %s";

    private SimpleOrderService() {
    }

    /**
     * Gets single class instance from nested class.
     * @return class instance.
     */
    public static SimpleOrderService getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Finds and returns result of find all orders.
     * @return all saved orders collection.
     */
    @Override
    public List<Order> findAll() {
        final List<Order> allFoundOrders = orderDao.findAll();
        logger.info(String.format(ALL_ORDERS_WERE_FOUND_MESSAGE, allFoundOrders.size()));
        return allFoundOrders;
    }

    /**
     * Makes order save and assigns generated id to order.
     * Check weather order date is matches in user subscription
     * dates range and set order status to approve so user can
     * order and read book at once. If order date not matches in user
     * subscription range or user dont have subscription then user have to
     * make order and wait for approve by librarian or admin.
     * @param order that need to be saved.
     * @return saved order with assigned id.
     */
    @Override
    public Order save(Order order) {
        final Subscription subscription = order.getUser().getSubscription();
        Order savedOrder = orderDao.save(order);
        if (subscription != null) {
            final boolean isNowDateInSubscriptionRange = subscription.getStartDate().isBefore(LocalDate.now()) && subscription.getEndDate().isAfter(LocalDate.now());
            final boolean isNowDateIsSubscriptionStartDate = subscription.getStartDate().isEqual(LocalDate.now());
            final boolean isNowDateIsSubscriptionEndDate = subscription.getEndDate().isEqual(LocalDate.now());
            if (isNowDateIsSubscriptionStartDate || isNowDateInSubscriptionRange || isNowDateIsSubscriptionEndDate) {
                final Order savedOrderWithApprovedStatus = new Order(savedOrder.getId(), savedOrder.getUser(), savedOrder.getBook(), savedOrder.getOrderDate(), Status.APPROVED);
                return orderDao.update(savedOrderWithApprovedStatus);
            }
        }
        logger.info(String.format(ORDER_WAS_REGISTERED_MESSAGE, savedOrder));
        return savedOrder;
    }

    /**
     * Finds orders on passed page.
     * @throws IllegalArgumentException if passed page number is negative or
     * passed page number is greater then pages amount
     * @param currentPage number entities page that need to be found.
     * @return orders on passed page collection.
     */
    @Override
    public List<Order> findPage(int currentPage) {
        if (currentPage <= 0 || currentPage > getPagesAmount()) {
            throw new IllegalArgumentException();
        }
        final List<Order> foundPage = orderDao.findPage(currentPage);
        logger.info(String.format(PAGE_OF_ORDERS_WAS_FOUND_MESSAGE, currentPage, foundPage.size()));
        return foundPage;
    }

    /**
     * Calculates saved orders pages amount.
     * @return pages amount.
     */
    @Override
    public int getPagesAmount() {
        return orderDao.getPagesAmount();
    }

    /**
     * Makes order approve of passed order.
     * @param order order that need to approve.
     */
    @Override
    public void approveOrder(Order order) {
        final Order approvedOrder = new Order(order.getId(), order.getUser(), order.getBook(), order.getOrderDate(), Status.APPROVED);
        final Order updatedOrder = orderDao.update(approvedOrder);
        logger.info(String.format(ORDER_WAS_APPROVED_MESSAGE, updatedOrder));
    }

    /**
     * Deletes saved order
     * @param order Order that need to be deleted.
     */
    @Override
    public void delete(Order order) {
        orderDao.delete(order.getId());
        logger.info(String.format(ORDER_WAS_DELETED_MESSAGE, order));
    }

    /**
     * Makes order return of passed order.
     * @param order order that need to return.
     */
    @Override
    public void returnOrder(Order order) {
        final Order orderToUpdate = new Order(order.getId(), order.getUser(), order.getBook(), order.getOrderDate(), Status.RETURNED);
        final Order returnedOrder = orderDao.update(orderToUpdate);
        logger.info(String.format(ORDER_WAS_RETURNED_MESSAGE, returnedOrder));
    }

    /**
     * Finds and returns result of find orders that passed user made.
     * @param user whose orders need to be found.
     * @return orders that passed user made collection.
     */
    @Override
    public List<Order> findByUser(User user) {
        final List<Order> foundOrdersByUser = orderDao.findByUser(user);
        logger.info(String.format(ORDERS_BY_USER_WERE_FOUND_MESSAGE, user, foundOrdersByUser.size()));
        return foundOrdersByUser;
    }

    /**
     * Finds and returns result of find orders that have passed book.
     * @param book which orders need to be found.
     * @return orders that have passed book collection.
     */
    @Override
    public List<Order> findByBook(Book book) {
        final List<Order> foundOrdersByBook = orderDao.findByBook(book);
        logger.info(String.format(ORDERS_BY_BOOK_WERE_FOUND_MESSAGE, book, foundOrdersByBook.size()));
        return foundOrdersByBook;
    }

    /**
     * Finds saved order that has passed id.
     * @param orderId order that has passed id.
     * @return order that has passed id.
     */
    @Override
    public Order findById(Long orderId) {
        final Optional<Order> optionalBookOrder = orderDao.findById(orderId);
        if (!optionalBookOrder.isPresent()) {
            logger.error(String.format(ORDER_BY_ID_WAS_NOT_FOUND_MESSAGE, orderId));
            throw new ServiceException(String.format(ORDER_BY_ID_WAS_NOT_FOUND_MESSAGE, orderId));
        }
        Order foundOrder = optionalBookOrder.get();
        logger.info(String.format(ORDER_BY_ID_WAS_FOUND_MESSAGE, foundOrder));
        return foundOrder;
    }

    /**
     * Nested class that encapsulates single {@link SimpleOrderService} instance.
     * Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final SimpleOrderService INSTANCE = new SimpleOrderService();
    }
}
