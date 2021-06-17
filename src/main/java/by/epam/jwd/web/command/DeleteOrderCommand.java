package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Order;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class DeleteOrderCommand implements ActionCommand {

    private DeleteOrderCommand() {}

    public static DeleteOrderCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        final Long orderId = Long.valueOf(request.getParameter("id"));
        final Order order = ServiceFactory.getInstance().getOrderService().findById(orderId);
        ServiceFactory.getInstance().getBookService().addOneCopy(order.getBook().getId());
        ServiceFactory.getInstance().getOrderService().deleteOrder(orderId);
        request.getSession().setAttribute("commandResult", String.format("Order %d was deleted", orderId));
        return null;
    }

    private static class Singleton {
        private static final DeleteOrderCommand INSTANCE = new DeleteOrderCommand();
    }
}
