package by.epam.jwd.web.command;

import javax.servlet.http.HttpServletRequest;

public class ShowLoginCommand implements ActionCommand {

    public static final String LOGIN_JSP_PATH = "WEB-INF/jsp/login.jsp";

    private ShowLoginCommand() {
    }

    public static ShowLoginCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        return new CommandResult() {
            @Override
            public String getResultPath() {
                return "WEB-INF/jsp/login.jsp";
            }

            @Override
            public boolean isRedirect() {
                return false;
            }
        };
    }

    private static class Singleton {
        private static final ShowLoginCommand INSTANCE = new ShowLoginCommand();
    }
}
