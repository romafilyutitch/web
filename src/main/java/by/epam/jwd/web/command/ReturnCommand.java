package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.service.ServiceException;
import by.epam.jwd.web.service.SimpleBookService;

import javax.servlet.http.HttpServletRequest;

public class ReturnCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request) {
        final long id = Long.parseLong(request.getParameter("id"));
        try {
            final Book book = SimpleBookService.getInstance().addOneCopy(id);
            return "controller?command=main";
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            return "controller?command=read&id=" + id;
        }
    }
}
