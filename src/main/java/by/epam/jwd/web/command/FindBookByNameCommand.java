package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.security.Provider;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class FindBookByNameCommand implements ActionCommand {
    private FindBookByNameCommand() {}

    public static FindBookByNameCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final String bookName = request.getParameter("name");
        final Optional<Book> optionalBook = ServiceFactory.getInstance().getBookService().findByName(bookName);
        if (optionalBook.isPresent()) {
            final List<Book> foundBook = Collections.singletonList(optionalBook.get());
            request.setAttribute("books", foundBook);
            request.setAttribute("findResult", String.format("%d book was found", foundBook.size()));
        } else {
            request.setAttribute("books", Collections.emptyList());
            request.setAttribute("findResult", "No book was found");
        }
        return new CommandResult() {
            @Override
            public String getResultPath() {
                return "WEB-INF/jsp/main.jsp";
            }

            @Override
            public boolean isRedirect() {
                return false;
            }
        };
    }

    private static class Singleton {
        private static final FindBookByNameCommand INSTANCE = new FindBookByNameCommand();
    }
}
