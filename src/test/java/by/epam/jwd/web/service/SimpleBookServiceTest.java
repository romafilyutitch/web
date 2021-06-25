package by.epam.jwd.web.service;

import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.exception.ConnectionPoolInitializationException;
import by.epam.jwd.web.exception.RegisterException;
import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.model.Author;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class SimpleBookServiceTest {
    private static final ConnectionPool POOL = ConnectionPool.getConnectionPool();
    private final SimpleBookService testService = SimpleBookService.getInstance();
    private Book testBook = new Book("New book", new Author("new Author"), Genre.FANTASY, LocalDate.now(), 100, "New test book");

    @BeforeClass
    public static void initPool() throws ConnectionPoolInitializationException {
        POOL.init();
    }

    @AfterClass
    public static void destroyPool() {
        POOL.destroy();
    }

    @Before
    public void setUp() throws Exception {
        testBook = testService.register(testBook);
    }

    @After
    public void tearDown() throws Exception {
        testService.delete(testBook.getId());
    }

    @Test
    public void getInstance() {
        final SimpleBookService instance = SimpleBookService.getInstance();
        Assert.assertEquals("Got instance must be equal to test instance", testService, instance);
    }

    @Test
    public void findAll_mustReturnNotNullList() {
        final List<Book> allBooks = testService.findAll();
        Assert.assertNotNull("All books list must be not null", allBooks);
    }

    @Test
    public void findPage_mustReturnNotNullPageList() {
        final int pagesAmount = testService.getPagesAmount();
        final List<Book> foundPage = testService.findPage(pagesAmount);
        Assert.assertNotNull("Found page list must be not null", foundPage);
    }

    @Test
    public void getPagesAmount_mustReturnNotNegativeNumber() {
        final int pagesAmount = testService.getPagesAmount();
        Assert.assertTrue("Pages amount must be not negative", pagesAmount >= 0);
    }

    @Test
    public void findById_mustReturnSavedBookById() {
        final Book foundBook = testService.findById(testBook.getId());
        Assert.assertNotNull("Found book must be not null", foundBook);
        Assert.assertEquals("Found books myst be equal to test book", foundBook, testBook);
    }

    @Test(expected = ServiceException.class)
    public void findById_mustThrowException_whenBookWithIdDoesNotExist() {
        testService.findById(0L);
    }

    @Test
    public void addOneCopy() {
        final int initCopiesAmount = testBook.getCopiesAmount();
        final Book bookWithAddedCopy = testService.addOneCopy(testBook.getId());
        final int finalCopiesAmount = bookWithAddedCopy.getCopiesAmount();

        Assert.assertNotNull("Book with added copy must be not null", bookWithAddedCopy);
        Assert.assertEquals("Book with added copy id must be equal to test book id", bookWithAddedCopy.getId(), testBook.getId());
        Assert.assertTrue("Final copies amount must be greater than init copies amount", initCopiesAmount < finalCopiesAmount);
    }

    @Test
    public void removeOneCopy() {
        final int initCopiesAmount = testBook.getCopiesAmount();
        final Book bookWithRemovedCopy = testService.removeOneCopy(testBook.getId());
        final int finalCopiesAmount = bookWithRemovedCopy.getCopiesAmount();

        Assert.assertNotNull("Book with removed copy must be not null", bookWithRemovedCopy);
        Assert.assertEquals("Book with added copy id must be equal to test book id", bookWithRemovedCopy.getId(), testBook.getId());
        Assert.assertTrue("Final copies amount must be less than init copies amount", initCopiesAmount > finalCopiesAmount);
    }

    @Test
    public void register_mustReturnBookWithId() {
        Assert.assertNotNull("Registered book must be not null", testBook);
        Assert.assertNotNull("Registered book id must be not null", testBook.getId());
    }

    @Test(expected = RegisterException.class)
    public void register_mustThrowException_whenBookWithNameAlreadyExists() throws RegisterException {
        testService.register(testBook);
    }

    @Test
    public void delete_mustDeleteBook() {
        testService.delete(testBook.getId());
        final List<Book> allBooks = testService.findAll();
        Assert.assertFalse("All books must not contain deleted book", allBooks.contains(testBook));
    }

    @Test
    public void findByGenre_mustReturnListOfBooksWithSpecifiedGenre_whenFantasyGenrePassed() {
        final List<Book> foundBooksByGenre = testService.findByGenre(Genre.FANTASY);
        Assert.assertNotNull("found list of books by genre must be not null", foundBooksByGenre);
        if (!foundBooksByGenre.isEmpty()) {
            for (Book book : foundBooksByGenre) {
                if (!book.getGenre().equals(Genre.FANTASY)) {
                    Assert.fail("Found fantasy books list contains not fantasy book");
                }
            }
        }
    }

    @Test
    public void findByName_mustReturnSavedBook_ifSavedBookNameWasPassed() {
        final Optional<Book> optionalBook = testService.findByName(testBook.getName());
        Assert.assertNotNull("Optional book must be not null", optionalBook);
        Assert.assertTrue("Optional book must contain saved book", optionalBook.isPresent());
        Assert.assertEquals("Returned book must be equal to test book", testBook, optionalBook.get());
    }

    @Test
    public void findByName_mustReturnEmptyBook_ifNotSavedBookNameWasPassed() {
        testService.delete(testBook.getId());
        final Optional<Book> optionalBook = testService.findByName(testBook.getName());
        Assert.assertNotNull("Optional book must be not null", optionalBook);
        Assert.assertFalse("Optional book must be empty", optionalBook.isPresent());
    }
}