package by.epam.jwd.web.command.action.comment;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Comment;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.resource.CommandManager;
import by.epam.jwd.web.resource.PathManager;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.service.BookService;
import by.epam.jwd.web.service.CommentService;
import by.epam.jwd.web.service.ServiceFactory;
import by.epam.jwd.web.validation.Validation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

/**
 * Executes command that is add new {@link Comment} to {@link Book}.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class AddCommentCommand implements ActionCommand {
    private final BookService bookService = ServiceFactory.getInstance().getBookService();
    private final CommentService commentService = ServiceFactory.getInstance().getCommentService();
    private final Validation<Comment> commentValidation = Validation.getCommentValidation();

    private static final String SESSION_USER_ATTRIBUTE_KEY = "user";
    private static final String REQUEST_BOOK_ID_PARAMETER_KEY = "bookId";
    private static final String REQUEST_TEXT_PARAMETER_KEY = "text";
    private static final String REQUEST_MESSAGE_ATTRIBUTE_KEY = "message";
    private static final String COMMENT_REGISTERED_MESSAGE_KEY = "comment.register.registered";

    private AddCommentCommand() {
    }

    /**
     * Gets single class instance from singleton class.
     * @return class instance.
     */
    public static AddCommentCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Added new {@link Comment} to {@link Book}.
     * Request must have comments data from user.
     * @param request request that need to be execute.
     * @return main command for forward.
     */
    @Override
    public String execute(HttpServletRequest request) {
        final Comment commentFromRequest = buildCommentFromRequest(request);
        final List<String> validationMessages = commentValidation.validate(commentFromRequest);
        if (!validationMessages.isEmpty()) {
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, validationMessages);
            return CommandManager.getCommand("main");
        }
        commentService.save(commentFromRequest);
        request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(COMMENT_REGISTERED_MESSAGE_KEY));
        return CommandManager.getCommand("main");
    }

    private Comment buildCommentFromRequest(HttpServletRequest request) {
        final HttpSession session = request.getSession();
        final User user = (User) session.getAttribute(SESSION_USER_ATTRIBUTE_KEY);
        final Long bookId = Long.valueOf(request.getParameter(REQUEST_BOOK_ID_PARAMETER_KEY));
        final String text = request.getParameter(REQUEST_TEXT_PARAMETER_KEY);
        final Book book = bookService.findById(bookId);
        return new Comment(user, book, LocalDate.now(), text);
    }

    /**
     * Nested class that encapsulates single {@link AddCommentCommand} instance.
     * Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final AddCommentCommand INSTANCE = new AddCommentCommand();
    }
}
