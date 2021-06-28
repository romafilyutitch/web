package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class RemoveCopyCommand implements ActionCommand {

    private static final String REQUEST_BOOK_ID_PARAMETER_KEY = "id";

    private static final String SESSION_SUCCESS_ATTRIBUTE_KEY = "success";
    private static final String SUCCESS_MESSAGE = "copy of book %s was removed";

    private static final String RESULT_PATH = "index.jsp";

    private RemoveCopyCommand() {
    }

    public static RemoveCopyCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final Long id = Long.valueOf(request.getParameter(REQUEST_BOOK_ID_PARAMETER_KEY));
        final Book book = ServiceFactory.getInstance().getBookService().removeOneCopy(id);
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
        private static final RemoveCopyCommand INSTANCE = new RemoveCopyCommand();
    }
}
