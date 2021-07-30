package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Order;
import by.epam.jwd.web.service.BookService;
import by.epam.jwd.web.service.OrderService;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class DeleteOrderCommand implements ActionCommand {
    private final BookService bookService = ServiceFactory.getInstance().getBookService();
    private final OrderService orderService = ServiceFactory.getInstance().getOrderService();

    private static final String REQUEST_ORDER_ID_PARAMETER_KEY = "id";

    private static final String SESSION_SUCCESS_ATTRIBUTE_KEY = "success";
    private static final String ORDER_DELETED_LOCALIZATION_MESSAGE_KEY = "orderDeleted";

    private static final String RESULT_PATH = "controller?command=show_orders";

    private DeleteOrderCommand() {
    }

    public static DeleteOrderCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final Long orderId = Long.valueOf(request.getParameter(REQUEST_ORDER_ID_PARAMETER_KEY));
        final Order order = orderService.findById(orderId);
        final Book book = order.getBook();
        bookService.addOneCopy(book);
        orderService.delete(orderId);
        request.getSession().setAttribute(SESSION_SUCCESS_ATTRIBUTE_KEY, ORDER_DELETED_LOCALIZATION_MESSAGE_KEY);
        return new CommandResult() {
            @Override
            public String getResultPath() {
                return RESULT_PATH;
            }

            @Override
            public boolean isRedirect() {
                return false;
            }
        };
    }

    private static class Singleton {
        private static final DeleteOrderCommand INSTANCE = new DeleteOrderCommand();
    }
}
