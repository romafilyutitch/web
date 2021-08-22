package by.epam.jwd.web.command.navigation;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.model.Order;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.resource.PathManager;
import by.epam.jwd.web.service.OrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Executes command that is form user's orders and form user orders page path to forward.
 *
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class ShowUserOrdersPageCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(ShowUserOrdersPageCommand.class);
    private final OrderService orderService = OrderService.getInstance();
    private static final String COMMAND_REQUESTED_MESSAGE = "Show user orders page command was requested";
    private static final String COMMAND_EXECUTED_MESSAGE = "Show user orders page command was executed";
    private static final String SESSION_USER_ATTRIBUTE_KEY = "user";
    private static final String REQUEST_ORDERS_ATTRIBUTE_KEY = "orders";

    private ShowUserOrdersPageCommand() {
    }

    /**
     * Gets single class instance from nested class.
     *
     * @return class instance.
     */
    public static ShowUserOrdersPageCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Finds user's orders and forms users orders page path to forward.
     *
     * @param request request that need to be execute.
     * @return user orders page path for forward.
     */
    @Override
    public String execute(HttpServletRequest request) {
        logger.info(COMMAND_REQUESTED_MESSAGE);
        final User user = (User) request.getSession().getAttribute(SESSION_USER_ATTRIBUTE_KEY);
        final List<Order> userOrders = orderService.findByUser(user);
        request.setAttribute(REQUEST_ORDERS_ATTRIBUTE_KEY, userOrders);
        logger.info(COMMAND_EXECUTED_MESSAGE);
        return PathManager.getUserOrdersPagePath();
    }

    /**
     * Nested class that encapsulates single {@link ShowUserOrdersPageCommand} instance.
     * Singleton pattern variation.
     *
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final ShowUserOrdersPageCommand INSTANCE = new ShowUserOrdersPageCommand();
    }
}
