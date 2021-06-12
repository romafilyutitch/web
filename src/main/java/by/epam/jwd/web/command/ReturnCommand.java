package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.service.ServiceException;
import by.epam.jwd.web.service.SimpleBookService;

import javax.servlet.http.HttpServletRequest;

public class ReturnCommand implements ActionCommand {
    @Override
    public String execute(HttpServletRequest request) {
        return "controller?command=main";
    }
}
