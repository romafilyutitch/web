package by.epam.jwd.web.command;

import javax.servlet.http.HttpServletRequest;

public class ShowLoginPageCommand implements ActionCommand {

    private ShowLoginPageCommand() {

    }

    public static ShowLoginPageCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        return "WEB-INF/jsp/login.jsp";
    }

    private static class Singleton {
        private static final ShowLoginPageCommand INSTANCE = new ShowLoginPageCommand();
    }
}
