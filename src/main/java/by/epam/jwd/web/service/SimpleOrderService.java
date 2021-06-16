package by.epam.jwd.web.service;

import by.epam.jwd.web.dao.BookDao;
import by.epam.jwd.web.dao.DAOFactory;
import by.epam.jwd.web.dao.OrderDao;
import by.epam.jwd.web.dao.UserDao;
import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.model.Order;
import by.epam.jwd.web.model.Status;
import by.epam.jwd.web.model.Subscription;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

class SimpleOrderService implements OrderService {
    private static final OrderDao ORDER_DAO = DAOFactory.getInstance().getOrderDao();
    private static final UserDao USER_DAO = DAOFactory.getInstance().getUserDao();
    private static final BookDao BOOK_DAO = DAOFactory.getInstance().getBookDao();

    private SimpleOrderService() {
    }

    public static SimpleOrderService getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public List<Order> findAllOrders() {
        return ORDER_DAO.findAll();
    }

    @Override
    public Order registerBookOrder(Order order) {
        final Subscription subscription = order.getUser().getSubscription();
        final Order savedOrder = ORDER_DAO.save(order);
        if (subscription != null) {
            if (subscription.getStartDate().isBefore(LocalDate.now()) && subscription.getEndDate().isAfter(LocalDate.now())) {
                return ORDER_DAO.update(savedOrder.updateOrderStatus(Status.APPROVED));
            }
        }
        return savedOrder;
    }

    @Override
    public Order approveOrder(Long orderId) throws ServiceException {
        final Order order = findById(orderId);
        return ORDER_DAO.update(order.updateOrderStatus(Status.APPROVED));
    }

    @Override
    public void deleteOrder(Long orderId) throws ServiceException {
        ORDER_DAO.delete(orderId);
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return ORDER_DAO.findOrdersByUserId(userId);
    }

    @Override
    public Order findById(Long orderId) {
        final Optional<Order> optionalBookOrder = ORDER_DAO.findById(orderId);
        if (!optionalBookOrder.isPresent()) {
            throw new ServiceException(String.format("Saved order with id %d was not found", orderId));
        }
        return optionalBookOrder.get();
    }

    private static class Singleton {
        private static final SimpleOrderService INSTANCE = new SimpleOrderService();
    }
}
