package by.epam.jwd.web.command;

import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class PromoteRoleCommand implements ActionCommand {

    public static final String ID = "id";
    public static final String COMMAND_RESULT = "commandResult";
    public static final String RESULT_MESSAGE = "role for user was promoted";

    private PromoteRoleCommand() {
    }

    public static PromoteRoleCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        final Long id = Long.valueOf(request.getParameter(ID));
        ServiceFactory.getInstance().getUserService().promoteUserRole(id);
        request.getSession().setAttribute(COMMAND_RESULT, RESULT_MESSAGE);
        return null;
    }

    private static class Singleton {
        private static final PromoteRoleCommand INSTANCE = new PromoteRoleCommand();
    }
}
