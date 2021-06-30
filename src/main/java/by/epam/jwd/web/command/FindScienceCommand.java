package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class FindScienceCommand implements ActionCommand {

    private static final String REQUEST_BOOKS_ATTRIBUTE_KEY = "books";
    private static final String REQUEST_FIND_RESULT_ATTRIBUTE_KEY = "findResult";
    private static final String FIND_RESULT_MESSAGE = "%d books was found";

    public static final String RESULT_PATH = "WEB-INF/jsp/main.jsp";

    private FindScienceCommand() {
    }

    public static FindScienceCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final List<Book> scienceBooks = ServiceFactory.getInstance().getBookService().findByGenre(Genre.SCIENCE);
        if (scienceBooks.isEmpty()) {
            request.setAttribute(REQUEST_FIND_RESULT_ATTRIBUTE_KEY, "booksNotFound");
        } else {
            request.setAttribute(REQUEST_BOOKS_ATTRIBUTE_KEY, scienceBooks);
            request.setAttribute(REQUEST_FIND_RESULT_ATTRIBUTE_KEY, "booksFound");
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
        private static final FindScienceCommand INSTANCE = new FindScienceCommand();
    }
}
