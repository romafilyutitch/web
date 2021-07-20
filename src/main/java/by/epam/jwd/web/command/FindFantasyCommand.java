package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;
import by.epam.jwd.web.service.BookService;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class FindFantasyCommand implements ActionCommand {
    private final BookService bookService = ServiceFactory.getInstance().getBookService();

    private static final String REQUEST_BOOKS_ATTRIBUTE_KEY = "books";
    private static final String REQUEST_FIND_RESULT_ATTRIBUTE_KEY = "findResult";
    private static final String BOOKS_WAS_NOT_FOUND_LOCALIZATION_MESSAGE_KEY = "booksNotFound";
    private static final String BOOKS_FOUND_LOCALIZATION_MESSAGE_KEY = "booksFound";

    private static final String RESULT_PATH = "WEB-INF/jsp/main.jsp";

    private FindFantasyCommand() {
    }

    public static FindFantasyCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final List<Book> fantasyBooks = bookService.findByGenre(Genre.FANTASY);
        if (fantasyBooks.isEmpty()) {
            request.setAttribute(REQUEST_FIND_RESULT_ATTRIBUTE_KEY, BOOKS_WAS_NOT_FOUND_LOCALIZATION_MESSAGE_KEY);
        } else {
            request.setAttribute(REQUEST_BOOKS_ATTRIBUTE_KEY, fantasyBooks);
            request.setAttribute(REQUEST_FIND_RESULT_ATTRIBUTE_KEY, BOOKS_FOUND_LOCALIZATION_MESSAGE_KEY);
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
        private static final FindFantasyCommand INSTANCE = new FindFantasyCommand();
    }
}
