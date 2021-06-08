package by.epam.jwd.web.command;

import javax.servlet.http.HttpServletRequest;

public class ShowRegisterCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request) {
        return "WEB-INF/jsp/register.jsp";
    }
}
