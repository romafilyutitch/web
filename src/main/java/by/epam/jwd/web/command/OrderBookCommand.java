package by.epam.jwd.web.command;

import by.epam.jwd.web.exception.RegisterException;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Order;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class OrderBookCommand implements ActionCommand {

    private static final String REQUEST_ORDER_ID_PARAMETER_KEY = "id";
    private static final String SESSION_USER_ATTRIBUTE_KEY = "user";
    private static final String SESSION_SUCCESS_ATTRIBUTE_KEY = "success";
    private static final String SUCCESS_MESSAGE = "Book %s was ordered. See my orders page";
    private static final String SESSION_FAIL_ATTRIBUTE_KEY = "fail";

    public static final String RESULT_PATH = "index.jsp";

    private OrderBookCommand() {
    }

    public static OrderBookCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final Long bookId = Long.valueOf(request.getParameter(REQUEST_ORDER_ID_PARAMETER_KEY));
        final Book book = ServiceFactory.getInstance().getBookService().findById(bookId);
        final User user = (User) request.getSession().getAttribute(SESSION_USER_ATTRIBUTE_KEY);
        final Order order = new Order(user, book);
        try {
            ServiceFactory.getInstance().getOrderService().register(order);
            final Book orderedBook = ServiceFactory.getInstance().getBookService().removeOneCopy(bookId);
            request.getSession().setAttribute(SESSION_SUCCESS_ATTRIBUTE_KEY, String.format(SUCCESS_MESSAGE, orderedBook.getName()));
        } catch (RegisterException e) {
            request.getSession().setAttribute(SESSION_FAIL_ATTRIBUTE_KEY, e.getMessage());
        }
        return new CommandResult() {
            @Override
            public String getResultPath() {
                return RESULT_PATH;
            }

            @Override
            public boolean isRedirect() {
                return true;
            }
        };
    }

    private static class Singleton {
        private static final OrderBookCommand INSTANCE = new OrderBookCommand();
    }
}