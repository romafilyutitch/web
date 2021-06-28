package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class DeleteBookCommand implements ActionCommand {


    private static final String REQUEST_BOOK_ID_PARAMETER_KEY = "id";

    private static final String SESSION_SUCCESS_ATTRIBUTE_KEY = "success";
    private static final String SUCCESS_MESSAGE = "book %s was deleted";

    private static final String RESULT_PATH = "index.jsp";

    private DeleteBookCommand() {
    }

    public static DeleteBookCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final Long id = Long.valueOf(request.getParameter(REQUEST_BOOK_ID_PARAMETER_KEY));
        final Book book = ServiceFactory.getInstance().getBookService().findById(id);
        ServiceFactory.getInstance().getBookService().delete(id);
        request.getSession().setAttribute(SESSION_SUCCESS_ATTRIBUTE_KEY, String.format(SUCCESS_MESSAGE, book.getName()));
        return new CommandResult() {
            @Override
            public String getResultPath() {
                return RESULT_PATH;
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
