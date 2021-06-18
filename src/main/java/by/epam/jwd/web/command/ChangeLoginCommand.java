package by.epam.jwd.web.command;

import by.epam.jwd.web.exception.LoginException;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.ServiceFactory;
import by.epam.jwd.web.validator.UserValidator;

import javax.servlet.http.HttpServletRequest;

public class ChangeLoginCommand implements ActionCommand {

    private ChangeLoginCommand() {}

    public static ChangeLoginCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final String login = request.getParameter("login");
        User user = (User) request.getSession().getAttribute("user");
        try {
            ServiceFactory.getInstance().getUserService().changeLogin(user.getId(), login);
            request.getSession().setAttribute("success", "Login was changed");
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
        } catch (LoginException e) {
            request.setAttribute("error", e.getMessage());
            return new CommandResult() {
                @Override
                public String getResultPath() {
                    return "WEB-INF/jsp/account.jsp";
                }

                @Override
                public boolean isRedirect() {
                    return false;
                }
            };
        }
    }

    private static class Singleton {
        private static final ChangeLoginCommand INSTANCE = new ChangeLoginCommand();
    }
}
