package by.epam.jwd.web.command;

import by.epam.jwd.web.exception.RegisterException;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Order;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class OrderBookCommand implements ActionCommand {

    public static final String USER = "user";
    public static final String ID = "id";
    public static final String COMMAND_RESULT = "commandResult";
    public static final String RESULT_MESSAGE = "Book %s was ordered. See my orders page";

    private OrderBookCommand() {
    }

    public static OrderBookCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final Long bookId = Long.valueOf(request.getParameter("id"));
        final Book book = ServiceFactory.getInstance().getBookService().findById(bookId);
        final User user = (User) request.getSession().getAttribute("user");
        final Order order = new Order(user, book);
        try {
            ServiceFactory.getInstance().getOrderService().register(order);
            final Book orderedBook = ServiceFactory.getInstance().getBookService().removeOneCopy(bookId);
            request.getSession().setAttribute("success", String.format("Book %s was ordered. See my orders page", orderedBook.getName()));
        } catch (RegisterException e) {
            request.getSession().setAttribute("fail", e.getMessage());
        }
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
        private static final OrderBookCommand INSTANCE = new OrderBookCommand();
    }
}