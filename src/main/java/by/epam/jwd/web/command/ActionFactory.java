package by.epam.jwd.web.command;

import javax.servlet.http.HttpServletRequest;

public class ActionFactory {

    private ActionFactory() {}

    public static ActionFactory getInstance() {
        return Singleton.INSTANCE;
    }

    public ActionCommand defineCommand(HttpServletRequest request) {
        String action = request.getParameter("command");
        CommandEnum currentEnum = CommandEnum.valueOf(action.toUpperCase());
        return currentEnum.getCurrentCommand();
    }


    private static class Singleton{
        private static final ActionFactory INSTANCE = new ActionFactory();
    }
}
