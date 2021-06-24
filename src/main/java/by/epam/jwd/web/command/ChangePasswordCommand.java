package by.epam.jwd.web.command;

import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class ChangePasswordCommand implements ActionCommand {

    private ChangePasswordCommand() {}

    public static ChangePasswordCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final String newPassword = request.getParameter("password");
        final Long userId = Long.valueOf(request.getParameter("id"));
        final User userWithChangedPassword = ServiceFactory.getInstance().getUserService().changePassword(userId, newPassword);
        final HttpSession session = request.getSession();
        session.setAttribute("success", "Password was changed");
        session.setAttribute("user", userWithChangedPassword);
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
    }

    private static class Singleton {
        private static final ChangePasswordCommand INSTANCE = new ChangePasswordCommand();
    }
}
