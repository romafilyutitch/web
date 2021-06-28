package by.epam.jwd.web.command;

import javax.servlet.http.HttpServletRequest;

public class LogoutCommand implements ActionCommand {

    private static final String RESULT_PATH = "index.jsp";

    private LogoutCommand() {
    }

    public static LogoutCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        request.getSession().invalidate();
        return new CommandResult() {
            @Override
            public String getResultPath() {
                return RESULT_PATH;
            }

            @Override
            public boolean isRedirect() {
                return true;
            }
        };
    }

    private static class Singleton {
        private static final LogoutCommand INSTANCE = new LogoutCommand();
    }
}
