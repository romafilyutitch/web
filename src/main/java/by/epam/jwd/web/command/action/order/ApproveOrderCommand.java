package by.epam.jwd.web.command.action.order;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.model.Order;
import by.epam.jwd.web.resource.CommandManager;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.service.api.OrderService;
import by.epam.jwd.web.service.api.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * Executes command that is Approve book order that user makes.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class ApproveOrderCommand implements ActionCommand {
    private final OrderService orderService = ServiceFactory.getInstance().getOrderService();

    private static final String REQUEST_ORDER_ID_PARAMETER_KEY = "id";
    private static final String REQUEST_MESSAGE_ATTRIBUTE_KEY = "message";
    private static final String ORDER_APPROVED_MESSAGE_KEY = "order.approved";


    private ApproveOrderCommand() {
    }

    /**
     * Get single class instance from nested class.
     * @return class instance.
     */
    public static ApproveOrderCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Approves definite book order.
     * Request must contain order id that need to be approved.
     * @param request request that need to be execute.
     * @return show orders command for forward.
     */
    @Override
    public String execute(HttpServletRequest request) {
        final Long id = Long.valueOf(request.getParameter(REQUEST_ORDER_ID_PARAMETER_KEY));
        final Order foundOrder = orderService.findById(id);
        orderService.approveOrder(foundOrder);
        request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(ORDER_APPROVED_MESSAGE_KEY));
        return CommandManager.getShowOrdersCommand();
    }

    /**
     * Nested class that encapsulated single {@link ApproveOrderCommand} instance.
     * Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final ApproveOrderCommand INSTANCE = new ApproveOrderCommand();
    }
}
