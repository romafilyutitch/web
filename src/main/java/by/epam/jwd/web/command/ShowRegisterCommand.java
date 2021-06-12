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
    public String execute(HttpServletRequest request) {
        return REGISTER_JSP_PATH;
    }

    private static class Singleton {
        private static final ShowRegisterCommand INSTANCE = new ShowRegisterCommand();
    }
}
