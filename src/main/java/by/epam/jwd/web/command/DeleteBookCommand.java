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
    public CommandResult execute(HttpServletRequest request) {
        final Long id = Long.valueOf(request.getParameter("id"));
        final Book book = ServiceFactory.getInstance().getBookService().findById(id);
        ServiceFactory.getInstance().getBookService().delete(id);
        request.getSession().setAttribute("success", String.format("book %s was deleted", book.getName()));
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
        private static final DeleteBookCommand INSTANCE = new DeleteBookCommand();
    }
}
