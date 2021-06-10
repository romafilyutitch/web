package by.epam.jwd.web.service;

import by.epam.jwd.web.dao.BookDao;
import by.epam.jwd.web.dao.DaoFactory;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.BookAuthor;
import by.epam.jwd.web.model.BookGenre;

import java.time.LocalDate;
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
        return BOOK_DAO.update(savedBook.updatedBooksAmount(--savedBooksAmount));
    }

    @Override
    public Book createBook(String name, String author, String genre, String date, String pagesAmount, String description) throws ServiceException {
        final Optional<Book> optionalBook = BOOK_DAO.findBookByName(name);
        if (optionalBook.isPresent()) {
            throw new ServiceException(String.format("Book with name %s already exists. Try to add one copy if you want ", name));
        }
        if (name == null || name.isEmpty()) {
            throw new ServiceException("Book name is empty. Enter book name");
        }
        if (author == null || author.isEmpty()) {
            throw new ServiceException("Author name is empty. Enter author name");
        }
        if (genre == null || genre.isEmpty()) {
            throw new ServiceException("Genre name is empty. Enter genre name");
        }
        if (date == null || date.isEmpty()) {
            throw new ServiceException("Date is empty. Enter date");
        }
        if (pagesAmount == null || pagesAmount.isEmpty()) {
            throw new ServiceException("Pages amount is empty. Enter pages amount");
        }
        final int parsedPagesAmount = Integer.parseInt(pagesAmount);
        if (parsedPagesAmount <= 0) {
            throw new ServiceException("Pages amount is negative or equals 0. Enter positive pages amount");
        }
        final Book bookForSave = new Book(name, new BookAuthor(author), new BookGenre(genre), LocalDate.parse(date), parsedPagesAmount, description);
        System.out.println("++++++SAVING BOOK++++++" + bookForSave);
        return BOOK_DAO.save(bookForSave);
    }

    @Override
    public void deleteBook(Long bookId) throws ServiceException {
        final Optional<Book> optionalBook = BOOK_DAO.findById(bookId);
        if (!optionalBook.isPresent()) {
            throw new ServiceException(String.format("Book with id %d does not exist", bookId));
        }
        BOOK_DAO.delete(bookId);
    }

    private static class Singleton {
        private static final SimpleBookService INSTANCE = new SimpleBookService();
    }
}
