package by.epam.jwd.web.command;

import javax.servlet.http.HttpServletRequest;

public class EmptyCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request) {
        /*
        В случае ошибки или прямого обращения к контроллеру
        переадресация нс страницу ввода логина
         */
        return "WEB-INF/jsp/login.jsp";
    }
}
