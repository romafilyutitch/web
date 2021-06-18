package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class FindFantasyCommand implements ActionCommand {

    private FindFantasyCommand() {}

    public static FindFantasyCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final List<Book> fantasyBooks = ServiceFactory.getInstance().getBookService().findByGenre(Genre.FANTASY);
        request.setAttribute("books", fantasyBooks);
        request.setAttribute("findResult", String.format("%d books was found", fantasyBooks.size()));
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
        private static final FindFantasyCommand INSTANCE = new FindFantasyCommand();
    }
}
