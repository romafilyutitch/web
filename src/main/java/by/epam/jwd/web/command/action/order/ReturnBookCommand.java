package by.epam.jwd.web.command.action.order;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.model.Order;
import by.epam.jwd.web.resource.CommandManager;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.service.OrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * Executes command that is returned ordered by user book.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class ReturnBookCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(ReturnBookCommand.class);
    private final OrderService orderService = OrderService.getInstance();
    private static final String COMMAND_REQUESTED_MESSAGE = "Return book command was requested";
    private static final String COMMAND_EXECUTED_MESSAGE = "Return book command was executed";
    private static final String REQUEST_BOOK_ID_PARAMETER_KEY = "id";
    private static final String REQUEST_MESSAGE_ATTRIBUTE_KEY = "message";
    private static final String BOOK_WAS_RETURNED_MESSAGE_KEY = "book.returned";

    private ReturnBookCommand() {
    }

    /**
     * Gets single class instance from nested class.
     * @return class instance.
     */
    public static ReturnBookCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Returns ordered by user book.
     * Request must contain ordered book id to returned order.
     * @param request request that need to be execute.
     * @return show user orders command for forward.
     */
    @Override
    public String execute(HttpServletRequest request) {
        logger.info(COMMAND_REQUESTED_MESSAGE);
        final Long orderId = Long.valueOf(request.getParameter(REQUEST_BOOK_ID_PARAMETER_KEY));
        final Order foundOrder = orderService.findById(orderId);
        orderService.returnOrder(foundOrder);
        request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(BOOK_WAS_RETURNED_MESSAGE_KEY));
        logger.info(COMMAND_EXECUTED_MESSAGE);
        return CommandManager.getShowUsersOrdersCommand();
    }

    /**
     * Nested class that encapsulated single {@link ReturnBookCommand} instance.
     * Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final ReturnBookCommand INSTANCE = new ReturnBookCommand();
    }
}
