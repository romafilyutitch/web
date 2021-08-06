package by.epam.jwd.web.command.action.order;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Order;
import by.epam.jwd.web.resource.CommandManager;
import by.epam.jwd.web.resource.PathManager;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.service.BookService;
import by.epam.jwd.web.service.OrderService;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * Executes command that is delete {@link Order} from database table.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class DeleteOrderCommand implements ActionCommand {
    private final BookService bookService = ServiceFactory.getInstance().getBookService();
    private final OrderService orderService = ServiceFactory.getInstance().getOrderService();

    private static final String REQUEST_ORDER_ID_PARAMETER_KEY = "id";
    private static final String REQUEST_MESSAGE_ATTRIBUTE_KEY = "message";
    private static final String ORDER_WAS_DELETED_MESSAGE_KEY = "order.deleted";

    private DeleteOrderCommand() {
    }

    /**
     * Gets single class instance from nested class.
     * @return class instance.
     */
    public static DeleteOrderCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Deletes saved {@link Order} from database table.
     * Request must contain order id that need to delete.
     * @param request request that need to be execute.
     * @return show orders command for forward.
     */
    @Override
    public String execute(HttpServletRequest request) {
        final Long orderId = Long.valueOf(request.getParameter(REQUEST_ORDER_ID_PARAMETER_KEY));
        final Order order = orderService.findById(orderId);
        final Book book = order.getBook();
        bookService.addOneCopy(book);
        orderService.delete(orderId);
        request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(ORDER_WAS_DELETED_MESSAGE_KEY));
        return CommandManager.getShowOrdersCommand();
    }

    /**
     * Nested class that encapsulated single {@link DeleteOrderCommand} instance.
     * Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final DeleteOrderCommand INSTANCE = new DeleteOrderCommand();
    }
}
