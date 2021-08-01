package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Order;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.OrderService;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ShowUserOrdersPageCommand implements ActionCommand {
    private final OrderService orderService = ServiceFactory.getInstance().getOrderService();

    private static final String SESSION_USER_ATTRIBUTE_KEY = "user";
    private static final String REQUEST_ORDERS_ATTRIBUTE_KEY = "orders";

    private static final String RESULT_PATH = "WEB-INF/jsp/user_orders.jsp";

    private ShowUserOrdersPageCommand() {
    }

    public static ShowUserOrdersPageCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        final User user = (User) request.getSession().getAttribute(SESSION_USER_ATTRIBUTE_KEY);
        final List<Order> userOrders = orderService.findByUser(user);
        request.setAttribute(REQUEST_ORDERS_ATTRIBUTE_KEY, userOrders);
        return RESULT_PATH;
    }

    private static class Singleton {
        private static final ShowUserOrdersPageCommand INSTANCE = new ShowUserOrdersPageCommand();
    }
}
