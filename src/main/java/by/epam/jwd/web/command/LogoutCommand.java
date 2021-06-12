package by.epam.jwd.web.command;

import javax.servlet.http.HttpServletRequest;

public class LogoutCommand implements ActionCommand {

    private LogoutCommand() {}

    public static LogoutCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        request.getSession().invalidate();
        return null;
    }

    private static class Singleton {
        private static final LogoutCommand INSTANCE = new LogoutCommand();
    }
}
