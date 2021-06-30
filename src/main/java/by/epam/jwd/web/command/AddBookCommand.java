package by.epam.jwd.web.command;

import by.epam.jwd.web.exception.RegisterException;
import by.epam.jwd.web.model.Author;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

public class AddBookCommand implements ActionCommand {

    private static final String REQUEST_NAME_PARAMETER_KEY = "name";
    private static final String REQUEST_AUTHOR_PARAMETER_KEY = "author";
    private static final String REQUEST_GENRE_PARAMETER_KEY = "genre";
    private static final String REQUEST_DATE_PARAMETER_KEY = "date";
    private static final String REQUEST_PAGES_PARAMETER_KEY = "pages";
    private static final String REQUEST_DESCRIPTION_PARAMETER_KEY = "description";

    private static final String SESSION_SUCCESS_ATTRIBUTE_KEY = "success";
    private static final String SESSION_FAIL_ATTRIBUTE_KEY = "fail";
    private static final String SUCCESS_MESSAGE = "book %s was added";

    private static final String RESULT_PATH = "index.jsp";

    private AddBookCommand() {
    }

    public static AddBookCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final String name = request.getParameter(REQUEST_NAME_PARAMETER_KEY);
        final String author = request.getParameter(REQUEST_AUTHOR_PARAMETER_KEY);
        final Genre genre = Genre.valueOf(request.getParameter(REQUEST_GENRE_PARAMETER_KEY));
        final LocalDate date = LocalDate.parse(request.getParameter(REQUEST_DATE_PARAMETER_KEY));
        final int pages = Integer.parseInt(request.getParameter(REQUEST_PAGES_PARAMETER_KEY));
        final String description = request.getParameter(REQUEST_DESCRIPTION_PARAMETER_KEY);
        final Book book = new Book(name, new Author(author), genre, date, pages, description);
        try {
            ServiceFactory.getInstance().getBookService().register(book);
            request.getSession().setAttribute(SESSION_SUCCESS_ATTRIBUTE_KEY, "bookRegistered");
        } catch (RegisterException e) {
            request.getSession().setAttribute(SESSION_FAIL_ATTRIBUTE_KEY, "bookExists");
        }
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
        private static final AddBookCommand INSTANCE = new AddBookCommand();
    }


}
