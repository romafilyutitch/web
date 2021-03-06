package by.epam.jwd.web.command.action.user;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.resource.CommandManager;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * Executes command that is promote user role.
 *
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class PromoteRoleCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(PromoteRoleCommand.class);
    private final UserService userService = UserService.getInstance();
    private static final String COMMAND_REQUESTED_MESSAGE = "Promote role command was requested";
    private static final String COMMAND_EXECUTED_MESSAGE = "Promote role command was executed";
    private static final String REQUEST_USER_ID_PARAMETER_KEY = "id";
    private static final String REQUEST_MESSAGE_ATTRIBUTE_KEY = "message";
    private static final String USER_ROLE_WAS_PROMOTED_MESSAGE_KEY = "user.role.promoted";

    private PromoteRoleCommand() {
    }

    /**
     * Gets single class instance from nested class.
     *
     * @return class instance.
     */
    public static PromoteRoleCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Promotes user role.
     * Request must have user id whose role need to promote.
     *
     * @param request request that need to be execute.
     * @return show users command for forward.
     */
    @Override
    public String execute(HttpServletRequest request) {
        logger.info(COMMAND_REQUESTED_MESSAGE);
        final Long userId = Long.valueOf(request.getParameter(REQUEST_USER_ID_PARAMETER_KEY));
        final User foundUser = userService.findById(userId);
        userService.promoteRole(foundUser);
        request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(USER_ROLE_WAS_PROMOTED_MESSAGE_KEY));
        logger.info(COMMAND_EXECUTED_MESSAGE);
        return CommandManager.getShowUsersCommand();
    }

    /**
     * Nested class that encapsulates single {@link PromoteRoleCommand} instance.
     * Singleton pattern variation.
     *
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final PromoteRoleCommand INSTANCE = new PromoteRoleCommand();
    }
}
