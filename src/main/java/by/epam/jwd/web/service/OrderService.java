package by.epam.jwd.web.service;

import by.epam.jwd.web.exception.RegisterException;
import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.model.BookOrder;

import java.util.List;

public interface OrderService {
    List<BookOrder> findAllOrders();

    BookOrder registerBookOrder(BookOrder order);

    BookOrder approveOrder(Long orderId);

    BookOrder findById(Long orderId);

    void deleteOrder(Long orderId);

    List<BookOrder> findByUserId(Long userId);
}
