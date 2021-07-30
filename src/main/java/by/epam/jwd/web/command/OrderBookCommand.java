package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Order;
import by.epam.jwd.web.model.Status;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.BookService;
import by.epam.jwd.web.service.OrderService;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

public class OrderBookCommand implements ActionCommand {
    private final BookService bookService = ServiceFactory.getInstance().getBookService();
    private final OrderService orderService = ServiceFactory.getInstance().getOrderService();

    private static final String REQUEST_ORDER_ID_PARAMETER_KEY = "id";
    private static final String SESSION_USER_ATTRIBUTE_KEY = "user";
    private static final String SESSION_SUCCESS_ATTRIBUTE_KEY = "success";
    private static final String SESSION_FAIL_ATTRIBUTE_KEY = "fail";
    private static final String NO_COPY_LOCALIZATION_MESSAGE_KEY = "noCopy";
    private static final String BOOK_WAS_ORDER_LOCALIZATION_MESSAGE_KEY = "bookOrdered";

    private static final String RESULT_PATH = "controller?command=main";

    private OrderBookCommand() {
    }

    public static OrderBookCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final Long bookId = Long.valueOf(request.getParameter(REQUEST_ORDER_ID_PARAMETER_KEY));
        final Book book = bookService.findById(bookId);
        final User user = (User) request.getSession().getAttribute(SESSION_USER_ATTRIBUTE_KEY);
        final Order order = new Order(user, book, LocalDate.now(), Status.ORDERED);
        if (book.getCopiesAmount() == 0) {
            request.getSession().setAttribute(SESSION_FAIL_ATTRIBUTE_KEY, NO_COPY_LOCALIZATION_MESSAGE_KEY);
        } else {
            orderService.save(order);
            bookService.removeOneCopy(book);
            request.getSession().setAttribute(SESSION_SUCCESS_ATTRIBUTE_KEY, BOOK_WAS_ORDER_LOCALIZATION_MESSAGE_KEY);
        }
        return new CommandResult() {
            @Override
            public String getResultPath() {
                return RESULT_PATH;
            }

            @Override
            public boolean isRedirect() {
                return false;
            }
        };
    }

    private static class Singleton {
        private static final OrderBookCommand INSTANCE = new OrderBookCommand();
    }
}