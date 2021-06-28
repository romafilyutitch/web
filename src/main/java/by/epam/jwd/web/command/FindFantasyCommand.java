package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class FindFantasyCommand implements ActionCommand {

    private static final String REQUEST_BOOKS_ATTRIBUTE_KEY = "books";
    private static final String REQUEST_FIND_RESULT_ATTRIBUTE_KEY = "findResult";
    private static final String FIND_RESULT_MESSAGE = "%d books was found";

    private static final String RESULT_PATH = "WEB-INF/jsp/main.jsp";

    private FindFantasyCommand() {
    }

    public static FindFantasyCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final List<Book> fantasyBooks = ServiceFactory.getInstance().getBookService().findByGenre(Genre.FANTASY);
        request.setAttribute(REQUEST_BOOKS_ATTRIBUTE_KEY, fantasyBooks);
        request.setAttribute(REQUEST_FIND_RESULT_ATTRIBUTE_KEY, String.format(FIND_RESULT_MESSAGE, fantasyBooks.size()));
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
