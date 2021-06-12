package by.epam.jwd.web.command;

import javax.servlet.http.HttpServletRequest;

public class ShowAddBookPageCommand implements ActionCommand {

    public static final String ADD_BOOK_JSP_PATH = "WEB-INF/jsp/add_book.jsp";

    private ShowAddBookPageCommand() {
    }

    public static ShowAddBookPageCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        return ADD_BOOK_JSP_PATH;
    }

    private static class Singleton {
        private static final ShowAddBookPageCommand INSTANCE = new ShowAddBookPageCommand();
    }
}
