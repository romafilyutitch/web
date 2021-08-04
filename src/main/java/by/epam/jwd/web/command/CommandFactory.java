package by.epam.jwd.web.command;

import by.epam.jwd.web.command.navigation.MainCommand;

import javax.servlet.http.HttpServletRequest;

public class CommandFactory {

    public static final String COMMAND_PARAMETER_NAME = "command";

    private CommandFactory() {
    }

    public static CommandFactory getInstance() {
        return Singleton.INSTANCE;
    }

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

    private static class Singleton {
        private static final CommandFactory INSTANCE = new CommandFactory();
    }
}
