package by.epam.jwd.web.command.find;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Comment;
import by.epam.jwd.web.model.Genre;
import by.epam.jwd.web.resource.PathManager;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.service.api.BookService;
import by.epam.jwd.web.service.api.CommentService;
import by.epam.jwd.web.service.api.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Executes command that is find all books with {@link Genre#FICTION} genre.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class FindFictionCommand implements ActionCommand {
    private final BookService bookService = ServiceFactory.getInstance().getBookService();
    private final CommentService commentService = ServiceFactory.getInstance().getCommentService();

    private static final String REQUEST_BOOKS_ATTRIBUTE_KEY = "books";
    private static final String REQUEST_COMMENTS_ATTRIBUTE_KEY = "comments";
    private static final String REQUEST_MESSAGE_ATTRIBUTE_KEY = "message";
    private static final String FICTION_BOOKS_WERE_FOUND_MESSAGE_KEY = "book.find.fiction.found";
    private static final String FICTION_BOOKS_WERE_NOT_FOUND_MESSAGE_KEY = "book.find.fiction.notFound";

    private FindFictionCommand() {
    }

    /**
     * Gets single class instance from nested class.
     * @return class instance.
     */
    public static FindFictionCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Finds all {@link Genre#FICTION} books.
     * @param request request that need to be execute.
     * @return main page path for forward.
     */
    @Override
    public String execute(HttpServletRequest request) {
        final List<Book> fictionBooks = bookService.findByGenre(Genre.FICTION);
        if (fictionBooks.isEmpty()) {
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(FICTION_BOOKS_WERE_NOT_FOUND_MESSAGE_KEY));
        } else {
            request.setAttribute(REQUEST_BOOKS_ATTRIBUTE_KEY, fictionBooks);
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(FICTION_BOOKS_WERE_FOUND_MESSAGE_KEY));
            final List<Comment> comments = findComments(fictionBooks);
            request.setAttribute(REQUEST_COMMENTS_ATTRIBUTE_KEY, comments);
        }
        return PathManager.getMainPagePath();
    }

    private List<Comment> findComments(List<Book> books) {
        final ArrayList<Comment> comments = new ArrayList<>();
        for (Book book : books) {
            comments.addAll(commentService.findByBook(book));
        }
        return comments;
    }

    /**
     * Nested class that encapsulates single {@link FindFantasyCommand} instance.
     * Singleton pattern variation.
     * @see "Singleton pattenr"
     */
    private static class Singleton {
        private static final FindFictionCommand INSTANCE = new FindFictionCommand();
    }
}
