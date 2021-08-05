package by.epam.jwd.web.command.action.book;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.resource.ConfigurationManager;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.service.BookService;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

/**
 * Executes command that is remove one copy of saved {@link Book} instance.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class RemoveCopyCommand implements ActionCommand {
    private final BookService bookService = ServiceFactory.getInstance().getBookService();

    private static final String REQUEST_BOOK_ID_PARAMETER_KEY = "id";
    private static final String REQUEST_MESSAGE_ATTRIBUTE_KEY = "message";
    private static final String BOOK_COPY_WAS_REMOVED_MESSAGE_KEY = "book.copy.removed";

    private RemoveCopyCommand() {
    }

    /**
     * Gets single class instance from nested class.
     * @return class instance.
     */
    public static RemoveCopyCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Removed one copy of saved {@link Book} instance.
     * Request must contain book id that need to remove one copy.
     * @param request request that need to be execute.
     * @return show books command for forward.
     */
    @Override
    public String execute(HttpServletRequest request) {
        final Long id = Long.valueOf(request.getParameter(REQUEST_BOOK_ID_PARAMETER_KEY));
        final Book book = bookService.findById(id);
        bookService.removeOneCopy(book);
        request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(BOOK_COPY_WAS_REMOVED_MESSAGE_KEY));
        return ConfigurationManager.getShowBooksCommand();
    }

    /**
     * Nested class that encapsulates single {@link RemoveCopyCommand} instance.
     * Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final RemoveCopyCommand INSTANCE = new RemoveCopyCommand();
    }
}
