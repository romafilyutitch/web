package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class FindFictionCommand implements ActionCommand {

    private FindFictionCommand() {}

    public static FindFictionCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        final List<Book> fictionBooks = ServiceFactory.getInstance().getBookService().findByGenre(Genre.FICTION);
        request.setAttribute("books", fictionBooks);
        return "WEB-INF/jsp/main.jsp";
    }

    private static class Singleton {
        private static final FindFictionCommand INSTANCE = new FindFictionCommand();
    }
}
