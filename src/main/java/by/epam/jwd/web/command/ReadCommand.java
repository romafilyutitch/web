package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.service.ServiceException;
import by.epam.jwd.web.service.SimpleBookService;

import javax.servlet.http.HttpServletRequest;

public class ReadCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request) {
        final long id = Long.parseLong(request.getParameter("id"));
        try {
            final Book bookToRead = SimpleBookService.getInstance().removeOneCopy(id);
            request.setAttribute("book", bookToRead);
            return "WEB-INF/jsp/read.jsp";
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            return "controller?command=main";
        }
    }
}
