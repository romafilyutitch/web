package by.epam.jwd.web.service;

import by.epam.jwd.web.dao.BookDao;
import by.epam.jwd.web.dao.DAOFactory;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Service implementation for book service interface.
 * Makes all operations related to book in application.
 *
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
class SimpleBookService implements BookService {
    private static final Logger logger = LogManager.getLogger(SimpleBookService.class);
    private final BookDao bookDao = DAOFactory.getFactory().getBookDao();

    private static final String PAGE_WAS_FOUND_MESSAGE = "Page of books number %d was found size = %d";
    private static final String SAVED_BOOK_WAS_NOT_FOUND_BY_ID_MESSAGE = "Saved book with id %d was not found";
    private static final String BOOK_WAS_FOUND_BY_ID_MESSAGE = "Book was found by id %s";
    private static final String COPY_WAS_ADDED_MESSAGE = "One copy for book was added %s";
    private static final String COPY_WAS_REMOVED_MESSAGE = "One copy for book was removed %s";
    private static final String BOOK_WAS_SAVED_MESSAGE = "Book was saved %s";
    private static final String BOOK_WAS_DELETED_MESSAGE = "Book was deleted %s";
    private static final String BOOKS_WERE_FOUND_BY_GENRE_MESSAGE = "Books by genre %s were found size = %d";
    private static final String BOOKS_WITH_NAME_LIKE_WERE_FOUND = "Books by name like %s were found size = %d";
    private static final String ALL_BOOKS_WERE_FOUND_MESSAGE = "All books were found size = %d";
    private static final String NO_BOOKS_WITH_NAME_MESSAGE = "Find book by name but there is no any book with name %s";
    private static final String BOOK_WAS_FOUND_BY_NAME_MESSAGE = "Book was found by name %s";
    private static final String BOOKS_WERE_SORTED_BY_NAME_MESSAGE = "Books were sorted by name";
    private static final String BOOKS_WERE_SORTED_BY_LIKES_MESSAGE = "Books were sorted by likes";
    private static final String BOOKS_WERE_SORTED_BY_COMMENTS_MESSAGE = "Books were sorted by comments";

    private SimpleBookService() {
    }

    /**
     * Gets single class instance from nested class
     *
     * @return class instance.
     */
    public static SimpleBookService getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Finds and returns find result of all books from database.
     * Delegates find to book dao.
     *
     * @return all found books collections.
     */
    @Override
    public List<Book> findAll() {
        List<Book> allBooks = bookDao.findAll();
        logger.info(String.format(ALL_BOOKS_WERE_FOUND_MESSAGE, allBooks.size()));
        return allBooks;
    }

    /**
     * Finds passed page of saved books from database.
     *
     * @param pageNumber page that need to be found.
     * @return collection of book on passed page.
     * @throws IllegalStateException when passed page number is negative or
     *                               page number is greater then pages amount.
     */
    @Override
    public List<Book> findPage(int pageNumber) {
        if (pageNumber <= 0 || pageNumber > getPagesAmount()) {
            throw new IllegalArgumentException();
        }
        List<Book> foundPage = bookDao.findPage(pageNumber);
        logger.info(String.format(PAGE_WAS_FOUND_MESSAGE, pageNumber, foundPage.size()));
        return foundPage;
    }

    /**
     * Returns current saved books pages amount.
     *
     * @return current saved book pages amount.
     */
    @Override
    public int getPagesAmount() {
        return bookDao.getPagesAmount();
    }

    /**
     * Finds saved book by passed id.
     *
     * @param id by what need to find book.
     * @return saved book that has passed id.
     * @throws ServiceException when saved book was not found by id.
     */
    @Override
    public Book findById(Long id) {
        final Optional<Book> optionalBook = bookDao.findById(id);
        if (!optionalBook.isPresent()) {
            logger.error(String.format(SAVED_BOOK_WAS_NOT_FOUND_BY_ID_MESSAGE, id));
            throw new ServiceException(String.format(SAVED_BOOK_WAS_NOT_FOUND_BY_ID_MESSAGE, id));
        }
        Book foundBook = optionalBook.get();
        logger.info(String.format(BOOK_WAS_FOUND_BY_ID_MESSAGE, foundBook));
        return foundBook;
    }

