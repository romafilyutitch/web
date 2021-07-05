package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Comment;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.BookService;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDate;

public class AddCommentCommand implements ActionCommand {
    private final BookService bookService = ServiceFactory.getInstance().getBookService();

    private AddCommentCommand() {};

    public static AddCommentCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final HttpSession session = request.getSession();
        final User user = (User) session.getAttribute("user");
        final Long bookId = Long.valueOf(request.getParameter("bookId"));
        final String text = request.getParameter("text");
        final Book book = bookService.findById(bookId);
        final Comment comment = new Comment(user, book, LocalDate.now(), text);
        bookService.addComment(comment);
        session.setAttribute("success", "commentAdded");
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
        private static final AddCommentCommand INSTANCE = new AddCommentCommand();
    }
}
