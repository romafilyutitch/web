package by.epam.jwd.web.command.action.like;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Like;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.resource.CommandManager;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.service.BookService;
import by.epam.jwd.web.service.LikeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * Executes command that is add new {@link Like} to {@link Book} from {@link User}.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class AddLikeCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(AddLikeCommand.class);
    private final LikeService likeService = LikeService.getInstance();
    private final BookService bookService = BookService.getInstance();
    private static final String COMMAND_REQUESTED_MESSAGE = "Add like command was requested";
    private static final String COMMAND_EXECUTED_MESSAGE = "Add like command was executed";
    private static final String LIKE_WILL_BE_REMOVE_MESSAGE = "Like exists already. Like will be removed";
    private static final String LIKE_WILL_BE_SAVED_MESSAGE = "Like not exists yet. Like will be saved";
    private static final String REQUEST_BOOK_ID_PARAMETER_KEY = "id";
    private static final String REQUEST_MESSAGE_ATTRIBUTE_KEY = "message";
    private static final String SESSION_USER_ATTRIBUTE_KEY = "user";
    private static final String LIKE_REMOVED_MESSAGE_KEY = "like.removed";
    private static final String LIKE_ADDED_MESSAGE_KEY = "like.added";

    private AddLikeCommand() {
    }

    /**
     * Gets single class instance from nested class.
     * @return class instance.
     */
    public static AddLikeCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Saves like from {@link User} to {@link Book} in database if that like
     * not exist or removes like if like exists.
     * Request must contain book id to which need to add like.
     * @param request request that need to be execute.
     * @return main command for forward.
     */
    @Override
    public String execute(HttpServletRequest request) {
        logger.info(COMMAND_REQUESTED_MESSAGE);
        final HttpSession session = request.getSession();
        final User user = (User) session.getAttribute(SESSION_USER_ATTRIBUTE_KEY);
        final Long bookId = Long.valueOf(request.getParameter(REQUEST_BOOK_ID_PARAMETER_KEY));
        final Book foundBook = bookService.findById(bookId);
        final Optional<Like> optionalLike = likeService.findByUserAndBook(user, foundBook);
        if (optionalLike.isPresent()) {
            logger.info(LIKE_WILL_BE_REMOVE_MESSAGE);
            final Like foundLike = optionalLike.get();
            likeService.delete(foundLike);
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(LIKE_REMOVED_MESSAGE_KEY));
        } else {
            logger.info(LIKE_WILL_BE_SAVED_MESSAGE);
            final Like like = new Like(user, foundBook);
            likeService.save(like);
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(LIKE_ADDED_MESSAGE_KEY));
        }
        logger.info(COMMAND_EXECUTED_MESSAGE);
        return CommandManager.getMainCommand();
    }

    /**
     * Nested class that encapsulated single {@link AddLikeCommand} instance.
     * Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final AddLikeCommand INSTANCE = new AddLikeCommand();
    }
}
