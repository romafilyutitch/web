package by.epam.jwd.web.service;

import by.epam.jwd.web.dao.BookDao;
import by.epam.jwd.web.dao.DAOFactory;
import by.epam.jwd.web.exception.RegisterException;
import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

class SimpleBookService implements BookService {
    private static final BookDao BOOK_DAO = DAOFactory.getInstance().getBookDao();

    private SimpleBookService() {
    }

    public static SimpleBookService getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public List<Book> findAllBooks() {
        return BOOK_DAO.findAll();
    }

    @Override
    public Book findById(Long id) {
        final Optional<Book> optionalBook = BOOK_DAO.findById(id);
        if (!optionalBook.isPresent()) {
            throw new ServiceException(String.format("Saved book with id %s was not found", id));
        }
        return optionalBook.get();
    }

    @Override
    public Book addOneCopy(Long bookId) {
        final Book savedBook = findById(bookId);
        final AtomicInteger copiesAmount = new AtomicInteger(savedBook.getCopiesAmount());
        return BOOK_DAO.update(savedBook.updatedBooksAmount(copiesAmount.incrementAndGet()));
    }

    @Override
    public Book removeOneCopy(Long bookId) {
        final Book savedBook = findById(bookId);
        final AtomicInteger copiesAmount = new AtomicInteger(savedBook.getCopiesAmount());
        return BOOK_DAO.update(savedBook.updatedBooksAmount(copiesAmount.decrementAndGet()));
    }

    @Override
    public Book registerBook(Book book) throws RegisterException {
        final Optional<Book> optionalBook = BOOK_DAO.findBookByName(book.getName());
        if (optionalBook.isPresent()) {
            throw new RegisterException(String.format("Book with name %s already exists.", book.getName()));
        }
        return BOOK_DAO.save(book);
    }

    @Override
    public void deleteBook(Long bookId) throws ServiceException {
        BOOK_DAO.delete(bookId);
    }

    @Override
    public List<Book> findByGenre(Genre genre) throws ServiceException {
        return BOOK_DAO.findBooksByGenre(genre);
    }

    @Override
    public List<Book> findByAuthor(String author) throws ServiceException {
        return BOOK_DAO.findBooksByAuthorName(author);
    }

    @Override
    public Book findByName(String name) throws ServiceException {
        final Optional<Book> optionalBook = BOOK_DAO.findBookByName(name);
        if (!optionalBook.isPresent()) {
            throw new ServiceException(String.format("Book with name %s does not exist", name));
        }
        return optionalBook.get();
    }

    private static class Singleton {
        private static final SimpleBookService INSTANCE = new SimpleBookService();
    }
}
