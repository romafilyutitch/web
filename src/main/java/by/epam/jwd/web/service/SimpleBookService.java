package by.epam.jwd.web.service;

import by.epam.jwd.web.dao.AuthorDao;
import by.epam.jwd.web.dao.BookDao;
import by.epam.jwd.web.dao.DAOFactory;
import by.epam.jwd.web.exception.RegisterException;
import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.model.Author;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

class SimpleBookService implements BookService {
    private static final Logger logger = LogManager.getLogger(SimpleBookService.class);

    private static final BookDao BOOK_DAO = DAOFactory.getInstance().getBookDao();
    private static final AuthorDao AUTHOR_DAO = DAOFactory.getInstance().getAuthorDao();

    private SimpleBookService() {
    }

    public static SimpleBookService getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public List<Book> findAllBooks() {
        logger.trace("Trying to find all books");
        return BOOK_DAO.findAll();
    }

    @Override
    public Book findById(Long id) {
        logger.trace(String.format("Trying to find book with id %d ", id));
        final Optional<Book> optionalBook = BOOK_DAO.findById(id);
        if (!optionalBook.isPresent()) {
            logger.error(String.format("Saved book with id %d was not found", id));
            throw new ServiceException(String.format("Saved book with id %d was not found", id));
        }
        final Book foundBook = optionalBook.get();
        logger.info(String.format("Book was found by id %s", foundBook));
        return foundBook;
    }

    @Override
    public Book addOneCopy(Long bookId) {
        logger.trace(String.format("Trying to add one copy of book with id %d", bookId));
        final Book savedBook = findById(bookId);
        final AtomicInteger copiesAmount = new AtomicInteger(savedBook.getCopiesAmount());
        final Book updatedBook = BOOK_DAO.update(savedBook.updatedBooksAmount(copiesAmount.incrementAndGet()));
        logger.info(String.format("One copy of book %s was added", updatedBook));
        return updatedBook;
    }

    @Override
    public Book removeOneCopy(Long bookId) {
        logger.trace(String.format("Trying to remove one copy of book with id %d", bookId));
        final Book savedBook = findById(bookId);
        final AtomicInteger copiesAmount = new AtomicInteger(savedBook.getCopiesAmount());
        final Book updatedBook = BOOK_DAO.update(savedBook.updatedBooksAmount(copiesAmount.decrementAndGet()));
        logger.info(String.format("One copy of book %s was added", updatedBook));
        return updatedBook;
    }

    @Override
    public Book registerBook(Book book) throws RegisterException {
        logger.trace(String.format("Trying to register book %s", book));
        final Optional<Book> optionalBook = BOOK_DAO.findBookByName(book.getName());
        if (optionalBook.isPresent()) {
            logger.info(String.format("Book with name %s already exists. Cannot register book", book.getName()));
            throw new RegisterException(String.format("Book with name %s already exists. Cannot register book", book.getName()));
        }
        logger.trace(String.format("Trying to save book author %s", book.getAuthor()));
        final Optional<Author> optionalAuthor = AUTHOR_DAO.getByName(book.getAuthor().getName());
        if (!optionalAuthor.isPresent()) {
            logger.info(String.format("Author %s doesn't exist. Save author at first", book.getAuthor()));
            final Author savedAuthor = AUTHOR_DAO.save(book.getAuthor());
            return BOOK_DAO.save(book.updateAuthor(savedAuthor));
        } else {
            logger.info(String.format("Author %s exists. Save book"));
            return BOOK_DAO.save(book);
        }
    }

    @Override
    public void deleteBook(Long bookId) throws ServiceException {
        logger.trace(String.format("Trying to delete book with id %d", bookId));
        BOOK_DAO.delete(bookId);
        logger.info(String.format("Book with id %d was deleted", bookId));
    }

    @Override
    public List<Book> findByGenre(Genre genre) throws ServiceException {
        logger.trace(String.format("Trying to find all books by genre %s", genre));
        final List<Book> foundBooksByGenre = BOOK_DAO.findBooksByGenre(genre);
        logger.info(String.format("%d books with genre %s was found", foundBooksByGenre.size(), genre));
        return foundBooksByGenre;
    }

    @Override
    public List<Book> findByAuthor(String author) throws ServiceException {
        logger.trace(String.format("Trying to find all books by author %s", author));
        final List<Book> foundBooksByAuthor = BOOK_DAO.findBooksByAuthorName(author);
        logger.info(String.format("%d books by author %s was found", foundBooksByAuthor.size(), author));
        return foundBooksByAuthor;
    }

    @Override
    public Optional<Book> findByName(String name) throws ServiceException {
        logger.trace(String.format("Trying to find book by name %s", name));
        final Optional<Book> optionalBook = BOOK_DAO.findBookByName(name);
        if (optionalBook.isPresent()) {
            logger.info(String.format("Book by name was found %s", optionalBook.get()));
        } else {
            logger.info(String.format("Book by name %s was not found", name));
        }
        return optionalBook;
    }

    private static class Singleton {
        private static final SimpleBookService INSTANCE = new SimpleBookService();
    }
}
