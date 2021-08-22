package by.epam.jwd.web.command.action.user;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.resource.CommandManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Executes command that is log out user.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class LogoutCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(LogoutCommand.class);
    private static final String COMMAND_REQUESTED_MESSAGE = "Logout command was requested";
    private static final String COMMAND_EXECUTED_MESSAGE = "Logout command was executed";

    private LogoutCommand() {
    }

    /**
     * Gets single class instance from nested class.
     * @return class instance.
     */
    public static LogoutCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Makes user log out.
     * @param request request that need to be execute.
     * @return main command for forward.
     */
    @Override
    public String execute(HttpServletRequest request) {
        logger.info(COMMAND_REQUESTED_MESSAGE);
        final HttpSession session = request.getSession();
        session.invalidate();
        logger.info(COMMAND_EXECUTED_MESSAGE);
        return CommandManager.getMainCommand();
    }

    /**
     * Nested class that encapsulates single {@link LogoutCommand} instance.
     * Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final LogoutCommand INSTANCE = new LogoutCommand();
    }
}
