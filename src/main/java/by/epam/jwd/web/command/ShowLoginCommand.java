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
    public String execute(HttpServletRequest request) {
        return LOGIN_JSP_PATH;
    }

    private static class Singleton {
        private static final ShowLoginCommand INSTANCE = new ShowLoginCommand();
    }
}
