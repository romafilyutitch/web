package by.epam.jwd.web.command;

import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class DemoteRoleCommand implements ActionCommand {

    public static final String ID = "id";
    public static final String COMMAND_RESULT = "commandResult";
    public static final String RESULT_MESSAGE = "role for user was demoted";

    private DemoteRoleCommand() {
    }

    public static DemoteRoleCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final Long id = Long.valueOf(request.getParameter("id"));
        final User user = ServiceFactory.getInstance().getUserService().demoteUserRole(id);
        request.getSession().setAttribute("success", String.format("role for user %s was demoted", user.getLogin()));
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
        private static final DemoteRoleCommand INSTANCE = new DemoteRoleCommand();
    }
}
