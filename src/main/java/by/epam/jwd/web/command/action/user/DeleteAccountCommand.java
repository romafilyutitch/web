package by.epam.jwd.web.command.action.user;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.resource.ConfigurationManager;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.service.ServiceFactory;
import by.epam.jwd.web.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Executes command that is delete save {@link User} from database table.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class DeleteAccountCommand implements ActionCommand {
    private final UserService userService = ServiceFactory.getInstance().getUserService();

    private static final String REQUEST_MESSAGE_ATTRIBUTE_KEY = "message";
    private static final String SESSION_USER_ATTRIBUTE_KEY = "user";
    private static final String ACCOUNT_WAS_DELETED_MESSAGE_KEY = "account.deleted";

    private DeleteAccountCommand() {
    }

    /**
     * Gets class instance from nested class.
     * @return class instance.
     */
    public static DeleteAccountCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Deletes saved {@link User} from database table.
     * @param request request that need to be execute
     * @return account page path for forward.
     */
    @Override
    public String execute(HttpServletRequest request) {
        final HttpSession session = request.getSession();
        final User user = (User) session.getAttribute(SESSION_USER_ATTRIBUTE_KEY);
        userService.delete(user.getId());
        session.invalidate();
        request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(ACCOUNT_WAS_DELETED_MESSAGE_KEY));
        return ConfigurationManager.getAccountPagePath();
    }

    /**
     * Nested class that encapsulates single {@link DeleteAccountCommand} instance.
     * Singleton pattern variation.
     */
    private static class Singleton {
        private static final DeleteAccountCommand INSTANCE = new DeleteAccountCommand();
    }
}