    /**
     * Adds on copy to passed book.
     *
     * @param book which need to add one copy.
     */
    @Override
    public void addOneCopy(Book book) {
        final AtomicInteger bookCurrentCopiesAmount = new AtomicInteger(book.getCopiesAmount());
        final Book bookWithAddedCopy = new Book(book.getId(), book.getName(), book.getAuthor(), book.getGenre(), book.getDate(), book.getPagesAmount(), bookCurrentCopiesAmount.incrementAndGet(), book.getText(), book.getLikesAmount(), book.getCommentsAmount());
        final Book updatedBook = bookDao.update(bookWithAddedCopy);
        logger.info(String.format(COPY_WAS_ADDED_MESSAGE, updatedBook));
    }

    /**
     * Removes on copy from passed book
     *
     * @param book which need to add one copy.
     */
    @Override
    public void removeOneCopy(Book book) {
        final AtomicInteger copiesAmount = new AtomicInteger(book.getCopiesAmount());
        final Book bookWithRemovedCopy = new Book(book.getId(), book.getName(), book.getAuthor(), book.getGenre(), book.getDate(), book.getPagesAmount(), copiesAmount.decrementAndGet(), book.getText(), book.getLikesAmount(), book.getCommentsAmount());
        final Book updatedBook = bookDao.update(bookWithRemovedCopy);
        logger.info(String.format(COPY_WAS_REMOVED_MESSAGE, updatedBook));
    }

    /**
     * Makes book save.
     *
     * @param book that need to be saved.
     * @return saved book with generated id.
     */
    @Override
    public Book save(Book book) {
        Book savedBook = bookDao.save(book);
        logger.info(String.format(BOOK_WAS_SAVED_MESSAGE, savedBook));
        return savedBook;
    }

    /**
     * Deletes saved book that has passed by it's id
     *
     * @param book book that need to be deleted.
     */
    @Override
    public void delete(Book book) {
        bookDao.delete(book.getId());
        logger.info(String.format(BOOK_WAS_DELETED_MESSAGE, book));
    }

    /**
     * Finds books that have passed book genre.
     *
     * @param genre those books need to be found.
     * @return books that have passed genre collection.
     */
    @Override
    public List<Book> findByGenre(Genre genre) {
        List<Book> foundBooksByGenre = bookDao.findByGenre(genre);
        logger.info(String.format(BOOKS_WERE_FOUND_BY_GENRE_MESSAGE, genre, foundBooksByGenre.size()));
        return foundBooksByGenre;
    }


    /**
     * Finds Book which names are like passed name.
     * @param name of those books that hase name like passed
     * @return collection of books which names matches with the passed one.
     */
    @Override
    public List<Book> findWhereNameLike(String name) {
        List<Book> booksByName = bookDao.findWhereNameLike(name);
        logger.info(String.format(BOOKS_WITH_NAME_LIKE_WERE_FOUND, name, booksByName.size()));
        return booksByName;
    }

    /**
     * Finds book by passed name.
     * @param name to book which need to be found by name
     * @return not empty optional book if there is book with passed name
     * or empty optional book otherwise
     */
    @Override
    public Optional<Book> findByName(String name) {
        final Optional<Book> optionalBook = bookDao.findByName(name);
        if (optionalBook.isPresent()) {
            final Book foundBook = optionalBook.get();
            logger.info(String.format(BOOK_WAS_FOUND_BY_NAME_MESSAGE, foundBook));
        } else {
            logger.info(String.format(NO_BOOKS_WITH_NAME_MESSAGE, name));
        }
        return optionalBook;
    }

    @Override
    public List<Book> sortByName(List<Book> books) {
        books.sort(Comparator.comparing(Book::getName));
        logger.info(BOOKS_WERE_SORTED_BY_NAME_MESSAGE);
        return books;
    }

    @Override
    public List<Book> sortByLikes(List<Book> books) {
        books.sort(Comparator.comparingInt(Book::getLikesAmount).reversed());
        logger.info(BOOKS_WERE_SORTED_BY_LIKES_MESSAGE);
        return books;
    }

    @Override
    public List<Book> sortByComments(List<Book> books) {
        books.sort(Comparator.comparingInt(Book::getCommentsAmount).reversed());
        logger.info(BOOKS_WERE_SORTED_BY_COMMENTS_MESSAGE);
        return books;
    }

    /**
     * Nested class that encapsulates single {@link SimpleBookService} instance.
     * Singleton pattern variation.
     *
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final SimpleBookService INSTANCE = new SimpleBookService();
    }
}
