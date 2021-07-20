package by.epam.jwd.web.command;

import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.ServiceFactory;
import by.epam.jwd.web.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class DeleteUserCommand implements ActionCommand {
    private UserService userService = ServiceFactory.getInstance().getUserService();

    private static final String SESSION_USER_ATTRIBUTE_KEY = "user";

    private static final String RESULT_PATH = "index.jsp";

    private DeleteUserCommand() {
    }

    public static DeleteUserCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final HttpSession session = request.getSession();
        final User user = (User) session.getAttribute(SESSION_USER_ATTRIBUTE_KEY);
        userService.delete(user.getId());
        request.getSession().invalidate();
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
        private static final DeleteUserCommand INSTANCE = new DeleteUserCommand();
    }
}
