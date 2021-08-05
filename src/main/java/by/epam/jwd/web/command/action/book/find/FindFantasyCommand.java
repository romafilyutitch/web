package by.epam.jwd.web.command.action.book.find;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Comment;
import by.epam.jwd.web.model.Genre;
import by.epam.jwd.web.resource.PathManager;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.service.BookService;
import by.epam.jwd.web.service.CommentService;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * Executes command that is find book by genre that is one of {@link Genre#FANTASY} enumeration value.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class FindFantasyCommand implements ActionCommand {
    private final BookService bookService = ServiceFactory.getInstance().getBookService();
    private final CommentService commentService = ServiceFactory.getInstance().getCommentService();

    private static final String REQUEST_BOOKS_ATTRIBUTE_KEY = "books";
    private static final String REQUEST_MESSAGE_ATTRIBUTE_KEY = "message";
    private static final String REQUEST_COMMENTS_ATTRIBUTE_KEY = "comments";
    private static final String FANTASY_BOOKS_WERE_FOUND_MESSAGE_KEY = "book.find.fantasy.found";
    private static final String FANTASY_BOOKS_WERE_NOT_FOUND_MESSAGE_KEY = "book.find.fantasy.notFound";

    private FindFantasyCommand() {
    }

    /**
     * Gets singleton from nested class.
     * @return class instance.
     */
    public static FindFantasyCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Finds all fantasy {@link Genre} books.
     * @param request request that need to be execute.
     * @return main page path for forward.
     */
    @Override
    public String execute(HttpServletRequest request) {
        final List<Book> fantasyBooks = bookService.findByGenre(Genre.FANTASY);
        if (fantasyBooks.isEmpty()) {
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(FANTASY_BOOKS_WERE_NOT_FOUND_MESSAGE_KEY));
        } else {
            request.setAttribute(REQUEST_BOOKS_ATTRIBUTE_KEY, fantasyBooks);
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(FANTASY_BOOKS_WERE_FOUND_MESSAGE_KEY));
            final List<Comment> comments = findComments(fantasyBooks);
            request.setAttribute(REQUEST_COMMENTS_ATTRIBUTE_KEY, comments);
        }
        return PathManager.getPath("main");
    }

    private List<Comment> findComments(List<Book> books) {
        final List<Comment> comments = new ArrayList<>();
        for (Book book : books) {
            comments.addAll(commentService.findByBook(book));
        }
        return comments;
    }

    /**
     * Nested class that encapsulates single {@link FindFantasyCommand} instance.
     * Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final FindFantasyCommand INSTANCE = new FindFantasyCommand();
    }
}
