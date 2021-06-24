package by.epam.jwd.web.command;

import by.epam.jwd.web.exception.LoginException;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ChangeLoginCommand implements ActionCommand {

    private ChangeLoginCommand() {}

    public static ChangeLoginCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final String login = request.getParameter("login");
        final HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        try {
            final User userWithChangedLogin = ServiceFactory.getInstance().getUserService().changeLogin(user.getId(), login);
            session.setAttribute("success", "Login was changed");
            session.setAttribute("user", userWithChangedLogin);
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
