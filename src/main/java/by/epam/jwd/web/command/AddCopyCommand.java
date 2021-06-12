package by.epam.jwd.web.command;

import by.epam.jwd.web.service.BookService;
import by.epam.jwd.web.service.ServiceException;
import by.epam.jwd.web.service.SimpleBookService;

import javax.servlet.http.HttpServletRequest;

public class AddCopyCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request) {
        final String bookId = request.getParameter("id");
        final long parsedId = Long.parseLong(bookId);
        try {
            SimpleBookService.getInstance().addOneCopy(parsedId);
            request.getSession().setAttribute("commandResult", "copy of book was added");
            return null;
        } catch (ServiceException e) {
            request.getSession().setAttribute("commandResult", e.getMessage());
            return null;
        }
    }
}
