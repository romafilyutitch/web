package by.epam.jwd.web.service;

import by.epam.jwd.web.dao.BookDao;
import by.epam.jwd.web.dao.DaoFactory;
import by.epam.jwd.web.model.Book;

import java.util.List;
import java.util.Optional;

public class SimpleBookService implements BookService{
    private static final BookDao BOOK_DAO = DaoFactory.getInstance().getBookDao();

    private SimpleBookService() {}

    public static SimpleBookService getInstance() {
        return Singleton.INSTANCE;
    }
    @Override
    public List<Book> findAll() {
        return BOOK_DAO.findAll();
    }

    @Override
    public Book update(Book book) throws ServiceException {
        final Optional<Book> optionalBook = BOOK_DAO.findById(book.getId());
        if (!optionalBook.isPresent()) {
            throw new ServiceException("Book does not exist");
        }
        final Book foundBook = optionalBook.get();
        return BOOK_DAO.update(foundBook);
    }

    @Override
    public Book findById(Long id) throws ServiceException {
        final Optional<Book> optionalBook = BOOK_DAO.findById(id);
        if (!optionalBook.isPresent()) {
            throw new ServiceException(String.format("Book with id %d was not found", id));
        }
        return optionalBook.get();
    }

    @Override
    public Book addOneCopy(Long id) throws ServiceException {
        final Optional<Book> optionalBook = BOOK_DAO.findById(id);
        if (!optionalBook.isPresent()) {
            throw new ServiceException(String.format("Book with id %d does not exist", id));
        }
        final Book savedBook = optionalBook.get();
        int savedBookBooksAmount = savedBook.getBooksAmount();
        System.out.println("ADDIN ONE COPY TO BOOK");
        return BOOK_DAO.update(savedBook.updatedBooksAmount(++savedBookBooksAmount));
    }

    @Override
    public Book removeOneCopy(Long id) throws ServiceException {
        final Optional<Book> optionalBook = BOOK_DAO.findById(id);
        if (!optionalBook.isPresent()) {
            throw new ServiceException(String.format("Book with id %d does not exist", id));
        }
        final Book savedBook = optionalBook.get();
        int savedBooksAmount = savedBook.getBooksAmount();
        System.out.println("REMOVINH ONE COPY FROM BOOK");
        return BOOK_DAO.update(savedBook.updatedBooksAmount(--savedBooksAmount));
    }

    private static class Singleton {
        private static final SimpleBookService INSTANCE = new SimpleBookService();
    }
}
