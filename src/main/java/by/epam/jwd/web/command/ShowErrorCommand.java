package by.epam.jwd.web.command;

import javax.servlet.http.HttpServletRequest;

public class ShowErrorCommand implements ActionCommand {

    public static final String FORBIDDEN_JSP_PATH = "WEB-INF/jsp/forbidden.jsp";

    private ShowErrorCommand() {
    }

    public static ShowErrorCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        return FORBIDDEN_JSP_PATH;
    }

    private static class Singleton {
        private static final ShowErrorCommand INSTANCE = new ShowErrorCommand();
    }
}
