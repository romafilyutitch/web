package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Author;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;
import by.epam.jwd.web.resource.ConfigurationManager;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.service.BookService;
import by.epam.jwd.web.service.ServiceFactory;
import by.epam.jwd.web.validator.Validator;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class AddBookCommand implements ActionCommand {
    private final BookService bookService = ServiceFactory.getInstance().getBookService();
    private final Validator<Book> validator = Validator.getBookValidator();

    private static final String REQUEST_NAME_PARAMETER_KEY = "name";
    private static final String REQUEST_AUTHOR_PARAMETER_KEY = "author";
    private static final String REQUEST_GENRE_PARAMETER_KEY = "genre";
    private static final String REQUEST_PAGES_PARAMETER_KEY = "pages";
    private static final String REQUEST_DESCRIPTION_PARAMETER_KEY = "text";
    private static final String BOOK_EXISTS_MESSAGE_KEY = "book.register.exists";
    private static final String BOOK_REGISTERED_MESSAGE_KEY = "book.register.registered";
    private static final String REQUEST_MESSAGE_ATTRIBUTE_KEY = "message";

    private AddBookCommand() {}

    public static AddBookCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        final Book bookFromRequest = buildBookFromRequest(request);
        final Optional<Book> optionalBook = bookService.findByName(bookFromRequest.getName());
        if (!validator.validate(bookFromRequest)) {
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage("book.validation.invalid"));
            return ConfigurationManager.getShowBooksCommand();
        }
        if (optionalBook.isPresent()) {
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(BOOK_EXISTS_MESSAGE_KEY));
        } else {
            bookService.save(bookFromRequest);
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(BOOK_REGISTERED_MESSAGE_KEY));
        }
        return ConfigurationManager.getShowBooksCommand();
    }

    private Book buildBookFromRequest(HttpServletRequest request) {
        final String name = request.getParameter(REQUEST_NAME_PARAMETER_KEY);
        final String authorName = request.getParameter(REQUEST_AUTHOR_PARAMETER_KEY);
        final Genre genre = Genre.valueOf(request.getParameter(REQUEST_GENRE_PARAMETER_KEY));
        final int pages = Integer.parseInt(request.getParameter(REQUEST_PAGES_PARAMETER_KEY));
        final String description = request.getParameter(REQUEST_DESCRIPTION_PARAMETER_KEY);
        return new Book(name, new Author(authorName), genre, pages, description);
    }


    private static class Singleton {
        private static final AddBookCommand INSTANCE = new AddBookCommand();
    }


}
