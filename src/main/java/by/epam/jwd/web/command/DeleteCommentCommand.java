package by.epam.jwd.web.command;

import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.service.CommentService;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class DeleteCommentCommand implements ActionCommand {
    private final CommentService commentService = ServiceFactory.getInstance().getCommentService();

    private static final String REQUEST_COMMENT_ID_PARAMETER_KEY = "id";
    private static final String REQUEST_MESSAGE_ATTRIBUTE_KEY = "message";
    private static final String COMMENT_WAS_DELETED_MESSAGE_KEY = "comment.deleted";

    private static final String RESULT_PATH = "controller?command=main";

    private DeleteCommentCommand() {
    }

    public static DeleteCommentCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        final Long commentId = Long.valueOf(request.getParameter(REQUEST_COMMENT_ID_PARAMETER_KEY));
        commentService.delete(commentId);
        request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(COMMENT_WAS_DELETED_MESSAGE_KEY));
        return RESULT_PATH;
    }

    private static class Singleton {
        private static final DeleteCommentCommand INSTANCE = new DeleteCommentCommand();
    }
}
