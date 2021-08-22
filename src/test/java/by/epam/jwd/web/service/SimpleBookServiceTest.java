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
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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
        for (Book foundBook : foundBooks) {
            if (!foundBook.getGenre().equals(testBook.getGenre())) {
                fail();
            }
        }
    }

    @Test
    public void findByName_mustReturnBookWithName_whenSavedBookNameWasPassed() {
        final Optional<Book> optionalBook = testService.findByName(testBook.getName());
        assertNotNull(optionalBook);
        assertTrue(optionalBook.isPresent());
        assertEquals(optionalBook.get().getName(), testBook.getName());
    }

    @Test
    public void findByName_mustReturnEmptyOptionalBook_whenDeletedBookNameWasPassed() {
        testService.delete(testBook);
        final Optional<Book> optionalBook = testService.findByName(testBook.getName());
        assertNotNull(optionalBook);
        assertFalse(optionalBook.isPresent());
    }

    @Test
    public void findWhereNameLike_mustReturnCollectionOfBooksWithMatchesNames() {
        final List<Book> booksWithMatchesNames = testService.findWhereNameLike(testBook.getName());
        assertNotNull(booksWithMatchesNames);
        for (Book book : booksWithMatchesNames) {
            if (!book.getName().contains(testBook.getName())) {
                fail();
            }
        }
    }
}