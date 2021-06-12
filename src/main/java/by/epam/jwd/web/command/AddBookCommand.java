package by.epam.jwd.web.command;

import by.epam.jwd.web.service.ServiceException;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class AddBookCommand implements ActionCommand {

    public static final String BOOK_NAME = "bookName";
    public static final String AUTHOR_NAME = "authorName";
    public static final String GENRE_NAME = "genreName";
    public static final String DATE = "date";
    public static final String PAGES = "pages";
    public static final String DESCRIPTION = "description";
    public static final String TEXT = "text";
    public static final String COMMAND_RESULT = "commandResult";
    public static final String RESULT_MESSAGE = "book was added";

    private AddBookCommand() {
    }

    public static AddBookCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        final String name = request.getParameter(BOOK_NAME);
        final String author = request.getParameter(AUTHOR_NAME);
        final String genre = request.getParameter(GENRE_NAME);
        final String date = request.getParameter(DATE);
        final String pages = request.getParameter(PAGES);
        final String description = request.getParameter(DESCRIPTION);
        final String text = request.getParameter(TEXT);
        try {
            ServiceFactory.getInstance().getBookService().createBook(name, author, genre, date, pages, description, text);
            request.getSession().setAttribute(COMMAND_RESULT, RESULT_MESSAGE);
            return null;
        } catch (ServiceException e) {
            request.getSession().setAttribute(COMMAND_RESULT, e.getMessage());
            return null;
        }
    }

    private static class Singleton {
        private static final AddBookCommand INSTANCE = new AddBookCommand();
    }


}
