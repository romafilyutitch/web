package by.epam.jwd.web.command;

import javax.servlet.http.HttpServletRequest;

public class ActionFactory {

    public static final String COMMAND_PARAMETER_NAME = "command";

    private ActionFactory() {
    }

    public static ActionFactory getInstance() {
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
        private static final ActionFactory INSTANCE = new ActionFactory();
    }
}
