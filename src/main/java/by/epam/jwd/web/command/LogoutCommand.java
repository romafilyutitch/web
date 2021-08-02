package by.epam.jwd.web.command;

import by.epam.jwd.web.resource.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LogoutCommand implements ActionCommand {

    private LogoutCommand() {
    }

    public static LogoutCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        final HttpSession session = request.getSession();
        session.invalidate();
        return ConfigurationManager.getMainCommand();
    }

    private static class Singleton {
        private static final LogoutCommand INSTANCE = new LogoutCommand();
    }
}
