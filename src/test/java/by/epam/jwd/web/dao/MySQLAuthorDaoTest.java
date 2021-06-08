package by.epam.jwd.web.dao;

import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.connectionPool.ConnectionPoolInitializationException;
import by.epam.jwd.web.dao.DAOException;
import by.epam.jwd.web.dao.MySQLAuthorDao;

import by.epam.jwd.web.model.BookAuthor;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

public class MySQLAuthorDaoTest {
    private static final ConnectionPool pool = ConnectionPool.getConnectionPool();
    private final MySQLAuthorDao testDao = MySQLAuthorDao.getInstance();
    private BookAuthor testAuthor = new BookAuthor("James");

    @BeforeClass
    public static void setUp() throws ConnectionPoolInitializationException {
        pool.init();
    }

    @AfterClass
    public static void tearDown() {
        pool.destroy();
    }

    @Before
    public void saveTestAuthor() throws DAOException {
        testAuthor = testDao.save(testAuthor);
    }

    @Test
    public void save() {
        Assert.assertNotNull(testAuthor);
        Assert.assertNotNull(testAuthor.getId());
    }

    @Test
    public void findAll() throws DAOException {
        final List<BookAuthor> allAuthors = testDao.findAll();
        Assert.assertNotNull(allAuthors);
        Assert.assertFalse(allAuthors.isEmpty());
        Assert.assertTrue(allAuthors.contains(testAuthor));
    }

    @Test
    public void findById() throws DAOException {
        final Optional<BookAuthor> byId = testDao.findById(testAuthor.getId());

        Assert.assertEquals(testAuthor, byId.get());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void update() throws DAOException {
        testDao.update(testAuthor);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void delete() throws DAOException {
        testDao.delete(testAuthor.getId());
    }
}