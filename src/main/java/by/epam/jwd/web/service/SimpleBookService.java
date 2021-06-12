package by.epam.jwd.web.service;

import by.epam.jwd.web.dao.BookDao;
import by.epam.jwd.web.dao.DaoFactory;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.BookAuthor;
import by.epam.jwd.web.model.BookGenre;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

class SimpleBookService implements BookService{
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
        int savedBookBooksAmount = savedBook.getCopiesAmount();
        return BOOK_DAO.update(savedBook.updatedBooksAmount(++savedBookBooksAmount));
    }

    @Override
    public Book removeOneCopy(Long id) throws ServiceException {
        final Optional<Book> optionalBook = BOOK_DAO.findById(id);
        if (!optionalBook.isPresent()) {
            throw new ServiceException(String.format("Book with id %d does not exist", id));
        }
        final Book savedBook = optionalBook.get();
        int savedBooksAmount = savedBook.getCopiesAmount();
        return BOOK_DAO.update(savedBook.updatedBooksAmount(--savedBooksAmount));
    }

    @Override
    public Book createBook(String name, String author, String genre, String date, String pagesAmount, String description, String text) throws ServiceException {
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
        if (text == null || text.isEmpty()) {
            throw new ServiceException("Text is empty. Enter book text");
        }
        final int parsedPagesAmount = Integer.parseInt(pagesAmount);
        if (parsedPagesAmount <= 0) {
            throw new ServiceException("Pages amount is negative or equals 0. Enter positive pages amount");
        }

        final Book bookForSave = new Book(name, new BookAuthor(author), new BookGenre(genre), LocalDate.parse(date), parsedPagesAmount, description, text);
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

    @Override
    public List<Book> findByGenre(String genre) throws ServiceException {
        return BOOK_DAO.findBooksByGenreName(genre);
    }

    @Override
    public List<Book> findByAuthor(String author) throws ServiceException {
        return BOOK_DAO.findBooksByAuthorName(author);
    }

    @Override
    public Book findByName(String name) throws ServiceException {
        final Optional<Book> optionalBook = BOOK_DAO.findBookByName(name);
        if(!optionalBook.isPresent()) {
            throw new ServiceException(String.format("Book with name %s does not exist", name));
        }
        return optionalBook.get();
    }

    private static class Singleton {
        private static final SimpleBookService INSTANCE = new SimpleBookService();
    }
}
