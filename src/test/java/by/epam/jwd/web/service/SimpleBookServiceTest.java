package by.epam.jwd.web.service;

import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.connectionPool.ConnectionPoolInitializationException;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class SimpleBookServiceTest {
    private final SimpleBookService testService = SimpleBookService.getInstance();
    private Book testBook = new Book("New book", "new author", Genre.FANTASY, 100, "New test book");

    @BeforeClass
    public static void initPool() throws ConnectionPoolInitializationException {
        ConnectionPool.getConnectionPool().init();
    }

    @AfterClass
    public static void destroyPool() {
        ConnectionPool.getConnectionPool().destroy();
    }

    @Before
    public void setUp() throws Exception {
        testBook = testService.save(testBook);
    }

    @After
    public void tearDown() throws Exception {
        testService.delete(testBook);
    }

    @Test
    public void getInstance() {
        final SimpleBookService instance = SimpleBookService.getInstance();
        assertEquals(testService, instance);
    }

    @Test
    public void findAll_mustReturnNotNullList() {
        final List<Book> allBooks = testService.findAll();
        assertNotNull(allBooks);
    }

    @Test
    public void findPage_mustReturnNotNullPageList() {
        final int pagesAmount = testService.getPagesAmount();
        final List<Book> foundPage = testService.findPage(pagesAmount);
        assertNotNull(foundPage);
    }

    @Test
    public void getPagesAmount_mustReturnNotNegativeNumber() {
        final int pagesAmount = testService.getPagesAmount();
        assertTrue(pagesAmount >= 0);
    }

    @Test
    public void findById_mustReturnSavedBookById() {
        final Book foundBook = testService.findById(testBook.getId());
        assertNotNull(foundBook);
        assertEquals(foundBook, testBook);
    }

    @Test(expected = ServiceException.class)
    public void findById_mustThrowException_whenBookWithIdDoesNotExist() {
        testService.delete(testBook);
        testService.findById(testBook.getId());
    }

    @Test
    public void addOneCopy() {
        final int initCopiesAmount = testBook.getCopiesAmount();
        testService.addOneCopy(testBook);
        Book foundBook = testService.findById(testBook.getId());
        final int finalCopiesAmount = foundBook.getCopiesAmount();
        assertTrue(initCopiesAmount < finalCopiesAmount);
    }

    @Test
    public void removeOneCopy() {
        final int initCopiesAmount = testBook.getCopiesAmount();
        testService.removeOneCopy(testBook);
        final Book foundBook = testService.findById(testBook.getId());
        final int finalCopiesAmount = foundBook.getCopiesAmount();

        assertTrue(initCopiesAmount > finalCopiesAmount);
    }

    @Test
    public void register_mustReturnBookWithId() {
        assertNotNull(testBook);
        assertNotNull(testBook.getId());
    }

    @Test
    public void delete_mustDeleteBook() {
        testService.delete(testBook);
        final List<Book> allBooks = testService.findAll();
        assertFalse(allBooks.contains(testBook));
    }

    @Test
    public void findByGenre_mustReturnListOfBooksWithPassedGenre() {
        final List<Book> foundBooks = testService.findByGenre(testBook.getGenre());
        assertNotNull(foundBooks);
        if (!foundBooks.isEmpty()) {
            for (Book foundBook : foundBooks) {
                assertEquals(testBook.getGenre(), foundBook.getGenre());
            }
        }
    }

    @Test
    public void findByName_mustReturnCollectionWithSavedBook_whenSavedBookNameWasPassed() {
        final List<Book> booksByName = testService.findByName(testBook.getName());
        assertNotNull(booksByName);
        assertTrue(booksByName.contains(testBook));
    }

    @Test
    public void findByName_mustReturnCollectionWithoutDeletedBook_whenDeletedBookNameWasPassed() {
        testService.delete(testBook);
        final List<Book> booksByName = testService.findByName(testBook.getName());
        assertNotNull(booksByName);
        assertFalse(booksByName.contains(testBook));
    }
}