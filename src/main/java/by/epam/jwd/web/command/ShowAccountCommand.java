package by.epam.jwd.web.command;

import by.epam.jwd.web.resource.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;

public class ShowAccountCommand implements ActionCommand {

    private ShowAccountCommand() {
    }

    public static ShowAccountCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        return ConfigurationManager.getAccountPagePath();
    }

    private static class Singleton {
        private static final ShowAccountCommand INSTANCE = new ShowAccountCommand();
    }
}
