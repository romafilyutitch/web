package by.epam.jwd.web.command.navigation;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.resource.PathManager;

import javax.servlet.http.HttpServletRequest;

/**
 * Executes command that is forward to login page.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class ShowLoginCommand implements ActionCommand {

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
