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
    public String execute(HttpServletRequest request) {
        final List<Book> fantasyBooks = ServiceFactory.getInstance().getBookService().findByGenre(Genre.FANTASY);
        request.setAttribute("books", fantasyBooks);
        return "WEB-INF/jsp/main.jsp";
    }

    private static class Singleton {
        private static final FindFantasyCommand INSTANCE = new FindFantasyCommand();
    }
}
