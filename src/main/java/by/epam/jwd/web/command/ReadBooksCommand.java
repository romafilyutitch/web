package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ReadBooksCommand implements ActionCommand {

    private ReadBooksCommand() {}

    public static ReadBooksCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final int currentPageNumber = Integer.parseInt(request.getParameter("page"));
        final List<Book> currentPage = ServiceFactory.getInstance().getBookService().findPage(currentPageNumber);
        final int pagesAmount = ServiceFactory.getInstance().getBookService().getPagesAmount();
        request.setAttribute("books", currentPage);
        request.setAttribute("currentPage", currentPageNumber);
        request.setAttribute("pagesAmount", pagesAmount);
        return new CommandResult() {
            @Override
            public String getResultPath() {
                return "WEB-INF/jsp/main.jsp";
            }

            @Override
            public boolean isRedirect() {
                return false;
            }
        };
    }

    private static class Singleton {
        private static final ReadBooksCommand INSTANCE = new ReadBooksCommand();
    }
}
