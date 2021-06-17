package by.epam.jwd.web.command;

import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

public class FindBooksCommand implements ActionCommand {

    private static final String NAME = "name";
    private static final String FIND_CRITERIA = "criteria";
    private static final String BOOK_NAME_CRITERIA = "name";
    private static final String AUTHOR_NAME_CRITERIA = "author";
    private static final String GENRE_NAME_CRITERIA = "genre";
    private static final String BOOKS = "books";
    private static final String ERROR = "error";
    private static final String ERROR_MESSAGE = "Error in finding";
    private static final String MAIN_JSP_PATH = "WEB-INF/jsp/main.jsp";

    private FindBooksCommand() {
    }

    public static FindBooksCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        final String findName = request.getParameter(NAME);
        final String criteria = request.getParameter(FIND_CRITERIA);
        List<Book> books = ServiceFactory.getInstance().getBookService().findAllBooks();
        request.setAttribute(BOOKS, books);
        return MAIN_JSP_PATH;
    }

    private static class Singleton {
        private static final FindBooksCommand INSTANCE = new FindBooksCommand();
    }
}
