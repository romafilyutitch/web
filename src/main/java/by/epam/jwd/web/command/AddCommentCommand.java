package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Comment;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.BookService;
import by.epam.jwd.web.service.CommentService;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;

public class AddCommentCommand implements ActionCommand {
    private final BookService bookService = ServiceFactory.getInstance().getBookService();
    private final CommentService commentService = ServiceFactory.getInstance().getCommentService();

    private static final String SESSION_USER_ATTRIBUTE_KEY = "user";
    private static final String REQUEST_BOOK_ID_PARAMETER_KEY = "bookId";
    private static final String REQUEST_TEXT_PARAMETER_KEY = "text";
    private static final String SESSION_SUCCESS_ATTRIBUTE_KEY = "success";
    private static final String COMMENT_ADDED_LOCALIZATION_MESSAGE_KEY = "commentAdded";

    private static final String RESULT_PATH = "controller?command=main";

    private AddCommentCommand() {}

    public static AddCommentCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final HttpSession session = request.getSession();
        final User user = (User) session.getAttribute(SESSION_USER_ATTRIBUTE_KEY);
        final Long bookId = Long.valueOf(request.getParameter(REQUEST_BOOK_ID_PARAMETER_KEY));
        final String text = request.getParameter(REQUEST_TEXT_PARAMETER_KEY);
        final Book book = bookService.findById(bookId);
        final Comment comment = new Comment(user, book, LocalDate.now(), text);
        commentService.save(comment);
        session.setAttribute(SESSION_SUCCESS_ATTRIBUTE_KEY, COMMENT_ADDED_LOCALIZATION_MESSAGE_KEY);
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
        private static final AddCommentCommand INSTANCE = new AddCommentCommand();
    }
}
