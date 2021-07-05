package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.service.BookService;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;

public class AddLikeCommand implements ActionCommand {
    private final BookService bookService = ServiceFactory.getInstance().getBookService();

    private AddLikeCommand() {
    }

    public static AddLikeCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final Long bookId = Long.valueOf(request.getParameter("id"));
        final Book foundBook = bookService.findById(bookId);
        bookService.addLike(foundBook);
        request.getSession().setAttribute("success", "likeAdded");
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
        private static final AddLikeCommand INSTANCE = new AddLikeCommand();
    }
}
