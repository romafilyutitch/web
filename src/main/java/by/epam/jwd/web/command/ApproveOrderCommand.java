package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Order;
import by.epam.jwd.web.service.OrderService;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class ApproveOrderCommand implements ActionCommand {
    private final OrderService orderService = ServiceFactory.getInstance().getOrderService();

    private static final String REQUEST_ORDER_ID_PARAMETER_KEY = "id";
    private static final String SESSION_SUCCESS_ATTRIBUTE_KEY = "success";
    private static final String ORDER_APPROVED_LOCALIZATION_MESSAGE_KEY = "orderApproved";

    private static final String RESULT_PATH = "index.jsp";

    private ApproveOrderCommand() {
    }

    public static ApproveOrderCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final Long id = Long.valueOf(request.getParameter(REQUEST_ORDER_ID_PARAMETER_KEY));
        final Order foundOrder = orderService.findById(id);
        orderService.approveOrder(foundOrder);
        request.getSession().setAttribute(SESSION_SUCCESS_ATTRIBUTE_KEY, ORDER_APPROVED_LOCALIZATION_MESSAGE_KEY);
        return new CommandResult() {
            @Override
            public String getResultPath() {
                return RESULT_PATH;
            }

            @Override
            public boolean isRedirect() {
                return true;
            }
        };
    }

    private static class Singleton {
        private static final ApproveOrderCommand INSTANCE = new ApproveOrderCommand();
    }
}
