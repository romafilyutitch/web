package by.epam.jwd.web.dao;

import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.connectionPool.ConnectionPoolInitializationException;
import by.epam.jwd.web.dao.DAOException;
import by.epam.jwd.web.dao.MySQLGenreDao;

import by.epam.jwd.web.model.BookGenre;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

public class MySQLGenreDaoTest {
    private static final ConnectionPool pool = ConnectionPool.getConnectionPool();
    private final MySQLGenreDao testDao = MySQLGenreDao.getInstance();
    private BookGenre testGenre = new BookGenre("Horror");

    @BeforeClass
    public static void setUp() throws ConnectionPoolInitializationException {
        pool.init();
    }

    @AfterClass
    public static void tearDown() {
        pool.destroy();
    }

    @Before
    public void saveTestGenre() throws DAOException {
        testGenre = testDao.save(testGenre);
    }

    @Test
    public void save() {
        Assert.assertNotNull(testGenre);
        Assert.assertNotNull(testGenre.getId());
    }

    @Test
    public void findAll() throws DAOException {
        final List<BookGenre> allGenres = testDao.findAll();
        Assert.assertNotNull(allGenres);
        Assert.assertFalse(allGenres.isEmpty());
        Assert.assertTrue(allGenres.contains(testGenre));
    }

    @Test
    public void findById() throws DAOException {
        final Optional<BookGenre> byId = testDao.findById(testGenre.getId());
        Assert.assertEquals(testGenre, byId.get());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void update() throws DAOException {
        testDao.update(testGenre);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void delete() throws DAOException {
        testDao.delete(testGenre.getId());
    }
}