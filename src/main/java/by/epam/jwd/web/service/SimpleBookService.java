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

    private static final String PAGE_WAS_FOUND_MESSAGE = "Page of books number %s was found";
    private static final String SAVED_BOOK_WAS_NOT_FOUND_BY_ID_MESSAGE = "Saved book with id %d was not found";
    private static final String BOOK_WAS_FOUND_BY_ID_MESSAGE = "Book was found by id %s";
    private static final String COPY_WAS_ADDED_MESSAGE = "One copy of book %s was added";
    private static final String COPY_WAS_REMOVED_MESSAGE = "One copy of book %s was removed";
    private static final String BOOK_WITH_NAME_EXISTS_MESSAGE = "Book with name %s already exists. Cannot register book";
    private static final String AUTHOR_DOES_NOT_EXIST_MESSAGE = "Author %s doesn't exist. Save author at first";
    private static final String BOOK_WAS_SAVED_MESSAGE = "Book was saved %s";
    private static final String BOOK_WAS_DELETED_MESSAGE = "Book with id %d was deleted";
    private static final String BOOKS_WERE_FOUND_BY_GENRE_MESSAGE = "%d books with genre %s was found";
    private static final String BOOKS_WERE_FOUND_BY_AUTHOR_MESSAGE = "%d books by author %s was found";
    private static final String BOOK_BY_NAME_WAS_FOUND_MESSAGE = "Book by name was found %s";
    private static final String BOOK_BY_MAME_WAS_NOT_FOUND_MESSAGE = "Book by name %s was not found";
    private static final String ALL_BOOKS_WERE_FOUND_MESSAGE = "All books were found";

    private SimpleBookService() {
    }

    public static SimpleBookService getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public List<Book> findAll() {
        final List<Book> allBooks = BOOK_DAO.findAll();
        logger.info(ALL_BOOKS_WERE_FOUND_MESSAGE);
        return allBooks;
    }

    @Override
    public List<Book> findPage(int pageNumber) {
        List<Book> foundPage;
        if (pageNumber < 1) {
            foundPage = BOOK_DAO.findPage(1);
        } else if (pageNumber >= getPagesAmount()) {
            foundPage = BOOK_DAO.findPage(getPagesAmount());
        } else {
            foundPage = BOOK_DAO.findPage(pageNumber);
        }
        logger.info(String.format(PAGE_WAS_FOUND_MESSAGE, pageNumber));
        return foundPage;
    }

    @Override
    public int getPagesAmount() {
        return BOOK_DAO.getPagesAmount();
    }

    @Override
    public Book findById(Long id) {
        final Optional<Book> optionalBook = BOOK_DAO.findById(id);
        if (!optionalBook.isPresent()) {
            logger.error(String.format(SAVED_BOOK_WAS_NOT_FOUND_BY_ID_MESSAGE, id));
            throw new ServiceException(String.format(SAVED_BOOK_WAS_NOT_FOUND_BY_ID_MESSAGE, id));
        }
        final Book foundBook = optionalBook.get();
        logger.info(String.format(BOOK_WAS_FOUND_BY_ID_MESSAGE, foundBook));
        return foundBook;
    }

    @Override
    public Book addOneCopy(Long bookId) {
        final Book savedBook = findById(bookId);
        final AtomicInteger copiesAmount = new AtomicInteger(savedBook.getCopiesAmount());
        final Book updatedBook = BOOK_DAO.update(savedBook.updatedBooksAmount(copiesAmount.incrementAndGet()));
        logger.info(String.format(COPY_WAS_ADDED_MESSAGE, updatedBook));
        return updatedBook;
    }

    @Override
    public Book removeOneCopy(Long bookId) {
        final Book savedBook = findById(bookId);
        final AtomicInteger copiesAmount = new AtomicInteger(savedBook.getCopiesAmount());
        final Book updatedBook = BOOK_DAO.update(savedBook.updatedBooksAmount(copiesAmount.decrementAndGet()));
        logger.info(String.format(COPY_WAS_REMOVED_MESSAGE, updatedBook));
        return updatedBook;
    }

    @Override
    public Book register(Book book) throws RegisterException {
        final Optional<Book> optionalBook = BOOK_DAO.findBookByName(book.getName());
        if (optionalBook.isPresent()) {
            logger.info(String.format(BOOK_WITH_NAME_EXISTS_MESSAGE, book.getName()));
            throw new RegisterException(String.format(BOOK_WITH_NAME_EXISTS_MESSAGE, book.getName()));
        }
        final Optional<Author> optionalAuthor = AUTHOR_DAO.getByName(book.getAuthor().getName());
        if (!optionalAuthor.isPresent()) {
            logger.info(String.format(AUTHOR_DOES_NOT_EXIST_MESSAGE, book.getAuthor()));
            final Author savedAuthor = AUTHOR_DAO.save(book.getAuthor());
            return BOOK_DAO.save(book.updateAuthor(savedAuthor));
        } else {
            final Book savedBook = BOOK_DAO.save(book);
            logger.info(String.format(BOOK_WAS_SAVED_MESSAGE, savedBook));
            return savedBook;
        }
    }

    @Override
    public void delete(Long bookId) {
        BOOK_DAO.delete(bookId);
        logger.info(String.format(BOOK_WAS_DELETED_MESSAGE, bookId));
    }

    @Override
    public List<Book> findByGenre(Genre genre) {
        final List<Book> foundBooksByGenre = BOOK_DAO.findBooksByGenre(genre);
        logger.info(String.format(BOOKS_WERE_FOUND_BY_GENRE_MESSAGE, foundBooksByGenre.size(), genre));
        return foundBooksByGenre;
    }

    @Override
    public List<Book> findByAuthor(String author) {
        final List<Book> foundBooksByAuthor = BOOK_DAO.findBooksByAuthorName(author);
        logger.info(String.format(BOOKS_WERE_FOUND_BY_AUTHOR_MESSAGE, foundBooksByAuthor.size(), author));
        return foundBooksByAuthor;
    }

    @Override
    public Optional<Book> findByName(String name) {
        final Optional<Book> optionalBook = BOOK_DAO.findBookByName(name);
        if (optionalBook.isPresent()) {
            logger.info(String.format(BOOK_BY_NAME_WAS_FOUND_MESSAGE, optionalBook.get()));
        } else {
            logger.info(String.format(BOOK_BY_MAME_WAS_NOT_FOUND_MESSAGE, name));
        }
        return optionalBook;
    }

    private static class Singleton {
        private static final SimpleBookService INSTANCE = new SimpleBookService();
    }
}
