package by.epam.jwd.web.command.action.book.find;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Comment;
import by.epam.jwd.web.resource.ConfigurationManager;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.service.BookService;
import by.epam.jwd.web.service.CommentService;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Command that finds Book by entered name.
 * Used when client wants to find book by name.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class FindBookByNameCommand implements ActionCommand {
    private final BookService bookService = ServiceFactory.getInstance().getBookService();
    private final CommentService commentService = ServiceFactory.getInstance().getCommentService();

    private static final String REQUEST_BOOK_NAME_PARAMETER_KEY = "name";
    private static final String REQUEST_BOOKS_ATTRIBUTE_KEY = "books";
    private static final String REQUEST_COMMENTS_ATTRIBUTE_KEY = "comments";
    private static final String REQUEST_MESSAGE_ATTRIBUTE_KEY = "message";
    private static final String BOOK_WAS_FOUND_BY_NAME_MESSAGE_KEY = "book.find.name.found";
    private static final String BOOK_WAS_NOT_FOUND_BY_NAME_MESSAGE_KEY = "book.find.name.notFound";


    private FindBookByNameCommand() {
    }

    /**
     * Gets single class instance from nested class.
     * @return class instance.
     */
    public static FindBookByNameCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Executes command with help of services and makes request to client and returns result path or command
     * to make forward.
     * @param request request that need to be executed.
     * @return main page path for forward.
     */
    @Override
    public String execute(HttpServletRequest request) {
        final String bookName = request.getParameter(REQUEST_BOOK_NAME_PARAMETER_KEY).trim();
        final Optional<Book> optionalBook = bookService.findByName(bookName);
        if (optionalBook.isPresent()) {
            final List<Book> foundBook = Collections.singletonList(optionalBook.get());
            final List<Comment> bookComments = findComments(foundBook);
            request.setAttribute(REQUEST_BOOKS_ATTRIBUTE_KEY, foundBook);
            request.setAttribute(REQUEST_COMMENTS_ATTRIBUTE_KEY, bookComments);
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(BOOK_WAS_FOUND_BY_NAME_MESSAGE_KEY));
        } else {
            request.setAttribute(REQUEST_BOOKS_ATTRIBUTE_KEY, Collections.emptyList());
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(BOOK_WAS_NOT_FOUND_BY_NAME_MESSAGE_KEY));
        }
        return ConfigurationManager.getMainPagePath();
    }

    private List<Comment> findComments(List<Book> books) {
        final List<Comment> comments = new ArrayList<>();
        for (Book book : books) {
            comments.addAll(commentService.findByBook(book));
        }
        return comments;
    }

    /**
     * Nested class that encapsulates single {@link FindBookByNameCommand} instance.
     * Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final FindBookByNameCommand INSTANCE = new FindBookByNameCommand();
    }
}
