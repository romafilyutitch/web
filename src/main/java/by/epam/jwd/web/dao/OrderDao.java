package by.epam.jwd.web.dao;


import by.epam.jwd.web.model.Order;

import java.time.LocalDate;
import java.util.List;

public interface OrderDao extends Dao<Order> {

    List<Order> findOrdersByUserId(Long userId);

    List<Order> findOrdersByBookId(Long bookId);

    List<Order> findOrdersByUserLogin(String userLogin);

    List<Order> findOrdersByOrderDate(LocalDate orderDate);
}
