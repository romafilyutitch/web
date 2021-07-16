package by.epam.jwd.web.service;

import by.epam.jwd.web.exception.RegisterException;
import by.epam.jwd.web.model.Order;

import java.util.List;

public interface OrderService extends Service<Order> {

    void approveOrder(Order order);

    void returnOrder(Order order);

    List<Order> findByBookId(Long readerId);

    List<Order> findByReaderId(Long bookId);
}
