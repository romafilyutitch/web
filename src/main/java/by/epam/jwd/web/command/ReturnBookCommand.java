package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Order;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class ReturnBookCommand implements ActionCommand {

    public static final String ID = "id";
    public static final String COMMAND_RESULT = "commandResult";
    public static final String RESULT_MESSAGE = "book %s was returned";

    private ReturnBookCommand() {
    }

    public static ReturnBookCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final Long orderId = Long.valueOf(request.getParameter("id"));
        final Order order = ServiceFactory.getInstance().getOrderService().findById(orderId);
        final Book book = order.getBook();
        final Order returnedOrder = ServiceFactory.getInstance().getOrderService().returnOrder(order.getId());
        request.getSession().setAttribute("success", String.format("Book %s was returned", returnedOrder.getBook().getName()));
        return new CommandResult() {
            @Override
            public String getResultPath() {
                return "index.jsp";
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
