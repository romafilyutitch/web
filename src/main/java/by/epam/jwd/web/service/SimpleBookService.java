package by.epam.jwd.web.service;

import by.epam.jwd.web.dao.AuthorDao;
import by.epam.jwd.web.dao.BookDao;
import by.epam.jwd.web.dao.CommentDao;
import by.epam.jwd.web.dao.DAOFactory;
import by.epam.jwd.web.exception.RegisterException;
import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.model.Author;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Comment;
import by.epam.jwd.web.model.Genre;
import by.epam.jwd.web.model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

class SimpleBookService implements BookService {
    private static final Logger logger = LogManager.getLogger(SimpleBookService.class);

    private final BookDao bookDao = DAOFactory.getInstance().getBookDao();
    private final AuthorDao authorDao = DAOFactory.getInstance().getAuthorDao();
    private final CommentDao commentDao = DAOFactory.getInstance().getCommentDao();

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
    private static final String BOOK_BY_NAME_WAS_FOUND_MESSAGE = "Book by name %s was found";
    private static final String BOOK_BY_MAME_WAS_NOT_FOUND_MESSAGE = "Book by name %s was not found";
    private static final String ALL_BOOKS_WERE_FOUND_MESSAGE = "All books were found";
    private static final String COMMENT_WAS_ADD_MESSAGE = "New comment was add to book %s";
    private static final String LIKE_WAS_ADD_MESSAGE = "One like was add to book %s";
    private static final String LIKE_WAS_REMOVED_MESSAGE = "Like was removed from book %s";

    private SimpleBookService() {
    }

    public static SimpleBookService getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public List<Book> findAll() {
        List<Book> allBooks = bookDao.findAll();
        logger.info(ALL_BOOKS_WERE_FOUND_MESSAGE);
        return allBooks;
    }

    @Override
    public List<Book> findPage(int pageNumber) {
        List<Book> foundPage;
        if (pageNumber < 1) {
            foundPage = bookDao.findPage(1);
        } else if (pageNumber >= getPagesAmount()) {
            foundPage = bookDao.findPage(getPagesAmount());
        } else {
            foundPage = bookDao.findPage(pageNumber);
        }
        logger.info(String.format(PAGE_WAS_FOUND_MESSAGE, pageNumber));
        return foundPage;
    }

    @Override
    public int getPagesAmount() {
        return bookDao.getPagesAmount();
    }

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

    @Override
    public void addOneCopy(Book book) {
        final AtomicInteger bookCurrentCopiesAmount = new AtomicInteger(book.getCopiesAmount());
        final Book bookWithAddedCopy = new Book(book.getId(), book.getName(), book.getAuthor(), book.getGenre(), book.getDate(), book.getPagesAmount(), bookCurrentCopiesAmount.incrementAndGet(), book.getDescription(), book.getLikes());
        final Book updatedBook = bookDao.update(bookWithAddedCopy);
        logger.info(String.format(COPY_WAS_ADDED_MESSAGE, updatedBook));
    }

    @Override
    public void removeOneCopy(Book book) {
        final AtomicInteger copiesAmount = new AtomicInteger(book.getCopiesAmount());
        final Book bookWithRemovedCopy = new Book(book.getId(), book.getName(), book.getAuthor(), book.getGenre(), book.getDate(), book.getPagesAmount(), copiesAmount.decrementAndGet(), book.getDescription(), book.getLikes());
        final Book updatedBook = bookDao.update(bookWithRemovedCopy);
        logger.info(String.format(COPY_WAS_REMOVED_MESSAGE, updatedBook));
    }

    @Override
    public Book register(Book book) throws RegisterException {
        final Book bookToSave;
        final Optional<Book> optionalBook = bookDao.findByName(book.getName());
        if (optionalBook.isPresent()) {
            logger.info(String.format(BOOK_WITH_NAME_EXISTS_MESSAGE, book.getName()));
            throw new RegisterException(String.format(BOOK_WITH_NAME_EXISTS_MESSAGE, book.getName()));
        }
        final Optional<Author> optionalAuthor = authorDao.getByName(book.getAuthor().getName());
        if (!optionalAuthor.isPresent()) {
            logger.info(String.format(AUTHOR_DOES_NOT_EXIST_MESSAGE, book.getAuthor()));
            final Author savedAuthor = authorDao.save(book.getAuthor());
            bookToSave = new Book(book.getName(), savedAuthor, book.getGenre(), book.getDate(), book.getPagesAmount(), book.getCopiesAmount(), book.getDescription(), 0);
        } else {
            bookToSave = new Book(book.getName(), optionalAuthor.get(), book.getGenre(), book.getDate(), book.getPagesAmount(), book.getCopiesAmount(), book.getDescription(),0);
        }
        Book savedBook = bookDao.save(bookToSave);
        logger.info(String.format(BOOK_WAS_SAVED_MESSAGE, savedBook));
        return savedBook;
    }

    @Override
    public void delete(Long bookId) {
        bookDao.delete(bookId);
        logger.info(String.format(BOOK_WAS_DELETED_MESSAGE, bookId));
    }

    @Override
    public List<Book> findByGenre(Genre genre) {
        List<Book> foundBooksByGenre = bookDao.findByGenreId(genre.getId());
        logger.info(String.format(BOOKS_WERE_FOUND_BY_GENRE_MESSAGE, foundBooksByGenre.size(), genre));
        return foundBooksByGenre;
    }


    @Override
    public Optional<Book> findByName(String name) {
        Optional<Book> optionalBook = bookDao.findByName(name);
        if (optionalBook.isPresent()) {
            logger.info(String.format(BOOK_BY_NAME_WAS_FOUND_MESSAGE, name));
        } else {
            logger.info(String.format(BOOK_BY_MAME_WAS_NOT_FOUND_MESSAGE, name));
        }
        return optionalBook;
    }

    @Override
    public void addComment(Comment comment) {
        final Comment savedComment = commentDao.save(comment);
        logger.info(String.format(COMMENT_WAS_ADD_MESSAGE, savedComment));
    }

    @Override
    public void addLike(Book book, User user) {
        bookDao.addLike(book.getId(), user.getId());
        logger.info(String.format(LIKE_WAS_ADD_MESSAGE, book));
    }

    @Override
    public void removeLike(Book book, User user) {
        bookDao.removeLike(book.getId(), user.getId());
        logger.info(String.format(LIKE_WAS_REMOVED_MESSAGE, book));
    }

    @Override
    public boolean isLikedByUser(Book book, User user) {
        return bookDao.isLikedByUserWithId(book.getId(), user.getId());
    }

    private static class Singleton {
        private static final SimpleBookService INSTANCE = new SimpleBookService();
    }
}
