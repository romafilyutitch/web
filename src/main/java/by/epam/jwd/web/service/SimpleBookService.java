package by.epam.jwd.web.service;

import by.epam.jwd.web.dao.AuthorDao;
import by.epam.jwd.web.dao.BookDao;
import by.epam.jwd.web.dao.CommentDao;
import by.epam.jwd.web.dao.DAOFactory;
import by.epam.jwd.web.dao.UserDao;
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
import java.util.stream.Collectors;

class SimpleBookService implements BookService {
    private static final Logger logger = LogManager.getLogger(SimpleBookService.class);

    private final BookDao bookDao = DAOFactory.getInstance().getBookDao();
    private final AuthorDao authorDao = DAOFactory.getInstance().getAuthorDao();
    private final CommentDao commentDao = DAOFactory.getInstance().getCommentDao();
    private final UserDao userDao = DAOFactory.getInstance().getUserDao();

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
    private static final String BOOK_BY_NAME_WAS_FOUND_MESSAGE = "Book by name was found %s";
    private static final String BOOK_BY_MAME_WAS_NOT_FOUND_MESSAGE = "Book by name %s was not found";
    private static final String ALL_BOOKS_WERE_FOUND_MESSAGE = "All books were found";
    private static final String AUTHOR_WAS_NOT_FOUND_MESSAGE = "Saved author with id %d does not exist";
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
        allBooks = fillWithAuthor(allBooks);
        allBooks = fillWithComment(allBooks);
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
        foundPage = fillWithAuthor(foundPage);
        foundPage = fillWithComment(foundPage);
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
        foundBook = fillWithAuthor(foundBook);
        foundBook = fillWithComment(foundBook);
        logger.info(String.format(BOOK_WAS_FOUND_BY_ID_MESSAGE, foundBook));
        return foundBook;
    }

    @Override
    public void addOneCopy(Book book) {
        final AtomicInteger copiesAmount = new AtomicInteger(book.getCopiesAmount());
        final Book updatedBook = bookDao.update(book.updatedBooksAmount(copiesAmount.incrementAndGet()));
        logger.info(String.format(COPY_WAS_ADDED_MESSAGE, updatedBook));
    }

    @Override
    public void removeOneCopy(Book book) {
        final AtomicInteger copiesAmount = new AtomicInteger(book.getCopiesAmount());
        final Book updatedBook = bookDao.update(book.updatedBooksAmount(copiesAmount.decrementAndGet()));
        logger.info(String.format(COPY_WAS_REMOVED_MESSAGE, updatedBook));
    }

    @Override
    public Book register(Book book) throws RegisterException {
        final Optional<Book> optionalBook = bookDao.findByName(book.getName());
        if (optionalBook.isPresent()) {
            logger.info(String.format(BOOK_WITH_NAME_EXISTS_MESSAGE, book.getName()));
            throw new RegisterException(String.format(BOOK_WITH_NAME_EXISTS_MESSAGE, book.getName()));
        }
        final Optional<Author> optionalAuthor = authorDao.getByName(book.getAuthor().getName());
        if (!optionalAuthor.isPresent()) {
            logger.info(String.format(AUTHOR_DOES_NOT_EXIST_MESSAGE, book.getAuthor()));
            final Author savedAuthor = authorDao.save(book.getAuthor());
            book = book.updateAuthor(savedAuthor);
        } else {
            book = book.updateAuthor(optionalAuthor.get());
        }
        Book savedBook = bookDao.save(book);
        savedBook = fillWithAuthor(savedBook);
        savedBook = fillWithComment(savedBook);
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
        foundBooksByGenre = fillWithAuthor(foundBooksByGenre);
        foundBooksByGenre = fillWithComment(foundBooksByGenre);
        logger.info(String.format(BOOKS_WERE_FOUND_BY_GENRE_MESSAGE, foundBooksByGenre.size(), genre));
        return foundBooksByGenre;
    }


    @Override
    public Optional<Book> findByName(String name) {
        Optional<Book> optionalBook = bookDao.findByName(name);
        if (optionalBook.isPresent()) {
            Book foundBook = optionalBook.get();
            foundBook = fillWithAuthor(foundBook);
            foundBook = fillWithComment(foundBook);
            optionalBook = Optional.of(foundBook);
            logger.info(String.format(BOOK_BY_NAME_WAS_FOUND_MESSAGE, foundBook));
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

    private List<Book> fillWithAuthor(List<Book> books) {
        return books.stream().map(this::fillWithAuthor).collect(Collectors.toList());
    }

    private Book fillWithAuthor(Book book) {
        final Optional<Author> foundAuthor = authorDao.findById(book.getAuthor().getId());
        if (!foundAuthor.isPresent()) {
            throw new ServiceException(String.format(AUTHOR_WAS_NOT_FOUND_MESSAGE, book.getAuthor().getId()));
        }
        return book.updateAuthor(foundAuthor.get());
    }

    private Book fillWithComment(Book book) {
        List<Comment> bookComments = commentDao.findByBookId(book.getId());
        bookComments = bookComments.stream().map(comment -> {
            final Optional<User> optionalUser = userDao.findById(comment.getUser().getId());
            final Optional<Book> optionalBook = bookDao.findById(comment.getBook().getId());
            if (!optionalUser.isPresent()) {
                throw new ServiceException(String.format("Saved user with id %d was not found", comment.getUser().getId()));
            }
            if(!optionalBook.isPresent()) {
                throw new ServiceException(String.format("Saved book with id %d was not found", comment.getBook().getId()));
            }
            return new Comment(comment.getId(), optionalUser.get(), optionalBook.get(), comment.getDate(), comment.getText());
        }).collect(Collectors.toList());
        return book.updateBookComments(bookComments);
    }

    private List<Book> fillWithComment(List<Book> books) {
        return books.stream().map(this::fillWithComment).collect(Collectors.toList());
    }


    private static class Singleton {
        private static final SimpleBookService INSTANCE = new SimpleBookService();
    }
}
