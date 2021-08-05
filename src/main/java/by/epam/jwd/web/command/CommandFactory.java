package by.epam.jwd.web.command;

import by.epam.jwd.web.command.navigation.MainCommand;

import javax.servlet.http.HttpServletRequest;

/**
 * Factory class that defines command that user wants to execute
 * finds command instance in {@link CommandEnum}.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class CommandFactory {

    public static final String COMMAND_PARAMETER_NAME = "command";

    private CommandFactory() {
    }

    /**
     * Returns {@link CommandFactory} instance from nested class that
     * contains single {@link CommandFactory} instance.
     * @return class instance
     */
    public static CommandFactory getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Gets command string value from request to define which {@link ActionCommand} instance
     * need to be executed and returns it instance.
     * @param request request formed by client
     * @return defined from request {@link ActionCommand} instance.
     */
    public ActionCommand defineCommand(HttpServletRequest request) {
        String action = request.getParameter(COMMAND_PARAMETER_NAME);
        if (action == null) {
            return MainCommand.getInstance();
        }
        try {
            CommandEnum currentEnum = CommandEnum.valueOf(action.toUpperCase());
            return currentEnum.getCurrentCommand();
        } catch (IllegalArgumentException e) {
            return MainCommand.getInstance();
        }
    }

    /**
     * Nested class that encapsulates single {@link CommandFactory} instance.
     * Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final CommandFactory INSTANCE = new CommandFactory();
    }
}
