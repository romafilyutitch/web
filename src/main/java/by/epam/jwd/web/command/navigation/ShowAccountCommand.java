package by.epam.jwd.web.command.navigation;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.resource.PathManager;
import by.epam.jwd.web.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Executes command that is forward to account page.
 *
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class ShowAccountCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(ShowAccountCommand.class);
    private final UserService userService = UserService.getInstance();
    private static final String COMMAND_REQUESTED_MESSAGE = "Show account command was requested";
    private static final String COMMAND_EXECUTED_MESSAGE = "Show account command was executed";

    private static final String SESSION_CURRENT_USER_ATTRIBUTE_KEY = "user";

    private ShowAccountCommand() {
    }

    /**
     * Gets single class instance from nester class.
     *
     * @return class instance.
     */
    public static ShowAccountCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Makes forward to account page.
     *
     * @param request request that need to be execute
     * @return account page path for forward.
     */
    @Override
    public String execute(HttpServletRequest request) {
        logger.info(COMMAND_REQUESTED_MESSAGE);
        final HttpSession userSession = request.getSession();
        final User currentUser = (User) userSession.getAttribute(SESSION_CURRENT_USER_ATTRIBUTE_KEY);
        final User foundUser = userService.findById(currentUser.getId());
        userSession.setAttribute(SESSION_CURRENT_USER_ATTRIBUTE_KEY, foundUser);
        logger.info(COMMAND_EXECUTED_MESSAGE);
        return PathManager.getAccountPagePath();
    }

    /**
     * Nested class that encapsulated single {@link ShowAccountCommand} instance.
     * Singleton pattern variation.
     *
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final ShowAccountCommand INSTANCE = new ShowAccountCommand();
    }
}
