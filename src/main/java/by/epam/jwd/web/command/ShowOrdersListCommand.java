package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Order;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ShowOrdersListCommand implements ActionCommand {

    private static final String REQUEST_PAGE_PARAMETER_KEY = "page";
    private static final String REQUEST_ORDERS_ATTRIBUTE_KEY = "orders";
    private static final String REQUEST_CURRENT_PAGE_NUMBER_ATTRIBUTE_KEY = "currentPageNumber";
    private static final String REQUEST_PAGES_AMOUNT_ATTRIBUTE_KEY = "pagesAmount";

    private static final String RESULT_PATH = "WEB-INF/jsp/orders.jsp";

    private ShowOrdersListCommand() {
    }

    public static ShowOrdersListCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        int currentPageNumber = 1;
        final String pageParameter = request.getParameter(REQUEST_PAGE_PARAMETER_KEY);
        if (pageParameter != null) {
            currentPageNumber = Integer.parseInt(pageParameter);
        }
        final List<Order> currentPage = ServiceFactory.getInstance().getOrderService().findPage(currentPageNumber);
        final int pagesAmount = ServiceFactory.getInstance().getOrderService().getPagesAmount();
        request.setAttribute(REQUEST_ORDERS_ATTRIBUTE_KEY, currentPage);
        request.setAttribute(REQUEST_CURRENT_PAGE_NUMBER_ATTRIBUTE_KEY, currentPageNumber);
        request.setAttribute(REQUEST_PAGES_AMOUNT_ATTRIBUTE_KEY, pagesAmount);
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
        private static final ShowOrdersListCommand INSTANCE = new ShowOrdersListCommand();
    }
}
