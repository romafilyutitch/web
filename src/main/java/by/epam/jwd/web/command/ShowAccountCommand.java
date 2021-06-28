package by.epam.jwd.web.command;

import javax.servlet.http.HttpServletRequest;

public class ShowAccountCommand implements ActionCommand {

    private static final String RESULT_PATH = "WEB-INF/jsp/account.jsp";

    private ShowAccountCommand() {
    }

    public static ShowAccountCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        return new CommandResult() {
            @Override
            public String getResultPath() {
                return RESULT_PATH;
            }

            @Override
            public boolean isRedirect() {
                return false;
            }
        };
    }

    private static class Singleton {
        private static final ShowAccountCommand INSTANCE = new ShowAccountCommand();
    }
}
