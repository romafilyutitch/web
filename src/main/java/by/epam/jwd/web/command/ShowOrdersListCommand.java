package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Order;
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
    public CommandResult execute(HttpServletRequest request) {
        int currentPageNumber = 1;
        final String pageParameter = request.getParameter("page");
        if (pageParameter != null) {
            currentPageNumber = Integer.parseInt(pageParameter);
        }
        final List<Order> currentPage = ServiceFactory.getInstance().getOrderService().findPage(currentPageNumber);
        final int pagesAmount = ServiceFactory.getInstance().getOrderService().getPagesAmount();
        request.setAttribute("orders", currentPage);
        request.setAttribute("currentPageNumber", currentPageNumber);
        request.setAttribute("pagesAmount", pagesAmount);
        return new CommandResult() {
            @Override
            public String getResultPath() {
                return "WEB-INF/jsp/orders.jsp";
            }

            @Override
            public boolean isRedirect() {
                return false;
            }
        };
    }

    private static class Singleton {
        private static final ShowOrdersListCommand INSTANCE = new ShowOrdersListCommand();
    }
}
