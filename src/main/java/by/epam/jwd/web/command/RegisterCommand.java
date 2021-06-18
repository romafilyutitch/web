package by.epam.jwd.web.command;

import by.epam.jwd.web.exception.RegisterException;
import by.epam.jwd.web.exception.ValidationException;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.service.ServiceFactory;
import by.epam.jwd.web.validator.UserValidator;

import javax.servlet.http.HttpServletRequest;

public class RegisterCommand implements ActionCommand {
    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";
    public static final String USER = "user";
    public static final String ERROR = "error";
    public static final String REGISTER_JSP_PATH = "WEB-INF/jsp/register.jsp";


    private RegisterCommand() {
    }

    public static RegisterCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final String login = request.getParameter("login");
        final String password = request.getParameter("password");
        final User user = new User(login, password);
        try {
            UserValidator.getInstance().validate(user);
            final User registeredUser = ServiceFactory.getInstance().getUserService().registerUser(user);
            request.getSession().setAttribute("user", registeredUser);
            return new CommandResult() {
                @Override
                public String getResultPath() {
                    return "index.jsp";
                }

                @Override
                public boolean isRedirect() {
                    return true;
                }
            };
        } catch (ValidationException | RegisterException e) {
            request.setAttribute(ERROR, e.getMessage());
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
    }

    private static class Singleton {
        private static final RegisterCommand INSTANCE = new RegisterCommand();
    }
}
