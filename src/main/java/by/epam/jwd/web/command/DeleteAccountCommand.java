package by.epam.jwd.web.command;

import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.ServiceFactory;
import by.epam.jwd.web.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class DeleteAccountCommand implements ActionCommand {
    private UserService userService = ServiceFactory.getInstance().getUserService();

    private static final String SESSION_USER_ATTRIBUTE_KEY = "user";
    private static final String REQUEST_ACCOUNT_DELETED_ATTRIBUTE_KEY = "deleted";
    private static final String ACCOUNT_DELETED_LOCALIZATION_MESSAGE_KEY = "accountDeleted";

    private static final String RESULT_PATH = "WEB-INF/jsp/account.jsp";

    private DeleteAccountCommand() {
    }

    public static DeleteAccountCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final HttpSession session = request.getSession();
        final User user = (User) session.getAttribute(SESSION_USER_ATTRIBUTE_KEY);
        userService.delete(user.getId());
        session.invalidate();
        request.setAttribute(REQUEST_ACCOUNT_DELETED_ATTRIBUTE_KEY, ACCOUNT_DELETED_LOCALIZATION_MESSAGE_KEY);
        return new CommandResult() {
            @Override
            public String getResultPath() {
                return RESULT_PATH;
            }

            @Override
            public boolean isRedirect() {
                return false;
            }
        };
    }

    private static class Singleton {
        private static final DeleteAccountCommand INSTANCE = new DeleteAccountCommand();
    }
}
