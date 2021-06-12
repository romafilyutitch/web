package by.epam.jwd.web.command;

import javax.servlet.http.HttpServletRequest;

public class ShowAccountCommand implements ActionCommand {

    public static final String ACCOUNT_JSP_PATH = "WEB-INF/jsp/account.jsp";

    private ShowAccountCommand() {
    }

    public static ShowAccountCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        return ACCOUNT_JSP_PATH;
    }

    private static class Singleton {
        private static final ShowAccountCommand INSTANCE = new ShowAccountCommand();
    }
}
