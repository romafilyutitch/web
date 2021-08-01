package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Comment;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.service.BookService;
import by.epam.jwd.web.service.CommentService;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FindBookByNameCommand implements ActionCommand {
    private final BookService bookService = ServiceFactory.getInstance().getBookService();
    private final CommentService commentService = ServiceFactory.getInstance().getCommentService();

    private static final String REQUEST_BOOK_NAME_PARAMETER_KEY = "name";
    private static final String REQUEST_BOOKS_ATTRIBUTE_KEY = "books";
    private static final String REQUEST_COMMENTS_ATTRIBUTE_KEY = "comments";
    private static final String REQUEST_MESSAGE_ATTRIBUTE_KEY = "message";
    private static final String BOOK_WAS_FOUND_BY_NAME_MESSAGE_KEY = "book.find.name.found";
    private static final String BOOK_WAS_NOT_FOUND_BY_NAME_MESSAGE_KEY = "book.find.name.notFound";

    private static final String RESULT_PATH = "WEB-INF/jsp/main.jsp";


    private FindBookByNameCommand() {
    }

    public static FindBookByNameCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        final String bookName = request.getParameter(REQUEST_BOOK_NAME_PARAMETER_KEY).trim();
        final Optional<Book> optionalBook = bookService.findByName(bookName);
        if (optionalBook.isPresent()) {
            final List<Book> foundBook = Collections.singletonList(optionalBook.get());
            final List<Comment> bookComments = findComments(foundBook);
            request.setAttribute(REQUEST_BOOKS_ATTRIBUTE_KEY, foundBook);
            request.setAttribute(REQUEST_COMMENTS_ATTRIBUTE_KEY, bookComments);
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(BOOK_WAS_FOUND_BY_NAME_MESSAGE_KEY));
        } else {
            request.setAttribute(REQUEST_BOOKS_ATTRIBUTE_KEY, Collections.emptyList());
            request.setAttribute(REQUEST_MESSAGE_ATTRIBUTE_KEY, MessageManager.getMessage(BOOK_WAS_NOT_FOUND_BY_NAME_MESSAGE_KEY));
        }
        return RESULT_PATH;
    }

    private List<Comment> findComments(List<Book> books) {
        final List<Comment> comments = new ArrayList<>();
        for (Book book : books) {
            comments.addAll(commentService.findByBook(book));
        }
        return comments;
    }

    private static class Singleton {
        private static final FindBookByNameCommand INSTANCE = new FindBookByNameCommand();
    }
}
