package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Order;
import by.epam.jwd.web.service.OrderService;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class ReturnBookCommand implements ActionCommand {
    private final OrderService orderService = ServiceFactory.getInstance().getOrderService();

    private static final String REQUEST_BOOK_ID_PARAMETER_KEY = "id";
    private static final String SESSION_SUCCESS_ATTRIBUTE_KEY = "success";
    private static final String BOOK_WAS_RETURNED_LOCALIZATION_MESSAGE_KEY = "bookReturned";

    private static final String RESULT_PATH = "index.jsp";

    private ReturnBookCommand() {
    }

    public static ReturnBookCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final Long orderId = Long.valueOf(request.getParameter(REQUEST_BOOK_ID_PARAMETER_KEY));
        final Order foundOrder = orderService.findById(orderId);
        orderService.returnOrder(foundOrder);
        request.getSession().setAttribute(SESSION_SUCCESS_ATTRIBUTE_KEY, BOOK_WAS_RETURNED_LOCALIZATION_MESSAGE_KEY);
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
        private static final ReturnBookCommand INSTANCE = new ReturnBookCommand();
    }
}
