package by.epam.jwd.web.command.navigation;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.resource.PathManager;
import by.epam.jwd.web.service.UserService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Executes command that is find users page and form users page path to forward.
 *
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class ShowUsersListCommand implements ActionCommand {
    private final UserService userService = UserService.getInstance();

    private static final String REQUEST_PAGE_PARAMETER_KEY = "page";
    private static final String REQUEST_USERS_ATTRIBUTE_KEY = "users";
    private static final String REQUEST_CURRENT_PAGE_NUMBER_ATTRIBUTE_KEY = "currentPageNumber";
    private static final String REQUEST_PAGES_AMOUNT_ATTRIBUTE_KEY = "pagesAmount";

    private ShowUsersListCommand() {
    }

    /**
     * Gets single class instance from nested class.
     *
     * @return class instance.
     */
    public static ShowUsersListCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Forms users page if page number passed from request or first user page
     * otherwise and forms users page path to forward.
     *
     * @param request request that need to be execute.
     * @return users page path for forward.
     */
    @Override
    public String execute(HttpServletRequest request) {
        int currentPageNumber = 1;
        final String pageParameter = request.getParameter(REQUEST_PAGE_PARAMETER_KEY);
        if (pageParameter != null) {
            currentPageNumber = Integer.parseInt(pageParameter);
        }

        final List<User> currentPage = userService.findPage(currentPageNumber);
        final int pagesAmount = userService.getPagesAmount();
        request.setAttribute(REQUEST_USERS_ATTRIBUTE_KEY, currentPage);
        request.setAttribute(REQUEST_CURRENT_PAGE_NUMBER_ATTRIBUTE_KEY, currentPageNumber);
        request.setAttribute(REQUEST_PAGES_AMOUNT_ATTRIBUTE_KEY, pagesAmount);
        return PathManager.getUsersPagePath();
    }

    /**
     * Nested class that encapsulates single {@link ShowUsersListCommand} instance.
     * Singleton pattern variation.
     *
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final ShowUsersListCommand INSTANCE = new ShowUsersListCommand();
    }
}
