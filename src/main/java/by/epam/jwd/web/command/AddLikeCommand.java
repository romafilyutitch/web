package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Like;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.BookService;
import by.epam.jwd.web.service.LikeService;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

public class AddLikeCommand implements ActionCommand {
    private final LikeService likeService = ServiceFactory.getInstance().getLikeService();
    private final BookService bookService = ServiceFactory.getInstance().getBookService();

    private static final String REQUEST_BOOK_ID_PARAMETER_KEY = "id";
    private static final String SESSION_USER_ATTRIBUTE_KEY = "user";
    private static final String SESSION_SUCCESS_ATTRIBUTE_KEY = "success";
    private static final String LIKE_ADDED_LOCALIZATION_MESSAGE_KEY = "likeAdded";
    private static final String LIKE_REMOVED_LOCALIZATION_MESSAGE_KEY = "likeRemoved";

    private static final String RESULT_PATH = "index.jsp";

    private AddLikeCommand() {
    }

    public static AddLikeCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final HttpSession session = request.getSession();
        final User user = (User) session.getAttribute(SESSION_USER_ATTRIBUTE_KEY);
        final Long bookId = Long.valueOf(request.getParameter(REQUEST_BOOK_ID_PARAMETER_KEY));
        final Book foundBook = bookService.findById(bookId);
        final Optional<Like> optionalLike = likeService.findByUserAndBook(user, foundBook);
        if (optionalLike.isPresent()) {
            final Like foundLike = optionalLike.get();
            likeService.delete(foundLike.getId());
            session.setAttribute(SESSION_SUCCESS_ATTRIBUTE_KEY, LIKE_REMOVED_LOCALIZATION_MESSAGE_KEY);
        } else {
            final Like like = new Like(user, foundBook);
            likeService.save(like);
            session.setAttribute(SESSION_SUCCESS_ATTRIBUTE_KEY, LIKE_ADDED_LOCALIZATION_MESSAGE_KEY);
        }
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
        private static final AddLikeCommand INSTANCE = new AddLikeCommand();
    }
}
