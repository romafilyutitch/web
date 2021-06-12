package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.BookOrder;
import by.epam.jwd.web.service.ServiceException;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class ReturnBookCommand implements ActionCommand {

    public static final String ID = "id";
    public static final String COMMAND_RESULT = "commandResult";
    public static final String RESULT_MESSAGE = "book was returned";

    private ReturnBookCommand() {
    }

    public static ReturnBookCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        try {
            final Long orderId = Long.valueOf(request.getParameter(ID));
            final BookOrder order = ServiceFactory.getInstance().getOrderService().findById(orderId);
            final Book book = order.getBook();
            ServiceFactory.getInstance().getBookService().addOneCopy(book.getId());
            ServiceFactory.getInstance().getOrderService().deleteOrder(order.getId());
            request.getSession().setAttribute(COMMAND_RESULT, RESULT_MESSAGE);
            return null;
        } catch (ServiceException e) {
            request.getSession().setAttribute(COMMAND_RESULT, e.getMessage());
            return null;
        }
    }

    private static class Singleton {
        private static final ReturnBookCommand INSTANCE = new ReturnBookCommand();
    }
}
