package by.epam.jwd.web.dao;


import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Order;
import by.epam.jwd.web.model.User;

import java.time.LocalDate;
import java.util.List;

public interface OrderDao extends Dao<Order> {

    List<Order> findByUser(User user);

    List<Order> findByBook(Book book);

    List<Order> findByOrderDate(LocalDate orderDate);
}
