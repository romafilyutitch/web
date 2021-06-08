package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.service.SimpleBookService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class MainCommand implements ActionCommand {
    private static final SimpleBookService BOOK_SERVICE = SimpleBookService.getInstance();

    @Override
    public String execute(HttpServletRequest request) {
        final List<Book> all = BOOK_SERVICE.findAll();
        request.setAttribute("books", all);
        return "WEB-INF/jsp/main.jsp";
    }
}
