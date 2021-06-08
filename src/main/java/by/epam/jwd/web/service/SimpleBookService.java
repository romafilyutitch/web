package by.epam.jwd.web.service;

import by.epam.jwd.web.dao.BookDao;
import by.epam.jwd.web.dao.DaoFactory;
import by.epam.jwd.web.model.Book;

import java.util.List;

public class SimpleBookService {
    private static final BookDao BOOK_DAO = DaoFactory.getInstance().getBookDao();

    private SimpleBookService() {}

    public static SimpleBookService getInstance() {
        return Singleton.INSTANCE;
    }

    public List<Book> findAll() {
        return BOOK_DAO.findAll();
    }

    private static class Singleton {
        private static final SimpleBookService INSTANCE = new SimpleBookService();
    }
}
