package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.service.ServiceException;
import by.epam.jwd.web.service.SimpleBookService;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

public class FindByNameCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request) {
        final String name = request.getParameter("name");
        try {
            final Book book = SimpleBookService.getInstance().findByName(name);
            request.setAttribute("books", Collections.singletonList(book));
            return "WEB-INF/jsp/main.jsp";
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            return "WEB-INF/jsp/main.jsp";
        }
    }
}
