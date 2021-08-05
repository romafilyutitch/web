package by.epam.jwd.web.service;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Order;
import by.epam.jwd.web.model.User;

import java.util.List;

/**
 * Service interface for service layer that defines {@link Order} service behavior.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public interface OrderService extends Service<Order> {

    /**
     * Makes approve to passed order.
     * @param order order that need to approve.
     */
    void approveOrder(Order order);

    /**
     * Makes order return of passed order.
     * @param order order that need to return.
     */
    void returnOrder(Order order);

    /**
     * Finds orders that have passed book.
     * @param book which orders need to be found.
     * @return collection of orders that have passed book.
     */
    List<Order> findByBook(Book book);

    /**
     * Finds orders that passed user made.
     * @param user whose orders need to be found.
     * @return collection of orders that passed user made
     */
    List<Order> findByUser(User user);
}
