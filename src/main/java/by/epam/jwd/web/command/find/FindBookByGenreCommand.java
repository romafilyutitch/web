package by.epam.jwd.web.command.find;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Comment;
import by.epam.jwd.web.model.Genre;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.resource.PathManager;
import by.epam.jwd.web.service.BookService;
import by.epam.jwd.web.service.CommentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Command that finds saved books by genre that was passed by client
 * and returns find result and forms message
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class FindBookByGenreCommand implements ActionCommand {
    private final static Logger logger = LogManager.getLogger(FindBookByGenreCommand.class);
    private final BookService bookService = BookService.getInstance();
    private final CommentService commentService = CommentService.getInstance();
    private static final String COMMAND_REQUESTED_MESSAGE = "Find book by genre command was requested";
    private static final String COMMAND_EXECUTED_MESSAGE = "Find book by genre command was executed";
    private static final String REQUEST_GENRE_PARAMETER_KEY = "genre";
    private static final String REQUEST_MESSAGE_ATTRIBUTE_KEY = "message";
    private static final String REQUEST_BOOKS_PARAMETER_KEY = "books";
    private static final String REQUEST_COMMENTS_PARAMETER_KEY = "comments";
    private static final String REQUEST_MESSAGE_PARAMETER_KEY = "message";
    private static final String NO_BOOKS_BY_GENRE_MESSAGE_KEY = "book.find.genre.notFound";
    private static final String BOOKS_WERE_FOUND_BY_GENRE_MESSAGE_KEY = "book.find.genre.found";

    private FindBookByGenreCommand() {
    }

    /**
     * Returns class instance
     * @return instance
     */
    public static FindBookByGenreCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Finds books by genre that client passed in request.
     * Request must contain genre parameter.
     * @param request request that need to be execute
     * @return command result path or command result command
     */
    @Override
    public String execute(HttpServletRequest request) {
        logger.info(COMMAND_REQUESTED_MESSAGE);
        final String genreParameter = request.getParameter(REQUEST_GENRE_PARAMETER_KEY);
        final Genre genre = Genre.valueOf(genreParameter.toUpperCase());
        final List<Book> books = bookService.findByGenre(genre);
        if (books.isEmpty()) {
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(NO_BOOKS_BY_GENRE_MESSAGE_KEY));
        } else {
            final List<Comment> comments = findComments(books);
            request.setAttribute(REQUEST_BOOKS_PARAMETER_KEY, books);
            request.setAttribute(REQUEST_COMMENTS_PARAMETER_KEY, comments);
            request.setAttribute(REQUEST_MESSAGE_PARAMETER_KEY, MessageManager.getMessage(BOOKS_WERE_FOUND_BY_GENRE_MESSAGE_KEY));
        }
        logger.info(COMMAND_EXECUTED_MESSAGE);
        return PathManager.getMainPagePath();
    }

    private List<Comment> findComments(List<Book> books) {
        final List<Comment> comments = new ArrayList<>();
        for (Book book : books) {
            comments.addAll(commentService.findByBook(book));
        }
        return comments;
    }

    private static class Singleton {
        private static final FindBookByGenreCommand INSTANCE = new FindBookByGenreCommand();
    }
}
