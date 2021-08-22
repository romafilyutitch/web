package by.epam.jwd.web.command.action.comment;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.model.Comment;
import by.epam.jwd.web.resource.CommandManager;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.service.CommentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * Executes command that is delete saved {@link by.epam.jwd.web.model.Comment} from database table.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class DeleteCommentCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(DeleteCommentCommand.class);
    private final CommentService commentService = CommentService.getInstance();
    private static final String COMMAND_REQUESTED_MESSAGE = "Delete comment command was requested";
    private static final String COMMAND_EXECUTED_MESSAGE = "Delete comment command was executed";
    private static final String REQUEST_COMMENT_ID_PARAMETER_KEY = "id";
    private static final String REQUEST_MESSAGE_ATTRIBUTE_KEY = "message";
    private static final String COMMENT_WAS_DELETED_MESSAGE_KEY = "comment.deleted";

    private DeleteCommentCommand() {
    }

    /**
     * Get single class instance from nested class.
     * @return class instance.
     */
    public static DeleteCommentCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Deletes saved {@link by.epam.jwd.web.model.Comment} from database table.
     * Request must contain comment id that need to be deleted.
     * @param request request that need to be execute
     * @return main command for forward.
     */
    @Override
    public String execute(HttpServletRequest request) {
        logger.info(COMMAND_REQUESTED_MESSAGE);
        final Long commentId = Long.valueOf(request.getParameter(REQUEST_COMMENT_ID_PARAMETER_KEY));
        final Comment foundComment = commentService.findById(commentId);
        commentService.delete(foundComment);
        request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(COMMENT_WAS_DELETED_MESSAGE_KEY));
        logger.info(COMMAND_EXECUTED_MESSAGE);
        return CommandManager.getMainCommand();
    }

    /**
     * Nested class that encapsulates single {@link DeleteCommentCommand} instance.
     * Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final DeleteCommentCommand INSTANCE = new DeleteCommentCommand();
    }
}
