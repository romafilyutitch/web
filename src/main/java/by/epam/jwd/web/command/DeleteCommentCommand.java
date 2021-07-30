package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Comment;
import by.epam.jwd.web.service.CommentService;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class DeleteCommentCommand implements ActionCommand{
    private final CommentService commentService = ServiceFactory.getInstance().getCommentService();

    private static final String REQUEST_COMMENT_ID_PARAMETER_KEY = "id";
    private static final String SESSION_SUCCESS_ATTRIBUTE_KEY = "success";
    private static final String COMMENT_DELETED_LOCALIZATION_MESSAGE_KEY = "commentDeleted";

    private static final String RESULT_PATH = "controller?command=main";

    private DeleteCommentCommand() {}

    public static DeleteCommentCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final Long commentId = Long.valueOf(request.getParameter(REQUEST_COMMENT_ID_PARAMETER_KEY));
        commentService.delete(commentId);
        request.getSession().setAttribute(SESSION_SUCCESS_ATTRIBUTE_KEY, COMMENT_DELETED_LOCALIZATION_MESSAGE_KEY);
        return new CommandResult() {
            @Override
            public String getResultPath() {
                return RESULT_PATH;
            }

            @Override
            public boolean isRedirect() {
                return true;
            }
        };
    }

    private static class Singleton {
        private static final DeleteCommentCommand INSTANCE = new DeleteCommentCommand();
    }
}
