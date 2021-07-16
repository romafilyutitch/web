package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.service.BookService;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class AddCopyCommand implements ActionCommand {
    private final BookService bookService = ServiceFactory.getInstance().getBookService();

    private static final String REQUEST_BOOK_ID_PARAMETER_KEY = "id";

    private static final String SESSION_SUCCESS_ATTRIBUTE_KEY = "success";
    private static final String SUCCESS_MESSAGE = "copy of book %s was added";

    private static final String RESULT_PATH = "index.jsp";

    private AddCopyCommand() {
    }

    public static AddCopyCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final Long id = Long.valueOf(request.getParameter(REQUEST_BOOK_ID_PARAMETER_KEY));
        final Book book = bookService.findById(id);
        bookService.addOneCopy(book);
        request.getSession().setAttribute(SESSION_SUCCESS_ATTRIBUTE_KEY, "copyAdded");
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
        private static final AddCopyCommand INSTANCE = new AddCopyCommand();
    }
}
