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
    public String execute(HttpServletRequest request) {
        final List<Book> scienceBooks = ServiceFactory.getInstance().getBookService().findByGenre(Genre.SCIENCE);
        request.setAttribute("books", scienceBooks);
        return "WEB-INF/jsp/main.jsp";
    }

    private static class Singleton {
        private static final FindScienceCommand INSTANCE = new FindScienceCommand();
    }
}
