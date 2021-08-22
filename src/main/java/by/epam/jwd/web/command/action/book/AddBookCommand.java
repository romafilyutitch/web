package by.epam.jwd.web.command.action.book;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;
import by.epam.jwd.web.resource.CommandManager;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.service.BookService;
import by.epam.jwd.web.validation.Validation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
    private static final Logger logger = LogManager.getLogger(AddBookCommand.class);
    private final BookService bookService = BookService.getInstance();
    private final Validation<Book> bookValidation = Validation.getBookValidation();
    private static final String REQUEST_NAME_PARAMETER_KEY = "name";
    private static final String REQUEST_AUTHOR_PARAMETER_KEY = "author";
    private static final String REQUEST_GENRE_PARAMETER_KEY = "genre";
    private static final String REQUEST_PAGES_PARAMETER_KEY = "pages";
    private static final String REQUEST_DESCRIPTION_PARAMETER_KEY = "text";
    private static final String BOOK_EXISTS_MESSAGE_KEY = "book.register.exists";
    private static final String BOOK_REGISTERED_MESSAGE_KEY = "book.register.registered";
    private static final String REQUEST_MESSAGE_ATTRIBUTE_KEY = "message";
    private static final String COMMAND_REQUESTED_MESSAGE = "Add book new book command was requested";
    private static final String COMMAND_EXECUTED_MESSAGE = "Add new book command was executed";
    private static final String INVALID_BOOK_MESSAGE = "Can't add new book. Invalid book was get from request";
    private static final String BOOK_WITH_NAME_EXISTS_MESSAGE = "Cant add new book. Book with passed name exists";

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
        logger.info(COMMAND_REQUESTED_MESSAGE);
        final Book bookFromRequest = buildBookFromRequest(request);
        final List<String> validationMessages = bookValidation.validate(bookFromRequest);
        if (validationMessages.isEmpty()) {
            final Optional<Book> optionalBookByName = bookService.findByName(bookFromRequest.getName());
            if (optionalBookByName.isPresent()) {
                logger.info(BOOK_WITH_NAME_EXISTS_MESSAGE);
                request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(BOOK_EXISTS_MESSAGE_KEY));
            } else {
                bookService.save(bookFromRequest);
                request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(BOOK_REGISTERED_MESSAGE_KEY));
            }
        } else {
            logger.info(INVALID_BOOK_MESSAGE);
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, validationMessages);
        }
        logger.info(COMMAND_EXECUTED_MESSAGE);
        return CommandManager.getShowBooksCommand();
    }

    private Book buildBookFromRequest(HttpServletRequest request) {
        final String name = request.getParameter(REQUEST_NAME_PARAMETER_KEY);
        final String author = request.getParameter(REQUEST_AUTHOR_PARAMETER_KEY);
        final Genre genre = Genre.valueOf(request.getParameter(REQUEST_GENRE_PARAMETER_KEY));
        final int pages = Integer.parseInt(request.getParameter(REQUEST_PAGES_PARAMETER_KEY));
        final String description = request.getParameter(REQUEST_DESCRIPTION_PARAMETER_KEY);
        return new Book(name, author, genre, pages, description);
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
