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
    public String execute(HttpServletRequest request) {
        final Long orderId = Long.valueOf(request.getParameter(ID));
        final Order order = ServiceFactory.getInstance().getOrderService().findById(orderId);
        final Book book = order.getBook();
        ServiceFactory.getInstance().getBookService().addOneCopy(book.getId());
        ServiceFactory.getInstance().getOrderService().deleteOrder(order.getId());
        request.getSession().setAttribute(COMMAND_RESULT, String.format(RESULT_MESSAGE, book.getName()));
        return null;
    }

    private static class Singleton {
        private static final ReturnBookCommand INSTANCE = new ReturnBookCommand();
    }
}
