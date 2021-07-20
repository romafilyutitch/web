package by.epam.jwd.web.service;

import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.exception.ConnectionPoolInitializationException;
import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.model.Author;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SimpleBookServiceTest {
    private static final ConnectionPool POOL = ConnectionPool.getConnectionPool();
    private final SimpleBookService testService = SimpleBookService.getInstance();
    private Book testBook = new Book("New book", new Author("new Author"), Genre.FANTASY, LocalDate.now(), 100,"New test book");

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
        assertEquals("Got instance must be equal to test instance", testService, instance);
    }

    @Test
    public void findAll_mustReturnNotNullList() {
        final List<Book> allBooks = testService.findAll();
        assertNotNull("All books list must be not null", allBooks);
    }

    @Test
    public void findPage_mustReturnNotNullPageList() {
        final int pagesAmount = testService.getPagesAmount();
        final List<Book> foundPage = testService.findPage(pagesAmount);
        assertNotNull("Found page list must be not null", foundPage);
    }

    @Test
    public void getPagesAmount_mustReturnNotNegativeNumber() {
        final int pagesAmount = testService.getPagesAmount();
        assertTrue("Pages amount must be not negative", pagesAmount >= 0);
    }

    @Test
    public void findById_mustReturnSavedBookById() {
        final Book foundBook = testService.findById(testBook.getId());
        assertNotNull("Found book must be not null", foundBook);
        assertEquals("Found books myst be equal to test book", foundBook, testBook);
    }

    @Test(expected = ServiceException.class)
    public void findById_mustThrowException_whenBookWithIdDoesNotExist() {
        testService.delete(testBook.getId());
        testService.findById(testBook.getId());
    }

    @Test
    public void addOneCopy() {
        final int initCopiesAmount = testBook.getCopiesAmount();
        testService.addOneCopy(testBook);
        Book foundBook = testService.findById(testBook.getId());
        final int finalCopiesAmount = foundBook.getCopiesAmount();
        assertTrue("Final copies amount must be greater than init copies amount", initCopiesAmount < finalCopiesAmount);
    }

    @Test
    public void removeOneCopy() {
        final int initCopiesAmount = testBook.getCopiesAmount();
        testService.removeOneCopy(testBook);
        final Book foundBook = testService.findById(testBook.getId());
        final int finalCopiesAmount = foundBook.getCopiesAmount();

        assertTrue("Final copies amount must be less than init copies amount", initCopiesAmount > finalCopiesAmount);
    }

    @Test
    public void register_mustReturnBookWithId() {
        assertNotNull("Registered book must be not null", testBook);
        assertNotNull("Registered book id must be not null", testBook.getId());
    }

    @Test
    public void delete_mustDeleteBook() {
        testService.delete(testBook.getId());
        final List<Book> allBooks = testService.findAll();
        assertFalse("All books must not contain deleted book", allBooks.contains(testBook));
    }

    @Test
    public void findByGenre_mustReturnListOfBooksWithPassedGenre() {
        final List<Book> foundBooks = testService.findByGenre(testBook.getGenre());
        assertNotNull("found list of books by genre must be not null", foundBooks);
        if (!foundBooks.isEmpty()) {
            for (Book foundBook : foundBooks) {
                assertEquals("Found book must have passed genre", testBook.getGenre(), foundBook.getGenre());
            }
        }
    }

    @Test
    public void findByName_mustReturnSavedBook_whenSavedBookNameWasPassed() {
        final Optional<Book> optionalBook = testService.findByName(testBook.getName());
        assertNotNull("Returned value must be not null", optionalBook);
        assertTrue("Optional book must contain saved book", optionalBook.isPresent());
        assertEquals("Returned book must be equal to test book", testBook, optionalBook.get());
    }

    @Test
    public void findByName_mustReturnEmptyBook_whenThereIsNoBookWithPassedName() {
        testService.delete(testBook.getId());
        final Optional<Book> optionalBook = testService.findByName(testBook.getName());
        assertNotNull("Optional book must be not null", optionalBook);
        assertFalse("Optional book must be empty", optionalBook.isPresent());
    }
}