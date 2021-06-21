package by.epam.jwd.web.command;

import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ShowUsersListCommand implements ActionCommand {

    public static final String USERS = "users";
    public static final String USERS_JSP_PATH = "WEB-INF/jsp/users.jsp";

    private ShowUsersListCommand() {
    }

    public static ShowUsersListCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        int currentPageNumber = 1;
        final String pageParameter = request.getParameter("page");
        if (pageParameter != null) {
            currentPageNumber = Integer.parseInt(pageParameter);
        }

        final List<User> currentPage = ServiceFactory.getInstance().getUserService().findPage(currentPageNumber);
        final int pagesAmount = ServiceFactory.getInstance().getUserService().getPagesAmount();
        request.setAttribute("users", currentPage);
        request.setAttribute("currentPageNumber", currentPageNumber);
        request.setAttribute("pagesAmount", pagesAmount);
        return new CommandResult() {
            @Override
            public String getResultPath() {
                return "WEB-INF/jsp/users.jsp";
            }

            @Override
            public boolean isRedirect() {
                return false;
            }
        };
    }

    private static class Singleton {
        private static final ShowUsersListCommand INSTANCE = new ShowUsersListCommand();
    }
}
