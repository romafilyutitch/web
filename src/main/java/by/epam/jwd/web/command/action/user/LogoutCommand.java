package by.epam.jwd.web.command.action.user;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.resource.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Executes command that is log out user.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class LogoutCommand implements ActionCommand {

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
        final HttpSession session = request.getSession();
        session.invalidate();
        return ConfigurationManager.getMainCommand();
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
