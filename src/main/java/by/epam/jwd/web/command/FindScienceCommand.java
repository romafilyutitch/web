package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Comment;
import by.epam.jwd.web.model.Genre;
import by.epam.jwd.web.service.BookService;
import by.epam.jwd.web.service.CommentService;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class FindScienceCommand implements ActionCommand {
    private final BookService bookService = ServiceFactory.getInstance().getBookService();
    private final CommentService commentService = ServiceFactory.getInstance().getCommentService();

    private static final String REQUEST_BOOKS_ATTRIBUTE_KEY = "books";
    private static final String REQUEST_FIND_RESULT_ATTRIBUTE_KEY = "findResult";
    private static final String BOOKS_WERE_FOUND_LOCALIZATION_MESSAGE_KEY = "booksFound";
    private static final String BOOKS_WERE_NOT_FOUND_LOCALIZATION_MESSAGE_KEY = "booksNotFound";

    public static final String RESULT_PATH = "WEB-INF/jsp/main.jsp";

    private FindScienceCommand() {
    }

    public static FindScienceCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final List<Book> scienceBooks = bookService.findByGenre(Genre.SCIENCE);
        if (scienceBooks.isEmpty()) {
            request.setAttribute(REQUEST_FIND_RESULT_ATTRIBUTE_KEY, BOOKS_WERE_NOT_FOUND_LOCALIZATION_MESSAGE_KEY);
        } else {
            request.setAttribute(REQUEST_BOOKS_ATTRIBUTE_KEY, scienceBooks);
            request.setAttribute(REQUEST_FIND_RESULT_ATTRIBUTE_KEY, BOOKS_WERE_FOUND_LOCALIZATION_MESSAGE_KEY);
            final List<Comment> comments = commentService.findAll();
            request.setAttribute("comments", comments);
        }
        return new CommandResult() {
            @Override
            public String getResultPath() {
                return RESULT_PATH;
            }

            @Override
            public boolean isRedirect() {
                return false;
            }
        };
    }

    private static class Singleton {
        private static final FindScienceCommand INSTANCE = new FindScienceCommand();
    }
}
