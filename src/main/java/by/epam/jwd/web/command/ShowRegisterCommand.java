package by.epam.jwd.web.command;

import javax.servlet.http.HttpServletRequest;

public class ShowRegisterCommand implements ActionCommand {

    public static final String REGISTER_JSP_PATH = "WEB-INF/jsp/register.jsp";

    private ShowRegisterCommand() {
    }

    public static ShowRegisterCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        return new CommandResult() {
            @Override
            public String getResultPath() {
                return "WEB-INF/jsp/register.jsp";
            }

            @Override
            public boolean isRedirect() {
                return false;
            }
        };
    }

    private static class Singleton {
        private static final ShowRegisterCommand INSTANCE = new ShowRegisterCommand();
    }
}
