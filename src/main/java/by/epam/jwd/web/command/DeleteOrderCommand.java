package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Order;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class DeleteOrderCommand implements ActionCommand {

    private static final String REQUEST_ORDER_ID_PARAMETER_KEY = "id";

    private static final String SESSION_SUCCESS_ATTRIBUTE_KEY = "success";
    private static final String SUCCESS_MESSAGE = "Order %d was deleted";

    private static final String RESULT_PATH = "index.jsp";

    private DeleteOrderCommand() {
    }

    public static DeleteOrderCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final Long orderId = Long.valueOf(request.getParameter(REQUEST_ORDER_ID_PARAMETER_KEY));
        final Order order = ServiceFactory.getInstance().getOrderService().findById(orderId);
        ServiceFactory.getInstance().getBookService().addOneCopy(order.getBook().getId());
        ServiceFactory.getInstance().getOrderService().delete(orderId);
        request.getSession().setAttribute(SESSION_SUCCESS_ATTRIBUTE_KEY, "orderDeleted");
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
        private static final DeleteOrderCommand INSTANCE = new DeleteOrderCommand();
    }
}
