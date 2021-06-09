package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.service.SimpleBookService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class ShowBooksListCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request) {
        final List<Book> allBooks = SimpleBookService.getInstance().findAll();
        request.setAttribute("books", allBooks);
        return "WEB-INF/jsp/books.jsp";
    }
}
