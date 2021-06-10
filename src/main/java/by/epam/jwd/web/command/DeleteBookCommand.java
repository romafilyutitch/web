package by.epam.jwd.web.command;

import by.epam.jwd.web.service.ServiceException;
import by.epam.jwd.web.service.SimpleBookService;

import javax.servlet.http.HttpServletRequest;

public class DeleteBookCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request) {
        final long parsedId = Long.parseLong(request.getParameter("id"));
        try {
            SimpleBookService.getInstance().deleteBook(parsedId);
            return "controller?command=show_books";
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            return "controller?command=show_books";
        }
    }
}
