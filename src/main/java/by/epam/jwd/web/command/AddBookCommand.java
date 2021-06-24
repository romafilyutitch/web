package by.epam.jwd.web.command;

import by.epam.jwd.web.exception.RegisterException;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Author;
import by.epam.jwd.web.model.Genre;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

public class AddBookCommand implements ActionCommand {

    public static final String BOOK_NAME = "bookName";
    public static final String AUTHOR_NAME = "authorName";
    public static final String GENRE_NAME = "genreName";
    public static final String DATE = "date";
    public static final String PAGES = "pages";
    public static final String DESCRIPTION = "description";
    public static final String COMMAND_RESULT = "commandResult";
    public static final String RESULT_MESSAGE = "book %s was added";

    private AddBookCommand() {
    }

    public static AddBookCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final String name = request.getParameter("name");
        final String author = request.getParameter("author");
        final Genre genre = Genre.valueOf(request.getParameter("genre"));
        final LocalDate date = LocalDate.parse(request.getParameter("date"));
        final int pages = Integer.parseInt(request.getParameter("pages"));
        final String description = request.getParameter("description");
        final Book book = new Book(name, new Author(author), genre, date, pages, description);
        try {
            ServiceFactory.getInstance().getBookService().register(book);
            request.getSession().setAttribute("success", String.format("book %s was added", name));
        } catch (RegisterException e) {
            request.getSession().setAttribute("fail", e.getMessage());
        }
        return new CommandResult() {
            @Override
            public String getResultPath() {
                return "index.jsp";
            }

            @Override
            public boolean isRedirect() {
                return true;
            }
        };
    }


    private static class Singleton {
        private static final AddBookCommand INSTANCE = new AddBookCommand();
    }


}
