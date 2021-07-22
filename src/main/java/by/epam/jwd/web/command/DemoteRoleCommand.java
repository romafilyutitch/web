package by.epam.jwd.web.command;

import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.ServiceFactory;
import by.epam.jwd.web.service.UserService;

import javax.servlet.http.HttpServletRequest;

public class DemoteRoleCommand implements ActionCommand {
    private final UserService userService = ServiceFactory.getInstance().getUserService();

    private static final String REQUEST_USER_ID_PARAMETER_KEY = "id";
    private static final String SESSION_SUCCESS_ATTRIBUTE_KEY = "success";
    private static final String ROLE_DEMOTED_LOCALIZATION_MESSAGE_KEY = "roleDemoted";

    private static final String RESULT_PATH = "index.jsp";

    private DemoteRoleCommand() {
    }

    public static DemoteRoleCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final Long id = Long.valueOf(request.getParameter(REQUEST_USER_ID_PARAMETER_KEY));
        final User foundUser = userService.findById(id);
        userService.demoteRole(foundUser);
        request.getSession().setAttribute(SESSION_SUCCESS_ATTRIBUTE_KEY, ROLE_DEMOTED_LOCALIZATION_MESSAGE_KEY);
        return new CommandResult() {
            @Override
            public String getResultPath() {
                return RESULT_PATH;
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
