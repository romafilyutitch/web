package by.epam.jwd.web.command;

import by.epam.jwd.web.model.User;
import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class OrderBookCommand implements ActionCommand {

    public static final String USER = "user";
    public static final String ID = "id";
    public static final String COMMAND_RESULT = "commandResult";
    public static final String RESULT_MESSAGE = "Book was ordered. See my orders page";
    public static final String DID_NOT_CHOOSE_BOOK_MESSAGE = "You didn't chose book to order. Chose book";

    private OrderBookCommand() {
    }

    public static OrderBookCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        try {
            final User user = (User) request.getSession().getAttribute(USER);
            final Long userId = user.getId();
            final Long bookId = Long.valueOf(request.getParameter(ID));
            ServiceFactory.getInstance().getOrderService().createOrder(userId, bookId);
            ServiceFactory.getInstance().getBookService().removeOneCopy(bookId);
            request.getSession().setAttribute(COMMAND_RESULT, RESULT_MESSAGE);
            return null;
        } catch (ServiceException e) {
            request.getSession().setAttribute(COMMAND_RESULT, e.getMessage());
            return null;
        } catch (NumberFormatException e) {
            request.getSession().setAttribute(COMMAND_RESULT, DID_NOT_CHOOSE_BOOK_MESSAGE);
            return null;
        }
    }

    private static class Singleton {
        private static final OrderBookCommand INSTANCE = new OrderBookCommand();
    }
}