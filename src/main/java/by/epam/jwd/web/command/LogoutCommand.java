package by.epam.jwd.web.command;

import javax.servlet.http.HttpServletRequest;

public class LogoutCommand implements ActionCommand {

    private LogoutCommand() {}

    public static LogoutCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        request.getSession().invalidate();
        return new CommandResult() {
            @Override
            public String getResultPath() {
                return "index.jsp";
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
