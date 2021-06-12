package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.BookOrder;
import by.epam.jwd.web.service.ServiceException;
import by.epam.jwd.web.service.SimpleBookService;
import by.epam.jwd.web.service.SimpleOrderService;

import javax.servlet.http.HttpServletRequest;

public class DeleteOrderCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request) {
        final Long orderId = Long.valueOf(request.getParameter("id"));
        try {
            final BookOrder order = SimpleOrderService.getInstance().findById(orderId);
            final Book book = order.getBook();
            SimpleBookService.getInstance().addOneCopy(book.getId());
            SimpleOrderService.getInstance().deleteOrder(order.getId());
            return "controller?command=show_user_orders";
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            return "controller?command=show_user_orders";
        }
    }
}
