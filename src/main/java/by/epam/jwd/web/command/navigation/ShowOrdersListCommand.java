package by.epam.jwd.web.command.navigation;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.model.Order;
import by.epam.jwd.web.resource.PathManager;
import by.epam.jwd.web.service.OrderService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Executes command that is form orders page and make path for forward to orders page.
 *
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class ShowOrdersListCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(ShowOrdersListCommand.class);
    private final OrderService orderService = OrderService.getInstance();
    private static final String COMMAND_REQUESTED_MESSAGE = "Show orders list command was requested";
    private static final String COMMAND_EXECUTED_MESSAGE = "Show orders list command was executed";
    private static final String REQUEST_PAGE_PARAMETER_KEY = "page";
    private static final String REQUEST_ORDERS_ATTRIBUTE_KEY = "orders";
    private static final String REQUEST_CURRENT_PAGE_NUMBER_ATTRIBUTE_KEY = "currentPageNumber";
    private static final String REQUEST_PAGES_AMOUNT_ATTRIBUTE_KEY = "pagesAmount";

    private ShowOrdersListCommand() {
    }

    /**
     * Gets single class instance.
     *
     * @return class instance.
     */
    public static ShowOrdersListCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Forms orders page if page number is passed of first page
     * otherwise and forms orders page path to forward.
     *
     * @param request request that need to be execute
     * @return orders page path for forward.
     */
    @Override
    public String execute(HttpServletRequest request) {
        logger.info(COMMAND_REQUESTED_MESSAGE);
        final String pageParameter = request.getParameter(REQUEST_PAGE_PARAMETER_KEY);
        int currentPageNumber = pageParameter == null ? 1 : Integer.parseInt(pageParameter);
        final List<Order> currentPage = orderService.findPage(currentPageNumber);
        final int pagesAmount = orderService.getPagesAmount();
        request.setAttribute(REQUEST_ORDERS_ATTRIBUTE_KEY, currentPage);
        request.setAttribute(REQUEST_CURRENT_PAGE_NUMBER_ATTRIBUTE_KEY, currentPageNumber);
        request.setAttribute(REQUEST_PAGES_AMOUNT_ATTRIBUTE_KEY, pagesAmount);
        logger.info(COMMAND_EXECUTED_MESSAGE);
        return PathManager.getOrdersPagePath();
    }

    /**
     * Nested class that encapsulated single {@link ShowOrdersListCommand} instance.
     * Singleton pattern variation.
     *
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final ShowOrdersListCommand INSTANCE = new ShowOrdersListCommand();
    }
}
