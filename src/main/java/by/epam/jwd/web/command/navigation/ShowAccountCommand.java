package by.epam.jwd.web.command.navigation;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.resource.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;

/**
 * Executes command that is forward to account page.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class ShowAccountCommand implements ActionCommand {

    private ShowAccountCommand() {
    }

    /**
     * Gets single class instance from nester class.
     * @return class instance.
     */
    public static ShowAccountCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Makes forward to account page.
     * @param request request that need to be execute
     * @return account page path for forward.
     */
    @Override
    public String execute(HttpServletRequest request) {
        return ConfigurationManager.getAccountPagePath();
    }

    /**
     * Nested class that encapsulated single {@link ShowAccountCommand} instance.
     * Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final ShowAccountCommand INSTANCE = new ShowAccountCommand();
    }
}
