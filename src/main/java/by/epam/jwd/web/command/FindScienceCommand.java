package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class FindScienceCommand implements ActionCommand {

    private FindScienceCommand() {}

    public static FindScienceCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final List<Book> scienceBooks = ServiceFactory.getInstance().getBookService().findByGenre(Genre.SCIENCE);
        request.setAttribute("books", scienceBooks);
        request.setAttribute("findResult", String.format("%d books was found", scienceBooks.size()));
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
        private static final FindScienceCommand INSTANCE = new FindScienceCommand();
    }
}
