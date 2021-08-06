package by.epam.jwd.web.command.action.book;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.model.Author;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;
import by.epam.jwd.web.resource.CommandManager;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.service.BookService;
import by.epam.jwd.web.service.ServiceFactory;
import by.epam.jwd.web.validation.Validation;
import by.epam.jwd.web.validation.ValidationFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

/**
 * Executes command that is add new book to add new {@link Book} based on request values.
 * Performs validation at first and then saved {@link Book} instance formed from request data.
 *
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class AddBookCommand implements ActionCommand {
    private final BookService bookService = ServiceFactory.getInstance().getBookService();
    private final Validation<Book> bookValidation = ValidationFactory.getInstance().getBookValidation();

    private static final String REQUEST_NAME_PARAMETER_KEY = "name";
    private static final String REQUEST_AUTHOR_PARAMETER_KEY = "author";
    private static final String REQUEST_GENRE_PARAMETER_KEY = "genre";
    private static final String REQUEST_PAGES_PARAMETER_KEY = "pages";
    private static final String REQUEST_DESCRIPTION_PARAMETER_KEY = "text";
    private static final String BOOK_EXISTS_MESSAGE_KEY = "book.register.exists";
    private static final String BOOK_REGISTERED_MESSAGE_KEY = "book.register.registered";
    private static final String REQUEST_MESSAGE_ATTRIBUTE_KEY = "message";

    private AddBookCommand() {
    }

    /**
     * Get single class instance from nested class.
     *
     * @return class instance.
     */
    public static AddBookCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Retrieves data from request and build {@link Book} instance from them
     * then performs instance validation and save instance in database.
     *
     * @param request request that need to be execute.
     * @return show book command for forward.
     */
    @Override
    public String execute(HttpServletRequest request) {
        final Book bookFromRequest = buildBookFromRequest(request);
        final List<String> validationMessages = bookValidation.validate(bookFromRequest);
        if (!validationMessages.isEmpty()) {
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, validationMessages);
            return CommandManager.getShowBooksCommand();
        }
        final Optional<Book> optionalBook = bookService.findByName(bookFromRequest.getName());
        if (optionalBook.isPresent()) {
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(BOOK_EXISTS_MESSAGE_KEY));
        } else {
            bookService.save(bookFromRequest);
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(BOOK_REGISTERED_MESSAGE_KEY));
        }
        return CommandManager.getShowBooksCommand();
    }

    private Book buildBookFromRequest(HttpServletRequest request) {
        final String name = request.getParameter(REQUEST_NAME_PARAMETER_KEY);
        final String authorName = request.getParameter(REQUEST_AUTHOR_PARAMETER_KEY);
        final Genre genre = Genre.valueOf(request.getParameter(REQUEST_GENRE_PARAMETER_KEY));
        final int pages = Integer.parseInt(request.getParameter(REQUEST_PAGES_PARAMETER_KEY));
        final String description = request.getParameter(REQUEST_DESCRIPTION_PARAMETER_KEY);
        return new Book(name, new Author(authorName), genre, pages, description);
    }


    /**
     * Nested class that encapsulates single {@link AddBookCommand} instance.
     * Singleton pattern variation.
     *
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final AddBookCommand INSTANCE = new AddBookCommand();
    }


}
