package by.epam.jwd.web.command.navigation;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.resource.PathManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * Executes command that is forward to login page.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class ShowLoginCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(ShowLoginCommand.class);
    private static final String COMMAND_REQUESTED_MESSAGE = "Show login command was requested";
    private static final String COMMAND_EXECUTED_MESSAGE = "Show login command was executed";

    private ShowLoginCommand() {
    }

    /**
     * Gets single class instance from nested class.
     * @return class instance.
     */
    public static ShowLoginCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Forms login page path to forward.
     * @param request request that need to be execute.
     * @return login page path for forward.
     */
    @Override
    public String execute(HttpServletRequest request) {
        logger.info(COMMAND_REQUESTED_MESSAGE);
        logger.info(COMMAND_EXECUTED_MESSAGE);
        return PathManager.getLoginPagePath();
    }

    /**
     * Nested class that encapsulates single {@link ShowLoginCommand} instance.
     * Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final ShowLoginCommand INSTANCE = new ShowLoginCommand();
    }
}
