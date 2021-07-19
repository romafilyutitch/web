package by.epam.jwd.web.service;

import by.epam.jwd.web.dao.DAOFactory;
import by.epam.jwd.web.dao.OrderDao;
import by.epam.jwd.web.exception.RegisterException;
import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.model.Order;
import by.epam.jwd.web.model.Status;
import by.epam.jwd.web.model.Subscription;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

class SimpleOrderService implements OrderService {
    private static final Logger logger = LogManager.getLogger(SimpleOrderService.class);

    private final OrderDao orderDao = DAOFactory.getInstance().getOrderDao();

    private static final String ALL_ORDERS_WERE_FOUND_MESSAGE = "All orders were found";
    private static final String NO_FREE_COPY_MESSAGE = "There is no free copy of ordered book %s";
    private static final String ORDER_WAS_REGISTERED_MESSAGE = "Order was register %s";
    private static final String PAGE_OF_ORDERS_WAS_FOUND_MESSAGE = "Page of orders number %d was found";
    private static final String ORDER_WAS_APPROVED_MESSAGE = "Order %s was approved";
    private static final String ORDER_WAS_DELETED_MESSAGE = "Order with id %d was deleted";
    private static final String ORDER_WAS_RETURNED_MESSAGE = "Order was returned %s";
    private static final String ORDERS_BY_READER_ID_WERE_FOUND_MESSAGE = "Orders by reader id %d was found";
    private static final String ORDERS_BY_BOOK_WERE_FOUND_MESSAGE = "Orders by book id %d was found";
    private static final String ORDER_BY_ID_WAS_NOT_FOUND_MESSAGE = "Saved order with id %d was not found";
    private static final String ORDER_BY_ID_WAS_FOUND_MESSAGE = "Order by id was found %s";

    private SimpleOrderService() {
    }

    public static SimpleOrderService getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public List<Order> findAll() {
        List<Order> allFoundOrders = orderDao.findAll();
        logger.info(ALL_ORDERS_WERE_FOUND_MESSAGE);
        return allFoundOrders;
    }

    @Override
    public Order register(Order order) throws RegisterException {
        if (order.getBook().getCopiesAmount() == 0) {
            logger.info(String.format(NO_FREE_COPY_MESSAGE, order.getBook().getName()));
            throw new RegisterException(String.format(NO_FREE_COPY_MESSAGE, order.getBook().getName()));
        }
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
        logger.info(String.format(PAGE_OF_ORDERS_WAS_FOUND_MESSAGE, currentPage));
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
    public List<Order> findByReaderId(Long readerId) {
        List<Order> foundOrdersByReaderId = orderDao.findOrdersByUserId(readerId);
        logger.info(String.format(ORDERS_BY_READER_ID_WERE_FOUND_MESSAGE, readerId));
        return foundOrdersByReaderId;
    }

    @Override
    public List<Order> findByBookId(Long bookId) {
        List<Order> foundOrdersByBookId = orderDao.findOrdersByBookId(bookId);
        logger.info(String.format(ORDERS_BY_BOOK_WERE_FOUND_MESSAGE, bookId));
        return foundOrdersByBookId;
    }

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

    private static class Singleton {
        private static final SimpleOrderService INSTANCE = new SimpleOrderService();
    }
}
