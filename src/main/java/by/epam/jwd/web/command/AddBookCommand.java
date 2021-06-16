package by.epam.jwd.web.command;

import by.epam.jwd.web.exception.RegisterException;
import by.epam.jwd.web.exception.ValidationException;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Author;
import by.epam.jwd.web.model.Genre;
import by.epam.jwd.web.service.ServiceFactory;
import by.epam.jwd.web.validator.BookValidator;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

public class AddBookCommand implements ActionCommand {

    public static final String BOOK_NAME = "bookName";
    public static final String AUTHOR_NAME = "authorName";
    public static final String GENRE_NAME = "genreName";
    public static final String DATE = "date";
    public static final String PAGES = "pages";
    public static final String DESCRIPTION = "description";
    public static final String TEXT = "text";
    public static final String COMMAND_RESULT = "commandResult";
    public static final String RESULT_MESSAGE = "book %s was added";

    private AddBookCommand() {
    }

    public static AddBookCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
//        final String name = request.getParameter(BOOK_NAME);
//        final String author = request.getParameter(AUTHOR_NAME);
//        final String genre = request.getParameter(GENRE_NAME);
//        final String date = request.getParameter(DATE);
//        final String pages = request.getParameter(PAGES);
//        final String description = request.getParameter(DESCRIPTION);
//        final String text = request.getParameter(TEXT);
//        final Book book = new Book(name, new Author(author), Genre.FANTASY, LocalDate.parse(date), Integer.parseInt(pages), description, text);
//        try {
//            BookValidator.getInstance().validate(book);
//            ServiceFactory.getInstance().getBookService().registerBook(book);
//            request.getSession().setAttribute(COMMAND_RESULT, String.format(RESULT_MESSAGE, name));
//        } catch (ValidationException | RegisterException e) {
//            request.getSession().setAttribute(COMMAND_RESULT, e.getMessage());
//        }
        return null;
    }


    private static class Singleton {
        private static final AddBookCommand INSTANCE = new AddBookCommand();
    }


}
