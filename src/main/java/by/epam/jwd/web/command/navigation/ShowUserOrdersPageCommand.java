package by.epam.jwd.web.command.navigation;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.model.Order;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.resource.PathManager;
import by.epam.jwd.web.service.api.OrderService;
import by.epam.jwd.web.service.api.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Executes command that is form user's orders and form user orders page path to forward.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class ShowUserOrdersPageCommand implements ActionCommand {
    private final OrderService orderService = ServiceFactory.getInstance().getOrderService();

    private static final String SESSION_USER_ATTRIBUTE_KEY = "user";
    private static final String REQUEST_ORDERS_ATTRIBUTE_KEY = "orders";

    private ShowUserOrdersPageCommand() {
    }

    /**
     * Gets single class instance from nested class.
     * @return class instance.
     */
    public static ShowUserOrdersPageCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Finds user's orders and forms users orders page path to forward.
     * @param request request that need to be execute.
     * @return user orders page path for forward.
     */
    @Override
    public String execute(HttpServletRequest request) {
        final User user = (User) request.getSession().getAttribute(SESSION_USER_ATTRIBUTE_KEY);
        final List<Order> userOrders = orderService.findByUser(user);
        request.setAttribute(REQUEST_ORDERS_ATTRIBUTE_KEY, userOrders);
        return PathManager.getUserOrdersPagePath();
    }

    /**
     * Nested class that encapsulates single {@link ShowUserOrdersPageCommand} instance.
     * Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final ShowUserOrdersPageCommand INSTANCE = new ShowUserOrdersPageCommand();
    }
}
