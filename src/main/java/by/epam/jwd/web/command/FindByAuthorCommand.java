package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.service.ServiceException;
import by.epam.jwd.web.service.SimpleBookService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public class FindByAuthorCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request) {
        final String authorName = request.getParameter("name");
        try {
            final List<Book> byAuthor = SimpleBookService.getInstance().findByAuthor(authorName);
            request.setAttribute("books", byAuthor);
            return "WEB-INF/jsp/main.jsp";
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            return "WEB-INF/jsp/main.jsp";
        }

    }
}
