package by.epam.jwd.web.command.action.book;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.resource.CommandManager;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.service.BookService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * Executes command that is delete {@link Book} instance from database book table.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class DeleteBookCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(DeleteBookCommand.class);
    private final BookService bookService = BookService.getInstance();
    private static final String COMMAND_REQUESTED_MESSAGE = "Delete book command was requested";
    private static final String COMMAND_EXECUTED_MESSAGE = "Delete book command was executed";
    private static final String REQUEST_BOOK_ID_PARAMETER_KEY = "id";
    private static final String REQUEST_MESSAGE_ATTRIBUTE_KEY = "message";
    private static final String BOOK_WAS_DELETED_MESSAGE_KEY = "book.deleted";

    private DeleteBookCommand() {
    }

    /**
     * Gets single class instance from nested class.
     * @return class instance.
     */
    public static DeleteBookCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Deletes saved {@link Book} instance from database.
     * Request must contain book id that need to be deleted.
     * @param request request that need to be execute.
     * @return show books command for forward.
     */
    @Override
    public String execute(HttpServletRequest request) {
        logger.info(COMMAND_REQUESTED_MESSAGE);
        final Long id = Long.valueOf(request.getParameter(REQUEST_BOOK_ID_PARAMETER_KEY));
        final Book book = bookService.findById(id);
        bookService.delete(book);
        request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(BOOK_WAS_DELETED_MESSAGE_KEY));
        logger.info(COMMAND_EXECUTED_MESSAGE);
        return CommandManager.getShowBooksCommand();
    }

    /**
     * Nested class that encapsulates single {@link DeleteBookCommand} instance.
     * Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final DeleteBookCommand INSTANCE = new DeleteBookCommand();
    }
}
