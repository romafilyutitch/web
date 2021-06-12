package by.epam.jwd.web.command;

import by.epam.jwd.web.model.User;

import javax.servlet.http.HttpServletRequest;

public class ShowAccountCommand implements ActionCommand {

    @Override
    public String execute(HttpServletRequest request) {
        return "WEB-INF/jsp/account.jsp";
    }
}
