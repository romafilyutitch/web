package by.epam.jwd.web.service;

import by.epam.jwd.web.dao.DAOFactory;
import by.epam.jwd.web.dao.OrderDao;
import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Order;
import by.epam.jwd.web.model.Status;
import by.epam.jwd.web.model.Subscription;
import by.epam.jwd.web.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

class SimpleOrderService implements OrderService {
    private static final Logger logger = LogManager.getLogger(SimpleOrderService.class);

    private final OrderDao orderDao = DAOFactory.getInstance().getOrderDao();

    private static final String ALL_ORDERS_WERE_FOUND_MESSAGE = "All orders were found size = %d";
    private static final String ORDER_WAS_REGISTERED_MESSAGE = "Order was register %s";
    private static final String PAGE_OF_ORDERS_WAS_FOUND_MESSAGE = "Page of orders number %d was found size = %d";
    private static final String ORDER_WAS_APPROVED_MESSAGE = "Order was approved %s";
    private static final String ORDER_WAS_DELETED_MESSAGE = "Order with id %d was deleted";
    private static final String ORDER_WAS_RETURNED_MESSAGE = "Order was returned %s";
    private static final String ORDERS_BY_USER_WERE_FOUND_MESSAGE = "Orders by user was found %s size = %d";
    private static final String ORDERS_BY_BOOK_WERE_FOUND_MESSAGE = "Orders by book was found %s size = %d";
    private static final String ORDER_BY_ID_WAS_NOT_FOUND_MESSAGE = "Saved order with id %d was not found";
    private static final String ORDER_BY_ID_WAS_FOUND_MESSAGE = "Order by id %d was found %s";

    private SimpleOrderService() {
    }

    public static SimpleOrderService getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public List<Order> findAll() {
        List<Order> allFoundOrders = orderDao.findAll();
        logger.info(String.format(ALL_ORDERS_WERE_FOUND_MESSAGE, allFoundOrders.size()));
        return allFoundOrders;
    }

    @Override
    public Order register(Order order) {
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

    @Override
    public List<Order> findPage(int currentPage) {
        List<Order> foundPage;
        if (currentPage < 1) {
            foundPage = orderDao.findPage(1);
        } else if (currentPage >= getPagesAmount()) {
            foundPage = orderDao.findPage(getPagesAmount());
        } else {
            foundPage = orderDao.findPage(currentPage);
        }
        logger.info(String.format(PAGE_OF_ORDERS_WAS_FOUND_MESSAGE, currentPage, foundPage.size()));
        return foundPage;
    }

    @Override
    public int getPagesAmount() {
        return orderDao.getPagesAmount();
    }

    @Override
    public void approveOrder(Order order) throws ServiceException {
        final Order approvedOrder = new Order(order.getId(), order.getUser(), order.getBook(), order.getOrderDate(), Status.APPROVED);
        final Order updatedOrder = orderDao.update(approvedOrder);
        logger.info(String.format(ORDER_WAS_APPROVED_MESSAGE, updatedOrder));
    }

    @Override
    public void delete(Long orderId) {
        orderDao.delete(orderId);
        logger.info(String.format(ORDER_WAS_DELETED_MESSAGE, orderId));
    }

    @Override
    public void returnOrder(Order order) {
        final Order returnedOrder = new Order(order.getId(), order.getUser(), order.getBook(), order.getOrderDate(), Status.RETURNED);
        final Order updatedOrder = orderDao.update(returnedOrder);
        logger.info(String.format(ORDER_WAS_RETURNED_MESSAGE, updatedOrder));
    }

    @Override
    public List<Order> findByUser(User user) {
        List<Order> foundOrdersByUser = orderDao.findByUser(user);
        logger.info(String.format(ORDERS_BY_USER_WERE_FOUND_MESSAGE, user, foundOrdersByUser.size()));
        return foundOrdersByUser;
    }

    @Override
    public List<Order> findByBook(Book book) {
        List<Order> foundOrdersByBook = orderDao.findByBook(book);
        logger.info(String.format(ORDERS_BY_BOOK_WERE_FOUND_MESSAGE, book, foundOrdersByBook.size()));
        return foundOrdersByBook;
    }

    @Override
    public Order findById(Long orderId) {
        final Optional<Order> optionalBookOrder = orderDao.findById(orderId);
        if (!optionalBookOrder.isPresent()) {
            logger.error(String.format(ORDER_BY_ID_WAS_NOT_FOUND_MESSAGE, orderId));
            throw new ServiceException(String.format(ORDER_BY_ID_WAS_NOT_FOUND_MESSAGE, orderId));
        }
        Order foundOrder = optionalBookOrder.get();
        logger.info(String.format(ORDER_BY_ID_WAS_FOUND_MESSAGE, orderId, foundOrder));
        return foundOrder;
    }

    private static class Singleton {
        private static final SimpleOrderService INSTANCE = new SimpleOrderService();
    }
}
