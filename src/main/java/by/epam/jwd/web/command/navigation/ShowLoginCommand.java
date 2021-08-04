package by.epam.jwd.web.command.navigation;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.resource.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;

public class ShowLoginCommand implements ActionCommand {

    private ShowLoginCommand() {
    }

    public static ShowLoginCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        return ConfigurationManager.getLoginPagePath();
    }

    private static class Singleton {
        private static final ShowLoginCommand INSTANCE = new ShowLoginCommand();
    }
}
