package by.epam.jwd.web.command.navigation;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.resource.PathManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;

/**
 * Executes command that is form register page path to forward.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class ShowRegisterCommand implements ActionCommand {
    private static final Logger logger = LogManager.getLogger(ShowRegisterCommand.class);
    private static final String COMMAND_REQUESTED_MESSAGE = "Show register command was requested";
    private static final String COMMAND_EXECUTED_MESSAGE = "Show register command was executed";

    private ShowRegisterCommand() {
    }

    /**
     * Gets single class instance from nested class.
     * @return class instance.
     */
    public static ShowRegisterCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Forms register page path for forward.
     * @param request request that need to be execute.
     * @return register path for forward.
     */
    @Override
    public String execute(HttpServletRequest request) {
        logger.info(COMMAND_REQUESTED_MESSAGE);
        logger.info(COMMAND_EXECUTED_MESSAGE);
        return PathManager.getRegisterPagePath();
    }

    /**
     * Nested class that encapsulated single {@link ShowRegisterCommand} instance.
     * Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final ShowRegisterCommand INSTANCE = new ShowRegisterCommand();
    }
}
