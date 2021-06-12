package by.epam.jwd.web.dao;



import by.epam.jwd.web.model.BookOrder;

import java.time.LocalDate;
import java.util.List;

public interface OrderDao extends Dao<BookOrder> {

    List<BookOrder> findOrdersByUserId(Long userId);

    List<BookOrder> findOrdersByUserLogin(String userLogin);

    List<BookOrder> findOrdersByBookName(String bookName);

    List<BookOrder> findOrdersByOrderDate(LocalDate orderDate);
}
