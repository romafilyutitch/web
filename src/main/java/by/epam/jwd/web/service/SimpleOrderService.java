package by.epam.jwd.web.service;

import by.epam.jwd.web.dao.BookDao;
import by.epam.jwd.web.dao.DaoFactory;
import by.epam.jwd.web.dao.OrderDao;
import by.epam.jwd.web.dao.UserDao;
import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.BookOrder;
import by.epam.jwd.web.model.Status;
import by.epam.jwd.web.model.Subscription;
import by.epam.jwd.web.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

class SimpleOrderService implements OrderService {
    private static final OrderDao ORDER_DAO = DaoFactory.getInstance().getOrderDao();
    private static final UserDao USER_DAO = DaoFactory.getInstance().getUserDao();
    private static final BookDao BOOK_DAO = DaoFactory.getInstance().getBookDao();

    private SimpleOrderService() {
    }

    public static SimpleOrderService getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public List<BookOrder> findAll() {
        return ORDER_DAO.findAll();
    }

    @Override
    public BookOrder createOrder(Long readerId, Long bookId) throws ServiceException {
        final Optional<User> optionalUser = USER_DAO.findById(readerId);
        final Optional<Book> optionalBook = BOOK_DAO.findById(bookId);
        if (!optionalUser.isPresent()) {
            throw new ServiceException(String.format("User with id %d does not exist", readerId));
        }
        if (!optionalBook.isPresent()) {
            throw new ServiceException(String.format("Book with id %d does not exist", bookId));
        }
        if (optionalBook.get().getCopiesAmount() == 0) {
            throw new ServiceException("All copies are given. Try to order this book in other time");
        }
        final User user = optionalUser.get();
        final Subscription subscription = user.getSubscription();
        final BookOrder bookOrder = new BookOrder(user, optionalBook.get());
        final BookOrder savedOrder = ORDER_DAO.save(bookOrder);
        if (subscription != null) {
            if (subscription.getStartDate().isBefore(LocalDate.now()) && subscription.getEndDate().isAfter(LocalDate.now())) {
                return ORDER_DAO.update(savedOrder.updateOrderStatus(Status.APPROVED));
            }
        }
        return savedOrder;
    }

    @Override
    public BookOrder approveOrder(Long orderId) throws ServiceException {
        final Optional<BookOrder> optionalBookOrder = ORDER_DAO.findById(orderId);
        if (!optionalBookOrder.isPresent()) {
            throw new ServiceException(String.format("Order with id %d does not exist", orderId));
        }
        final BookOrder bookOrder = optionalBookOrder.get();
        return ORDER_DAO.update(bookOrder.updateOrderStatus(Status.APPROVED));
    }

    @Override
    public void deleteOrder(Long orderId) throws ServiceException {
        ORDER_DAO.delete(orderId);
    }

    @Override
    public List<BookOrder> findByUserId(Long userId) {
        return ORDER_DAO.findOrdersByUserId(userId);
    }

    @Override
    public BookOrder findById(Long orderId) throws ServiceException {
        final Optional<BookOrder> optionalBookOrder = ORDER_DAO.findById(orderId);
        if (!optionalBookOrder.isPresent()) {
            throw new ServiceException(String.format("order with id %d does not exist", orderId));
        }
        return optionalBookOrder.get();
    }

    private static class Singleton {
        private static final SimpleOrderService INSTANCE = new SimpleOrderService();
    }
}
