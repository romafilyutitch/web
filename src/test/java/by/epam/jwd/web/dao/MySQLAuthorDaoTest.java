package by.epam.jwd.web.dao;

import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.dao.mysql.MySQLAuthorDao;
import by.epam.jwd.web.exception.ConnectionPoolInitializationException;
import by.epam.jwd.web.model.Author;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class MySQLAuthorDaoTest {
    private static final ConnectionPool POOL = ConnectionPool.getConnectionPool();
    private final MySQLAuthorDao testDao = MySQLAuthorDao.getInstance();
    private Author testAuthor = new Author("test");

    @BeforeClass
    public static void initPool() throws ConnectionPoolInitializationException {
        POOL.init();
    }

    @AfterClass
    public static void destroyPool() {
        POOL.destroy();
    }

    @Before
    public void setUp() {
        testAuthor = testDao.save(testAuthor);
    }

    @After
    public void tearDown() {
        testDao.delete(testAuthor.getId());
    }


    @Test
    public void save_mustReturnAssignIdToSavedAuthor() {
        assertNotNull("Saved test author must be not null",testAuthor);
        assertNotNull("Saved test author must have not null id",testAuthor.getId());
    }

    @Test
    public void findAll_mustReturnNotNullList() {
        final List<Author> allAuthors = testDao.findAll();
        assertNotNull("All authors list must be not null",allAuthors);
    }

    @Test
    public void findById_mustReturnSavedAuthor_whenSavedAuthorIdPassed() {
        final Optional<Author> optionalAuthor = testDao.findById(testAuthor.getId());
        assertNotNull("Optional author must be not null",optionalAuthor);
        assertTrue("Optional author must be not empty",optionalAuthor.isPresent());
        assertEquals("Found author must be equal to test author",testAuthor, optionalAuthor.get());
    }

    @Test
    public void findById_mustReturnEmptyAuthor_whenThereIsNotAuthorWithPassedId() {
        testDao.delete(testAuthor.getId());
        final Optional<Author> optionalAuthor = testDao.findById(testAuthor.getId());
        assertNotNull("Optional author must be not null",optionalAuthor);
        assertFalse("Optional author must be empty",optionalAuthor.isPresent());
    }

    @Test
    public void update_mustUpdateAuthor() {
        final String updateName = "UPDATE";
        testAuthor = new Author(testAuthor.getId(), updateName);
        final Author updatedAuthor = testDao.update(testAuthor);
        assertNotNull("Returned value must be not null", updatedAuthor);
        assertEquals("Updated author must have updated name", updateName, updatedAuthor.getName());
        assertEquals("Updated author must be equal to saved author", testAuthor, updatedAuthor);
    }

    @Test
    public void delete_mustDeleteAuthor() {
        testDao.delete(testAuthor.getId());
        final List<Author> allAuthors = testDao.findAll();
        assertFalse("All authors list must not contain deleted author", allAuthors.contains(testAuthor));
    }

    @Test
    public void findPage_mustReturnNotNullPage() {
        final int pagesAmount = testDao.getPagesAmount();
        final List<Author> page = testDao.findPage(pagesAmount);
        assertNotNull("Found page must be not null", page);
    }

    @Test
    public void getRowsAmount_mustReturnNotNegativeNumber() {
        final int rowsAmount = testDao.getRowsAmount();
        assertTrue("Returned value must be not negative", rowsAmount >= 0);
    }

    @Test
    public void getPagesAmount_musReturnNotNegativeNumber() {
        final int pagesAmount = testDao.getPagesAmount();
        assertTrue("Returned value mut be not negative", pagesAmount >= 0);
    }

    @Test
    public void getInstance_mustReturnSameObject() {
        final MySQLAuthorDao instance = MySQLAuthorDao.getInstance();
        assertSame("Returned instance must be same as testDao", testDao, instance);
    }

    @Test
    public void getByName_mustReturnSavedAuthor_whenSavedAuthorNamePassed() {
        final Optional<Author> optionalAuthor = testDao.getByName(testAuthor.getName());
        assertNotNull("Returned value must be not null", optionalAuthor);
        assertTrue("Optional author must be not empty", optionalAuthor.isPresent());
        assertEquals("Found author must be equal to test author", testAuthor, optionalAuthor.get());
    }

    @Test
    public void getByName_mustReturnEmptyAuthor_whenWrongNamePassed() {
        testDao.delete(testAuthor.getId());
        final Optional<Author> optionalAuthor = testDao.getByName(testAuthor.getName());
        assertNotNull("Returned value must be not null", optionalAuthor);
        assertFalse("Optional author must empty", optionalAuthor.isPresent());
    }
}