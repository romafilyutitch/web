package by.epam.jwd.web.dao;

import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.exception.ConnectionPoolInitializationException;
import by.epam.jwd.web.model.Author;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;
import by.epam.jwd.web.model.User;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class MySQLBookDaoTest {
    private static final ConnectionPool POOL = ConnectionPool.getConnectionPool();
    private final MySQLBookDao testDao = MySQLBookDao.getInstance();
    private final Author testAuthor = MySQLAuthorDao.getInstance().findAll().stream().findAny().get();
    private final User testUser = MySQLUserDao.getInstance().findAll().stream().findAny().get();
    private Book testBook = new Book("Test Book", testAuthor , Genre.SCIENCE, LocalDate.now(), 100, 1, "Test book for unit test", 0);

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
        testBook = testDao.save(testBook);
    }

    @After
    public void tearDown() throws Exception {
        testDao.delete(testBook.getId());
    }

    @Test
    public void save_mustAssignIdToSavedBook() {
        assertNotNull("Save must return not null book", testBook);
        assertNotNull("Saved book id must be not null", testBook.getId());
    }

    @Test
    public void findAll_mustReturnNotNullList() {
        final List<Book> allBooks = testDao.findAll();
        assertNotNull("All books list must be not null", allBooks);
    }

    @Test
    public void findById_mustReturnNotNullOptional() {
        final Optional<Book> optionalBook = testDao.findById(testBook.getId());
        assertNotNull("Returned value must be not null", optionalBook);
    }

    @Test
    public void findById_mustReturnSavedBook_whenSavedBookIdPassed() {
        final Optional<Book> optionalBook = testDao.findById(testBook.getId());
        assertTrue("Optional book must be not empty", optionalBook.isPresent());
        assertEquals("Found book must be equal to saved book", testBook, optionalBook.get());
    }

    @Test
    public void findById_mustReturnEmptyOptionalBook_whenThereIsNoBookWithPassedId() {
        testDao.delete(testBook.getId());
        final Optional<Book> optionalBook = testDao.findById(testBook.getId());
        assertFalse("Optional book must be empty", optionalBook.isPresent());
    }

    @Test
    public void update_mustUpdateTestBookDescription() {
        String updatedDescription = "Updated description";
        testBook = new Book(testBook.getId(), testBook.getName(), testBook.getAuthor(), testBook.getGenre(), testBook.getDate(), testBook.getPagesAmount(), testBook.getCopiesAmount(), updatedDescription, testBook.getLikes());
        final Book updatedBook = testDao.update(testBook);
        assertNotNull("Updated book must be not null", updatedBook);
        assertEquals("Updated book description must be equal to updated string", updatedDescription, updatedBook.getDescription());
        assertEquals("Updated book must be equal to saved book", testBook, updatedBook);
    }

    @Test
    public void delete_mustDeleteTestBook() {
        testDao.delete(testBook.getId());
        final List<Book> allBooks = testDao.findAll();
        assertFalse("All books list must not contain deleted book", allBooks.contains(testBook));
    }

    @Test
    public void findPage_mustReturnNotNullPage() {
        final int pagesAmount = testDao.getPagesAmount();
        final List<Book> foundPage = testDao.findPage(pagesAmount);
        assertNotNull("Found page list must be not null", foundPage);
    }

    @Test
    public void getRowsAmount_mustReturnNotNegativeNumber() {
        final int rowsAmount = testDao.getRowsAmount();;
        assertTrue("Returned value must be not negative", rowsAmount >= 0);
    }

    @Test
    public void getPagesAmount_mustReturnNotNegativeNumber() {
        final int pagesAmount = testDao.getPagesAmount();
        assertTrue("Returned value must be not negative", pagesAmount >= 0);
    }

    @Test
    public void getInstance_mustReturnSameInstanceAsTestInstance() {
        final MySQLBookDao instance = MySQLBookDao.getInstance();
        assertSame("Returned instance must be same as test instance", testDao, instance);
    }

    @Test
    public void findByName_mustReturnSavedBook_whenSavedBookNameWasPassed() {
        final Optional<Book> optionalBook = testDao.findByName(testBook.getName());
        assertNotNull("Returned value must be not null", optionalBook);
        assertTrue("Saved book must be found by name", optionalBook.isPresent());
        assertEquals("Found book must be equal to test book", testBook, optionalBook.get());
    }

    @Test
    public void findByName_mustReturnEmptyOptionalBook_whenWrongNameWasPassed() {
        testDao.delete(testBook.getId());
        final Optional<Book> optionalBook = testDao.findByName(testBook.getName());
        assertNotNull("Returned value must be not null", optionalBook);
        assertFalse("Book by wrong name must not be found", optionalBook.isPresent());
    }

    @Test
    public void findByAuthorId_mustReturnNotNullListOfBooksWithSpecifiedAuthorId() {
        final List<Book> foundBooks = testDao.findByAuthorId(testBook.getAuthor().getId());
        assertNotNull("Returned value must be not null", foundBooks);
        for (Book foundBook : foundBooks) {
            assertEquals("Found book must have passed author id", testBook.getAuthor().getId(), foundBook.getAuthor().getId());
        }
    }

    @Test
    public void findByGenreId_mustReturnNotNullListOfBooksWithSpecifiedGenreId() {
        final List<Book> foundBooks = testDao.findByGenreId(testBook.getGenre().getId());
        assertNotNull("Returned value must be not null", foundBooks);
        for (Book foundBook : foundBooks) {
            assertEquals("Found book must have passed genre id", testBook.getGenre().getId(), foundBook.getGenre().getId());
        }
    }

    @Test
    public void addLike() {
        testDao.addLike(testBook.getId(), testUser.getId());
        assertTrue("Test book must be liked by user id", testDao.isLikedByUserWithId(testBook.getId(), testUser.getId()));
        testDao.removeLike(testBook.getId(), testUser.getId());
    }

    @Test
    public void removeLike() {
        testDao.addLike(testBook.getId(), testUser.getId());
        assertTrue("Test book must be liked by user id", testDao.isLikedByUserWithId(testBook.getId(), testUser.getId()));
        testDao.removeLike(testBook.getId(), testUser.getId());
        assertFalse("Test book must be not liked by user id", testDao.isLikedByUserWithId(testBook.getId(), testUser.getId()));
    }

    @Test
    public void isLikedByUserWithId_mustReturnFalse_whenBookNotLikedByUser() {
        assertFalse("Test book must be not liked by testUser", testDao.isLikedByUserWithId(testBook.getId(), testUser.getId()));
    }

    @Test
    public void isLikedByUserWithId_mustReturnTrue_whenBookLikedByUser() {
        testDao.addLike(testBook.getId(), testUser.getId());
        assertTrue("Test book must be liked by test User", testDao.isLikedByUserWithId(testBook.getId(), testUser.getId()));
        testDao.removeLike(testBook.getId(), testUser.getId());
    }
}