package by.epam.jwd.web.command.action.order;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Order;
import by.epam.jwd.web.model.Status;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.resource.CommandManager;
import by.epam.jwd.web.resource.PathManager;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.service.BookService;
import by.epam.jwd.web.service.OrderService;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

/**
 * Executes command that is make book order by user and save it in database table.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class OrderBookCommand implements ActionCommand {
    private final BookService bookService = ServiceFactory.getInstance().getBookService();
    private final OrderService orderService = ServiceFactory.getInstance().getOrderService();

    private static final String REQUEST_ORDER_ID_PARAMETER_KEY = "id";
    private static final String REQUEST_MESSAGE_ATTRIBUTE_KEY = "message";
    private static final String SESSION_USER_ATTRIBUTE_KEY = "user";
    private static final String NO_COPIES_MESSAGE_KEY = "order.noCopies";
    private static final String ORDER_REGISTERED_MESSAGE_KEY = "order.registered";

    private OrderBookCommand() {
    }

    /**
     * Gets single class instance from nested class.
     * @return class instance.
     */
    public static OrderBookCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Makes book order and save it in database table.
     * Request must contain saved book id that need to order.
     * @param request request that need to be execute.
     * @return main command for forward.
     */
    @Override
    public String execute(HttpServletRequest request) {
        final Long bookId = Long.valueOf(request.getParameter(REQUEST_ORDER_ID_PARAMETER_KEY));
        final Book book = bookService.findById(bookId);
        final User user = (User) request.getSession().getAttribute(SESSION_USER_ATTRIBUTE_KEY);
        final Order order = new Order(user, book, LocalDate.now(), Status.ORDERED);
        if (book.getCopiesAmount() == 0) {
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(NO_COPIES_MESSAGE_KEY));
        } else {
            orderService.save(order);
            bookService.removeOneCopy(book);
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(ORDER_REGISTERED_MESSAGE_KEY));
        }
        return CommandManager.getMainCommand();
    }

    /**
     * Nested class that encapsulates single {@link OrderBookCommand} instance.
     */
    private static class Singleton {
        private static final OrderBookCommand INSTANCE = new OrderBookCommand();
    }
}