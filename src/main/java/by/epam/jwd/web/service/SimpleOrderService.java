package by.epam.jwd.web.service;

import by.epam.jwd.web.dao.AuthorDao;
import by.epam.jwd.web.dao.BookDao;
import by.epam.jwd.web.dao.DAOFactory;
import by.epam.jwd.web.dao.OrderDao;
import by.epam.jwd.web.dao.UserDao;
import by.epam.jwd.web.exception.RegisterException;
import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.model.Author;
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
import java.util.stream.Collectors;

class SimpleOrderService implements OrderService {
    private static final Logger logger = LogManager.getLogger(SimpleOrderService.class);

    private static final OrderDao ORDER_DAO = DAOFactory.getInstance().getOrderDao();
    private static final UserDao USER_DAO = DAOFactory.getInstance().getUserDao();
    private static final BookDao BOOK_DAO = DAOFactory.getInstance().getBookDao();
    private static final AuthorDao AUTHOR_DAO = DAOFactory.getInstance().getAuthorDao();

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
    private static final String USER_WAS_NOT_FOUND_MESSAGE = "Saved user with id %d was not found";
    private static final String BOOK_WAS_NOT_FOUND_MESSAGE = "Saved book with %d was not found";
    private static final String AUTHOR_WAS_NOT_FOUND_MESSAGE = "Saved author with id %d was not found";

    private SimpleOrderService() {
    }

    public static SimpleOrderService getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public List<Order> findAll() {
        List<Order> allFoundOrders = ORDER_DAO.findAll();
        allFoundOrders = fillWithReader(allFoundOrders);
        allFoundOrders = fillWithBook(allFoundOrders);
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
        Order savedOrder = ORDER_DAO.save(order);
        savedOrder = fillWithReader(savedOrder);
        savedOrder = fillWithBook(savedOrder);
        if (subscription != null) {
            final boolean isNowDateInSubscriptionRange = subscription.getStartDate().isBefore(LocalDate.now()) && subscription.getEndDate().isAfter(LocalDate.now());
            final boolean isNowDateIsSubscriptionStartDate = subscription.getStartDate().isEqual(LocalDate.now());
            final boolean isNowDateIsSubscriptionEndDate = subscription.getEndDate().isEqual(LocalDate.now());
            if (isNowDateIsSubscriptionStartDate || isNowDateInSubscriptionRange || isNowDateIsSubscriptionEndDate) {
                return ORDER_DAO.update(savedOrder.updateOrderStatus(Status.APPROVED));
            }
        }
        logger.info(String.format(ORDER_WAS_REGISTERED_MESSAGE, savedOrder));
        return savedOrder;
    }

    @Override
    public List<Order> findPage(int currentPage) {
        List<Order> foundPage;
        if (currentPage < 1) {
            foundPage = ORDER_DAO.findPage(1);
        } else if (currentPage >= getPagesAmount()) {
            foundPage = ORDER_DAO.findPage(getPagesAmount());
        } else {
            foundPage = ORDER_DAO.findPage(currentPage);
        }
        foundPage = fillWithReader(foundPage);
        foundPage = fillWithBook(foundPage);
        logger.info(String.format(PAGE_OF_ORDERS_WAS_FOUND_MESSAGE, currentPage));
        return foundPage;
    }

    @Override
    public int getPagesAmount() {
        return ORDER_DAO.getPagesAmount();
    }

    @Override
    public void approveOrder(Order order) throws ServiceException {
        final Order approvedOrder = ORDER_DAO.update(order.updateOrderStatus(Status.APPROVED));
        logger.info(String.format(ORDER_WAS_APPROVED_MESSAGE, approvedOrder));
    }

    @Override
    public void delete(Long orderId) {
        ORDER_DAO.delete(orderId);
        logger.info(String.format(ORDER_WAS_DELETED_MESSAGE, orderId));
    }

    @Override
    public void returnOrder(Order order) {
        final Order returnedOrder = ORDER_DAO.update(order.updateOrderStatus(Status.RETURNED));
        logger.info(String.format(ORDER_WAS_RETURNED_MESSAGE, returnedOrder));
    }

    @Override
    public List<Order> findByReaderId(Long readerId) {
        List<Order> foundOrdersByReaderId = ORDER_DAO.findOrdersByUserId(readerId);
        foundOrdersByReaderId = fillWithReader(foundOrdersByReaderId);
        foundOrdersByReaderId = fillWithBook(foundOrdersByReaderId);
        logger.info(String.format(ORDERS_BY_READER_ID_WERE_FOUND_MESSAGE, readerId));
        return foundOrdersByReaderId;
    }

    @Override
    public List<Order> findByBookId(Long bookId) {
        List<Order> foundOrdersByBookId = ORDER_DAO.findOrdersByBookId(bookId);
        foundOrdersByBookId = fillWithReader(foundOrdersByBookId);
        foundOrdersByBookId = fillWithBook(foundOrdersByBookId);
        logger.info(String.format(ORDERS_BY_BOOK_WERE_FOUND_MESSAGE, bookId));
        return foundOrdersByBookId;
    }

    @Override
    public Order findById(Long orderId) {
        final Optional<Order> optionalBookOrder = ORDER_DAO.findById(orderId);
        if (!optionalBookOrder.isPresent()) {
            logger.error(String.format(ORDER_BY_ID_WAS_NOT_FOUND_MESSAGE, orderId));
            throw new ServiceException(String.format(ORDER_BY_ID_WAS_NOT_FOUND_MESSAGE, orderId));
        }
        Order foundOrder = optionalBookOrder.get();
        foundOrder = fillWithReader(foundOrder);
        foundOrder = fillWithBook(foundOrder);
        logger.info(String.format(ORDER_BY_ID_WAS_FOUND_MESSAGE, foundOrder));
        return foundOrder;
    }

    private List<Order> fillWithReader(List<Order> orders) {
        return orders.stream().map(this::fillWithReader).collect(Collectors.toList());
    }

    private List<Order> fillWithBook(List<Order> orders) {
        return orders.stream().map(this::fillWithBook).collect(Collectors.toList());
    }

    private Order fillWithReader(Order order) {
        final Optional<User> optionalUser = USER_DAO.findById(order.getUser().getId());
        if (!optionalUser.isPresent()) {
            throw new ServiceException(String.format(USER_WAS_NOT_FOUND_MESSAGE, order.getUser().getId()));
        }
        return order.updateUser(optionalUser.get());
    }

    private Order fillWithBook(Order order) {
        final Optional<Book> optionalBook = BOOK_DAO.findById(order.getBook().getId());
        if (!optionalBook.isPresent()) {
            throw new ServiceException(String.format(BOOK_WAS_NOT_FOUND_MESSAGE, order.getBook().getId()));
        }
        Book foundBook = optionalBook.get();
        final Optional<Author> optionalAuthor = AUTHOR_DAO.findById(foundBook.getAuthor().getId());
        if (!optionalAuthor.isPresent()) {
            throw new ServiceException(String.format(AUTHOR_WAS_NOT_FOUND_MESSAGE, foundBook.getAuthor().getId()));
        }
        foundBook = foundBook.updateAuthor(optionalAuthor.get());
        return order.updateBook(foundBook);
    }

    private static class Singleton {
        private static final SimpleOrderService INSTANCE = new SimpleOrderService();
    }
}
