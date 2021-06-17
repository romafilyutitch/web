package by.epam.jwd.web.command;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.service.ServiceFactory;

import javax.servlet.http.HttpServletRequest;
import java.security.Provider;
import java.util.Collections;
import java.util.Optional;

public class FindBookByNameCommand implements ActionCommand {
    private FindBookByNameCommand() {}

    public static FindBookByNameCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        final String bookName = request.getParameter("name");
        final Optional<Book> optionalBook = ServiceFactory.getInstance().getBookService().findByName(bookName);
        if (optionalBook.isPresent()) {
            request.setAttribute("books", Collections.singletonList(optionalBook.get()));
        } else {
            request.setAttribute("books", Collections.emptyList());
        }
        return "WEB-INF/jsp/main.jsp";
    }

    private static class Singleton {
        private static final FindBookByNameCommand INSTANCE = new FindBookByNameCommand();
    }
}
