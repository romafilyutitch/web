package by.epam.jwd.web.service;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Order;
import by.epam.jwd.web.model.User;

import java.util.List;

public interface OrderService extends Service<Order> {

    void approveOrder(Order order);

    void returnOrder(Order order);

    List<Order> findByBook(Book book);

    List<Order> findByUser(User user);
}
