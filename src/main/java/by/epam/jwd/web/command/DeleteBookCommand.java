package by.epam.jwd.web.command;

import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class DeleteBookCommand implements ActionCommand {

    public static final String ID = "id";
    public static final String COMMAND_RESULT = "commandResult";
    public static final String RESULT_MESSAGE = "book %s was deleted";


    private DeleteBookCommand() {
    }

    public static DeleteBookCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        final Long id = Long.valueOf(request.getParameter(ID));
        final Book book = ServiceFactory.getInstance().getBookService().findById(id);
        ServiceFactory.getInstance().getBookService().deleteBook(id);
        request.getSession().setAttribute(COMMAND_RESULT, String.format(RESULT_MESSAGE, book.getName()));
        return null;
    }

    private static class Singleton {
        private static final DeleteBookCommand INSTANCE = new DeleteBookCommand();
    }
}
