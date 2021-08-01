package by.epam.jwd.web.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class LogoutCommand implements ActionCommand {

    private static final String RESULT_PATH = "controller?command=main";

    private LogoutCommand() {
    }

    public static LogoutCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        final HttpSession session = request.getSession();
        session.invalidate();
        return RESULT_PATH;
    }

    private static class Singleton {
        private static final LogoutCommand INSTANCE = new LogoutCommand();
    }
}
