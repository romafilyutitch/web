package by.epam.jwd.web.command;

import by.epam.jwd.web.model.User;
import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class ChangeAccountCommand implements ActionCommand {

    public static final String USER = "user";
    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";
    public static final String COMMAND_RESULT = "commandResult";
    public static final String RESULT_MESSAGE = "user data was changed";


    private ChangeAccountCommand() {
    }

    public static ChangeAccountCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
       return null;
    }

    private static class Singleton {
        private static final ChangeAccountCommand INSTANCE = new ChangeAccountCommand();
    }
}
