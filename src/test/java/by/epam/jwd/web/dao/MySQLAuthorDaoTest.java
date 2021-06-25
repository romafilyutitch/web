package by.epam.jwd.web.dao;

import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.exception.ConnectionPoolInitializationException;
import by.epam.jwd.web.model.Author;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;

import java.util.List;
import java.util.Optional;

public class MySQLAuthorDaoTest {
    private static final ConnectionPool POOL = ConnectionPool.getConnectionPool();
    private final MySQLAuthorDao testDao = MySQLAuthorDao.getInstance();
    private Author testAuthor = new Author("test");

    @BeforeClass
    public static void setUp() throws ConnectionPoolInitializationException {
        POOL.init();
    }

    @AfterClass
    public static void tearDown() {
        POOL.destroy();
    }

    @Before
    public void saveAuthor() {
        testAuthor = testDao.save(testAuthor);
    }

    @After
    public void deleteAuthor() {
        testDao.delete(testAuthor.getId());
    }


    @Test
    public void save() {
        Assert.assertNotNull(testAuthor);
        final Optional<Author> optionalAuthor = testDao.findById(testAuthor.getId());
        if (!optionalAuthor.isPresent()) {
            Assert.fail("Saved author was not found by id");
        }
        Assert.assertEquals("Saved author not equals found author by id", testAuthor, optionalAuthor.get());
    }

    @Test
    public void findAll() {
        final List<Author> allAuthors = testDao.findAll();
        Assert.assertNotNull("All authors list is null", allAuthors);
    }

    @Test
    public void findById() {
        final Optional<Author> optionalAuthor = testDao.findById(testAuthor.getId());
        Assert.assertNotNull("Optional author is null", optionalAuthor);
    }

    @Test
    public void update() {
        Author author = new Author("update");
        author = testDao.save(author);
        author = author.updateName("UPDATE");
        final Author updatedAuthor = testDao.update(author);
        testDao.delete(updatedAuthor.getId());
        Assert.assertEquals("test author is not equals to updated author", author, updatedAuthor);
    }

    @Test
    public void delete() {
        testDao.delete(testAuthor.getId());
        final Optional<Author> optionalAuthor = testDao.findById(testAuthor.getId());
        if (optionalAuthor.isPresent()) {
            Assert.fail("Author was not deleted");
        }
    }

    @Test
    public void findPage() {
        final int pagesAmount = testDao.getPagesAmount();
        final List<Author> page = testDao.findPage(pagesAmount);
        Assert.assertNotNull("page is null", page);
    }

    @Test
    public void getRowsAmount() {
        final int rowsAmount = testDao.getRowsAmount();
        if (rowsAmount == 0) {
            Assert.fail("author was saved but rows amount was not changed");
        }
    }

    @Test
    public void getPagesAmount() {
        final int pagesAmount = testDao.getPagesAmount();
        if (pagesAmount < 0) {
            Assert.fail("Pages amount is negative");
        }
    }

    @Test
    public void getInstance() {
        final MySQLAuthorDao instance = MySQLAuthorDao.getInstance();
        Assert.assertNotNull("Instance is null", instance);
        Assert.assertEquals("test author dao is not equal to instance", testDao, instance);
    }

    @Test
    public void getByName() {
        final Optional<Author> optionalAuthor = testDao.getByName(testAuthor.getName());
        if (!optionalAuthor.isPresent()) {
            Assert.fail("Saved author was not found by id");
        }
    }
}