package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ShowBooksListCommand implements ActionCommand {

    public static final String BOOKS = "books";
    public static final String BOOKS_JSP_PATH = "WEB-INF/jsp/books.jsp";

    private ShowBooksListCommand() {
    }

    public static ShowBooksListCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final List<Book> allBooks = ServiceFactory.getInstance().getBookService().findAllBooks();
        final Genre[] genres = Genre.values();
        request.setAttribute("genres", genres);
        request.setAttribute("books", allBooks);
        return new CommandResult() {
            @Override
            public String getResultPath() {
                return "WEB-INF/jsp/books.jsp";
            }

            @Override
            public boolean isRedirect() {
                return false;
            }
        };
    }

    private static class Singleton {
        private static final ShowBooksListCommand INSTANCE = new ShowBooksListCommand();
    }
}
