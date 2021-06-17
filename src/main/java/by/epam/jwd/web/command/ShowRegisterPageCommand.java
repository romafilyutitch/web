package by.epam.jwd.web.command;

import javax.servlet.http.HttpServletRequest;

public class ShowRegisterPageCommand implements ActionCommand{

    private ShowRegisterPageCommand() {}

    public static ShowRegisterPageCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        return "WEB-INF/jsp/register.jsp";
    }

    private static class Singleton {
        private static ShowRegisterPageCommand INSTANCE = new ShowRegisterPageCommand();
    }
}
