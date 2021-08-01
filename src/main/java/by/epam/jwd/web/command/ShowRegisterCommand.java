package by.epam.jwd.web.command;

import javax.servlet.http.HttpServletRequest;

public class ShowRegisterCommand implements ActionCommand {

    private static final String RESULT_PATH = "WEB-INF/jsp/register.jsp";

    private ShowRegisterCommand() {
    }

    public static ShowRegisterCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        return RESULT_PATH;
    }

    private static class Singleton {
        private static final ShowRegisterCommand INSTANCE = new ShowRegisterCommand();
    }
}
