package by.epam.jwd.web.command;

import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.ServiceException;
import by.epam.jwd.web.service.SimpleBookService;
import by.epam.jwd.web.service.SimpleOrderService;

import javax.servlet.http.HttpServletRequest;

public class OrderBookCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request) {
        try {
            final User user = (User) request.getSession().getAttribute("user");
            final Long userId = user.getId();
            final Long bookId = Long.valueOf(request.getParameter("id"));
            SimpleOrderService.getInstance().createOrder(userId, bookId);
            SimpleBookService.getInstance().removeOneCopy(bookId);
            request.getSession().setAttribute("commandResult", "Book was ordered. See my orders page");
            return null;
        } catch (ServiceException e) {
            request.getSession().setAttribute("commandResult", e.getMessage());
            return null;
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("commandResult", "You didn't chose book to order. Chose book");
            return null;
        }
    }
}