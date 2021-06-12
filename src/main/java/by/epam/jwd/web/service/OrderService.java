package by.epam.jwd.web.service;

import by.epam.jwd.web.model.BookOrder;

import java.util.List;

public interface OrderService {
    List<BookOrder> findAll();

    BookOrder createOrder(Long readerId, Long bookId) throws ServiceException;

    BookOrder approveOrder(Long orderId) throws ServiceException;

    BookOrder findById(Long orderId) throws ServiceException;

    void deleteOrder(Long orderId) throws ServiceException;

    List<BookOrder> findByUserId(Long userId);
}
