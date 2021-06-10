package by.epam.jwd.web.command;

import by.epam.jwd.web.service.ServiceException;
import by.epam.jwd.web.service.SimpleBookService;

import javax.servlet.http.HttpServletRequest;

public class RemoveCopyCommand implements ActionCommand {

    @Override
    public String execute(HttpServletRequest request) {
        final String bookId = request.getParameter("id");
        final long parsedId = Long.parseLong(bookId);
        try {
            SimpleBookService.getInstance().removeOneCopy(parsedId);
            return "controller?command=show_books";
        } catch (ServiceException e) {
            request.setAttribute("removeError", e.getMessage());
            return "controller?command=show_books";
        }
    }
}
