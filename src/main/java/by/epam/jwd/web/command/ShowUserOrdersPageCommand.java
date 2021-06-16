package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Order;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ShowUserOrdersPageCommand implements ActionCommand {

    public static final String USER = "user";
    public static final String ORDERS = "orders";
    public static final String USER_ORDER_JSP_PATH = "WEB-INF/jsp/user_orders.jsp";

    private ShowUserOrdersPageCommand() {
    }

    public static ShowUserOrdersPageCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        final User user = (User) request.getSession().getAttribute(USER);
        final List<Order> userOrders = ServiceFactory.getInstance().getOrderService().findByUserId(user.getId());
        request.setAttribute(ORDERS, userOrders);
        return USER_ORDER_JSP_PATH;
    }

    private static class Singleton {
        private static final ShowUserOrdersPageCommand INSTANCE = new ShowUserOrdersPageCommand();
    }
}
