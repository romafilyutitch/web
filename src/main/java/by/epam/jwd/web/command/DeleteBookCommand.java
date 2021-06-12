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
            request.getSession().setAttribute("commandResult", "book was deleted");
            return null;
        } catch (ServiceException e) {
            request.getSession().setAttribute("commandResult", e.getMessage());
            return null;
        }
    }
}
