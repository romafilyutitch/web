package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FindBookByNameCommand implements ActionCommand {

    private static final String REQUEST_BOOK_NAME_PARAMETER_KEY = "name";
    private static final String REQUEST_BOOKS_ATTRIBUTE_KEY = "books";
    private static final String REQUEST_FIND_RESULT_ATTRIBUTE_KEY = "findResult";
    private static final String BOOK_WAS_FOUND_MESSAGE = "Book was found";
    private static final String BOOK_WAS_NOT_FOUND_MESSAGE = "Book was not found";

    private static final String RESULT_PATH = "WEB-INF/jsp/main.jsp";


    private FindBookByNameCommand() {
    }

    public static FindBookByNameCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final String bookName = request.getParameter(REQUEST_BOOK_NAME_PARAMETER_KEY);
        final Optional<Book> optionalBook = ServiceFactory.getInstance().getBookService().findByName(bookName);
        if (optionalBook.isPresent()) {
            final List<Book> foundBook = Collections.singletonList(optionalBook.get());
            request.setAttribute(REQUEST_BOOKS_ATTRIBUTE_KEY, foundBook);
            request.setAttribute(REQUEST_FIND_RESULT_ATTRIBUTE_KEY, BOOK_WAS_FOUND_MESSAGE);
        } else {
            request.setAttribute(REQUEST_BOOKS_ATTRIBUTE_KEY, Collections.emptyList());
            request.setAttribute(REQUEST_FIND_RESULT_ATTRIBUTE_KEY, BOOK_WAS_NOT_FOUND_MESSAGE);
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
        private static final FindBookByNameCommand INSTANCE = new FindBookByNameCommand();
    }
}
