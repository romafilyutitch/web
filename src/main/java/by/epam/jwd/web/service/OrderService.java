package by.epam.jwd.web.service;

import by.epam.jwd.web.model.Order;

import java.util.List;

public interface OrderService {
    List<Order> findAllOrders();

    Order registerBookOrder(Order order);

    Order approveOrder(Long orderId);

    Order findById(Long orderId);

    void deleteOrder(Long orderId);

    List<Order> findByUserId(Long userId);
}
