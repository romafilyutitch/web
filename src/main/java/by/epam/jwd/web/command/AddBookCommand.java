package by.epam.jwd.web.command;

import by.epam.jwd.web.service.ServiceException;
import by.epam.jwd.web.service.SimpleBookService;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

public class AddBookCommand implements ActionCommand {

    @Override
    public String execute(HttpServletRequest request) {
        final String name = request.getParameter("bookName");
        final String author = request.getParameter("authorName");
        final String genre = request.getParameter("genreName");
        final String date = request.getParameter("date");
        final String pages = request.getParameter("pages");
        final String description = request.getParameter("description");
        try {
            SimpleBookService.getInstance().createBook(name, author, genre, date, pages, description);
            return "controller?command=show_books";
        } catch (ServiceException e) {
            request.setAttribute("error", e.getMessage());
            return "controller?command=show_add_book_page";
        }
    }
}
