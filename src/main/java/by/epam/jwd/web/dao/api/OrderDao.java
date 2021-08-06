package by.epam.jwd.web.dao.api;


import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Order;
import by.epam.jwd.web.model.User;

import java.time.LocalDate;
import java.util.List;

/**
 * Order data access object interface for dao layer. Extends {@link Dao} base interface
 * @author roma0
 * @version 1.0
 * @since 1.0
 * @see "Data access object pattern"
 */
public interface OrderDao extends Dao<Order> {

    /**
     * Finds and returns find result of find orders by specified {@link User}
     * @throws by.epam.jwd.web.exception.DAOException when exception in dao layer occurs
     * @param user {@link User} whose orders need to be found
     * @return User's orders collection
     */
    List<Order> findByUser(User user);

    /**
     * Finds and returns find result of find orders by specified book
     * @throws by.epam.jwd.web.exception.DAOException when exception in dao layer occurs
     * @param book {@link Book} whose order need to be found
     * @return orders by book collection
     */
    List<Order> findByBook(Book book);

    /**
     * Finds and returns find result of find orders by specified {@link LocalDate}
     * @throws by.epam.jwd.web.exception.DAOException when exception in dao layer occurs
     * @param orderDate {@link LocalDate} by what need to find orders
     * @return orders with have specified order date
     */
    List<Order> findByOrderDate(LocalDate orderDate);
}
