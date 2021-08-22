package by.epam.jwd.web.command.action.user;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.resource.CommandManager;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Executes command that is delete save {@link User} from database table.
 *
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class DeleteAccountCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(DeleteAccountCommand.class);
    private final UserService userService = UserService.getInstance();
    private static final String COMMAND_REQUESTED_MESSAGE = "Delete account command was requested";
    private static final String COMMAND_EXECUTE_MESSAGE = "Delete account command was executed";
    private static final String REQUEST_MESSAGE_ATTRIBUTE_KEY = "message";
    private static final String SESSION_USER_ATTRIBUTE_KEY = "user";
    private static final String ACCOUNT_WAS_DELETED_MESSAGE_KEY = "account.deleted";

    private DeleteAccountCommand() {
    }

    /**
     * Gets class instance from nested class.
     *
     * @return class instance.
     */
    public static DeleteAccountCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Deletes saved {@link User} from database table.
     *
     * @param request request that need to be execute
     * @return main command for forward.
     */
    @Override
    public String execute(HttpServletRequest request) {
        logger.info(COMMAND_REQUESTED_MESSAGE);
        final HttpSession session = request.getSession();
        final User user = (User) session.getAttribute(SESSION_USER_ATTRIBUTE_KEY);
        userService.delete(user);
        session.invalidate();
        request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(ACCOUNT_WAS_DELETED_MESSAGE_KEY));
        logger.info(COMMAND_EXECUTE_MESSAGE);
        return CommandManager.getMainCommand();
    }

    /**
     * Nested class that encapsulates single {@link DeleteAccountCommand} instance.
     * Singleton pattern variation.
     */
    private static class Singleton {
        private static final DeleteAccountCommand INSTANCE = new DeleteAccountCommand();
    }
}
