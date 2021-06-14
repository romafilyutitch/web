package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class OrderBookCommand implements ActionCommand {

    public static final String USER = "user";
    public static final String ID = "id";
    public static final String COMMAND_RESULT = "commandResult";
    public static final String RESULT_MESSAGE = "Book %s was ordered. See my orders page";

    private OrderBookCommand() {
    }

    public static OrderBookCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        final Long bookId = Long.valueOf(request.getParameter(ID));
        try {
            final User user = (User) request.getSession().getAttribute(USER);
            final Long userId = user.getId();
            ServiceFactory.getInstance().getOrderService().createOrder(userId, bookId);
            final Book orderedBook = ServiceFactory.getInstance().getBookService().removeOneCopy(bookId);
            request.getSession().setAttribute(COMMAND_RESULT, String.format(RESULT_MESSAGE, orderedBook.getName()));
        } catch (ServiceException e) {
            request.getSession().setAttribute(COMMAND_RESULT, e.getMessage());
        }
        return null;
    }

    private static class Singleton {
        private static final OrderBookCommand INSTANCE = new OrderBookCommand();
    }
}