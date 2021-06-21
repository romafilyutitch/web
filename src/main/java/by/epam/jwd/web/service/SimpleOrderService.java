package by.epam.jwd.web.service;

import by.epam.jwd.web.dao.BookDao;
import by.epam.jwd.web.dao.DAOFactory;
import by.epam.jwd.web.dao.OrderDao;
import by.epam.jwd.web.dao.UserDao;
import by.epam.jwd.web.exception.PaginationException;
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

    private static final OrderDao ORDER_DAO = DAOFactory.getInstance().getOrderDao();
    private static final UserDao USER_DAO = DAOFactory.getInstance().getUserDao();
    private static final BookDao BOOK_DAO = DAOFactory.getInstance().getBookDao();

    private SimpleOrderService() {
    }

    public static SimpleOrderService getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public List<Order> findAll() {
        final List<Order> foundAllOrders = ORDER_DAO.findAll();
        logger.info("All orders was found");
        return foundAllOrders;
    }

    @Override
    public Order register(Order order) throws RegisterException {
        if (order.getBook().getCopiesAmount() == 0) {
            logger.info("Trying to order book but there is no free copy");
            throw new RegisterException("There is no free copy of this book");
        }
        final Subscription subscription = order.getUser().getSubscription();
        final Order savedOrder = ORDER_DAO.save(order);
        if (subscription != null) {
            if (subscription.getStartDate().isBefore(LocalDate.now()) && subscription.getEndDate().isAfter(LocalDate.now())) {
                return ORDER_DAO.update(savedOrder.updateOrderStatus(Status.APPROVED));
            }
        }
        logger.info(String.format("Order was register %s", order));
        return savedOrder;
    }

    @Override
    public List<Order> findPage(int currentPage) throws PaginationException {
        if (currentPage < 1 || currentPage > getPagesAmount()) {
            logger.info("Trying to find orders page but page does not exist");
            throw new PaginationException("There is no such page");
        }
        final List<Order> foundPage = ORDER_DAO.findPage(currentPage);
        logger.info(String.format("Page of orders number %d was found", currentPage));
        return foundPage;
    }

    @Override
    public int getPagesAmount() {
        return ORDER_DAO.getPagesAmount();
    }

    @Override
    public Order approveOrder(Long orderId) throws ServiceException {
        final Order order = findById(orderId);
        final Order approvedOrder = ORDER_DAO.update(order.updateOrderStatus(Status.APPROVED));
        logger.info(String.format("Order %s was approved", approvedOrder));
        return approvedOrder;
    }

    @Override
    public void delete(Long orderId) {
        ORDER_DAO.delete(orderId);
        logger.info(String.format("Order with id %d was deleted", orderId));
    }

    @Override
    public Order returnOrder(Long orderId) {
        final Order order = findById(orderId);
        final Order returnedOrder = ORDER_DAO.update(order.updateOrderStatus(Status.RETURNED));
        logger.info(String.format("Order was returned %s", returnedOrder));
        return returnedOrder;
    }

    @Override
    public List<Order> findByReaderId(Long readerId) {
        final List<Order> foundOrdersByReaderId = ORDER_DAO.findOrdersByUserId(readerId);
        logger.info(String.format("Orders by reader id %d was found", readerId));
        return foundOrdersByReaderId;
    }

    @Override
    public List<Order> findByBookId(Long bookId) {
        final List<Order> foundOrdersByBookId = ORDER_DAO.findOrdersByBookId(bookId);
        logger.info(String.format("Orders by book id %d was found", bookId));
        return foundOrdersByBookId;
    }

    @Override
    public Order findById(Long orderId) {
        final Optional<Order> optionalBookOrder = ORDER_DAO.findById(orderId);
        if (!optionalBookOrder.isPresent()) {
            logger.error(String.format("Saved order with id %d was not found", orderId));
            throw new ServiceException(String.format("Saved order with id %d was not found", orderId));
        }
        final Order foundByIdOrder = optionalBookOrder.get();
        logger.info(String.format("Order by id was found %s", foundByIdOrder));
        return foundByIdOrder;
    }

    private static class Singleton {
        private static final SimpleOrderService INSTANCE = new SimpleOrderService();
    }
}
