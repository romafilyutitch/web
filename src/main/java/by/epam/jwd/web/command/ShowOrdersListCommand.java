package by.epam.jwd.web.command;

import by.epam.jwd.web.model.BookOrder;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ShowOrdersListCommand implements ActionCommand {

    public static final String ORDERS = "orders";
    public static final String ORDERS_JSP_PATH = "WEB-INF/jsp/orders.jsp";

    private ShowOrdersListCommand() {
    }

    public static ShowOrdersListCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        final List<BookOrder> orders = ServiceFactory.getInstance().getOrderService().findAll();
        request.setAttribute(ORDERS, orders);
        return ORDERS_JSP_PATH;
    }

    private static class Singleton {
        private static final ShowOrdersListCommand INSTANCE = new ShowOrdersListCommand();
    }
}
