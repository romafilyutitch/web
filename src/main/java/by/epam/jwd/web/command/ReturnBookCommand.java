package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.BookOrder;
import by.epam.jwd.web.service.ServiceException;
import by.epam.jwd.web.service.SimpleBookService;
import by.epam.jwd.web.service.SimpleOrderService;

import javax.servlet.http.HttpServletRequest;

public class ReturnBookCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request) {
        try {
            final Long orderId = Long.valueOf(request.getParameter("id"));
            final BookOrder order = SimpleOrderService.getInstance().findById(orderId);
            final Book book = order.getBook();
            SimpleBookService.getInstance().addOneCopy(book.getId());
            SimpleOrderService.getInstance().deleteOrder(order.getId());
            request.getSession().setAttribute("commandResult", "book was returned");
            return null;
        } catch (ServiceException e) {
            request.getSession().setAttribute("commandResult", e.getMessage());
            return null;
        }
    }
}
