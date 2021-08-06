package by.epam.jwd.web.command.action.user;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.resource.CommandManager;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.service.api.ServiceFactory;
import by.epam.jwd.web.service.api.UserService;

import javax.servlet.http.HttpServletRequest;

/**
 * Executes command that is Demote saved user role.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class DemoteRoleCommand implements ActionCommand {
    private final UserService userService = ServiceFactory.getInstance().getUserService();

    private static final String REQUEST_USER_ID_PARAMETER_KEY = "id";
    private static final String REQUEST_MESSAGE_ATTRIBUTE_KEY = "message";
    private static final String USER_ROLE_WAS_DEMOTED_MESSAGE_KEY = "user.role.demoted";

    private DemoteRoleCommand() {
    }

    /**
     * Gets single class instance from nested class.
     * @return class instance.
     */
    public static DemoteRoleCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Demotes user role.
     * Request must contain user whose role need to demote.
     * @param request request that need to be execute.
     * @return show users command for forward.
     */
    @Override
    public String execute(HttpServletRequest request) {
        final Long id = Long.valueOf(request.getParameter(REQUEST_USER_ID_PARAMETER_KEY));
        final User foundUser = userService.findById(id);
        userService.demoteRole(foundUser);
        request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(USER_ROLE_WAS_DEMOTED_MESSAGE_KEY));
        return CommandManager.getShowUsersCommand();
    }

    /**
     * Nested class that encapsulates single {@link DemoteRoleCommand} instance.
     * Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final DemoteRoleCommand INSTANCE = new DemoteRoleCommand();
    }
}
