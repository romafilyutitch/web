package by.epam.jwd.web.dao;

import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.connectionPool.ConnectionPoolInitializationException;
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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class MySQLBookDaoTest {
    private final MySQLBookDao testDao = MySQLBookDao.getInstance();
    private Book testBook = new Book("Test Book", "author", Genre.SCIENCE, LocalDate.now(), 100, "Test book for unit test");

    @BeforeClass
    public static void initPool() throws ConnectionPoolInitializationException {
        ConnectionPool.getConnectionPool().init();
    }

    @AfterClass
    public static void destroyPool() {
        ConnectionPool.getConnectionPool().destroy();
    }

    @Before
    public void setUp() {
        testBook = testDao.save(testBook);
    }

    @After
    public void tearDown() {
        testDao.delete(testBook.getId());
    }

    @Test
    public void save_mustAssignIdToSavedBook() {
        assertNotNull(testBook);
        assertNotNull(testBook.getId());
    }

    @Test
    public void findAll_mustReturnNotNullList() {
        final List<Book> allBooks = testDao.findAll();
        assertNotNull(allBooks);
    }

    @Test
    public void findById_mustReturnNotNullOptional() {
        final Optional<Book> optionalBook = testDao.findById(testBook.getId());
        assertNotNull(optionalBook);
    }

    @Test
    public void findById_mustReturnSavedBook_whenSavedBookIdPassed() {
        final Optional<Book> optionalBook = testDao.findById(testBook.getId());
        assertTrue(optionalBook.isPresent());
        assertEquals(testBook, optionalBook.get());
    }

    @Test
    public void findById_mustReturnEmptyOptionalBook_whenThereIsNoBookWithPassedId() {
        testDao.delete(testBook.getId());
        final Optional<Book> optionalBook = testDao.findById(testBook.getId());
        assertFalse(optionalBook.isPresent());
    }

    @Test
    public void update_mustUpdateTestBookText() {
        String updatedText = "Updated text";
        testBook = new Book(testBook.getId(), testBook.getName(), testBook.getAuthor(), testBook.getGenre(), testBook.getDate(), testBook.getPagesAmount(), testBook.getCopiesAmount(), updatedText, testBook.getLikesAmount(), testBook.getCommentsAmount());
        final Book updatedBook = testDao.update(testBook);
        assertNotNull(updatedBook);
        assertEquals(updatedText, updatedBook.getText());
        assertEquals(testBook, updatedBook);
    }

    @Test
    public void delete_mustDeleteTestBook() {
        testDao.delete(testBook.getId());
        final List<Book> allBooks = testDao.findAll();
        assertFalse(allBooks.contains(testBook));
    }

    @Test
    public void findPage_mustReturnNotNullPage() {
        final int pagesAmount = testDao.getPagesAmount();
        final List<Book> foundPage = testDao.findPage(pagesAmount);
        assertNotNull(foundPage);
    }

    @Test
    public void getRowsAmount_mustReturnNotNegativeNumber() {
        final int rowsAmount = testDao.getRowsAmount();
        assertTrue(rowsAmount >= 0);
    }

    @Test
    public void getPagesAmount_mustReturnNotNegativeNumber() {
        final int pagesAmount = testDao.getPagesAmount();
        assertTrue(pagesAmount >= 0);
    }

    @Test
    public void getInstance_mustReturnSameInstanceAsTestInstance() {
        final MySQLBookDao instance = MySQLBookDao.getInstance();
        assertSame(testDao, instance);
    }

    @Test
    public void findByName_mustReturnCollectionWithSavedBook_whenSavedBookNameWasPassed() {
        final List<Book> booksByName = testDao.findByName(testBook.getName());
        assertNotNull(booksByName);
        assertTrue(booksByName.contains(testBook));
    }

    @Test
    public void findByName_mustReturnCollectionWithoutDeletedBook_whenDeletedBookNameWasPassed() {
        testDao.delete(testBook.getId());
        final List<Book> booksByName = testDao.findByName(testBook.getName());
        assertNotNull(booksByName);
        assertFalse(booksByName.contains(testBook));
    }

    @Test
    public void findByAuthorName_mustReturnNotNullListOfBooksWithSpecifiedAuthorName() {
        final List<Book> foundBooks = testDao.findByAuthorName(testBook.getAuthor());
        assertNotNull(foundBooks);
        for (Book foundBook : foundBooks) {
            assertEquals(testBook.getAuthor(), foundBook.getAuthor());
        }
    }

    @Test
    public void findByGenreId_mustReturnNotNullListOfBooksWithSpecifiedGenreId() {
        final List<Book> foundBooks = testDao.findByGenre(testBook.getGenre());
        assertNotNull(foundBooks);
        for (Book foundBook : foundBooks) {
            assertEquals(testBook.getGenre().getId(), foundBook.getGenre().getId());
        }
    }
}