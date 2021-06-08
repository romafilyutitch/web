package by.epam.jwd.web.command;

import javax.servlet.http.HttpServletRequest;

public class ShowLoginCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request) {
        return "WEB-INF/jsp/login.jsp";
    }
}
