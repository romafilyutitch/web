package by.epam.jwd.web.command.action.comment;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Comment;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.resource.ConfigurationManager;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.service.BookService;
import by.epam.jwd.web.service.CommentService;
import by.epam.jwd.web.service.ServiceFactory;
import by.epam.jwd.web.validation.Validation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.List;

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

    public static AddCommentCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        final Comment commentFromRequest = buildCommentFromRequest(request);
        final List<String> validationMessages = commentValidation.validate(commentFromRequest);
        if (!validationMessages.isEmpty()) {
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, validationMessages);
            return ConfigurationManager.getMainCommand();
        }
        commentService.save(commentFromRequest);
        request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(COMMENT_REGISTERED_MESSAGE_KEY));
        return ConfigurationManager.getMainCommand();
    }

    private Comment buildCommentFromRequest(HttpServletRequest request) {
        final HttpSession session = request.getSession();
        final User user = (User) session.getAttribute(SESSION_USER_ATTRIBUTE_KEY);
        final Long bookId = Long.valueOf(request.getParameter(REQUEST_BOOK_ID_PARAMETER_KEY));
        final String text = request.getParameter(REQUEST_TEXT_PARAMETER_KEY);
        final Book book = bookService.findById(bookId);
        return new Comment(user, book, LocalDate.now(), text);
    }

    private static class Singleton {
        private static final AddCommentCommand INSTANCE = new AddCommentCommand();
    }
}
