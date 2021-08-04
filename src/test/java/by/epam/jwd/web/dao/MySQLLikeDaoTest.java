package by.epam.jwd.web.dao;

import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.dao.mysql.MySQLBookDao;
import by.epam.jwd.web.dao.mysql.MySQLLikeDao;
import by.epam.jwd.web.dao.mysql.MySQLUserDao;
import by.epam.jwd.web.exception.ConnectionPoolInitializationException;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Like;
import by.epam.jwd.web.model.User;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class MySQLLikeDaoTest {
    private static final ConnectionPool POOL = ConnectionPool.getConnectionPool();
    private final User testUser = MySQLUserDao.getInstance().findAll().stream().findAny().get();
    private final Book testBook = MySQLBookDao.getInstance().findAll().stream().findAny().get();
    private Like testLike = new Like(testUser, testBook);
    private LikeDao testDao = MySQLLikeDao.getInstance();

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
        testLike = testDao.save(testLike);
    }

    @After
    public void tearDown() {
        testDao.delete(testLike.getId());
    }


    @Test
    public void save_mustAssignIdToSavedLike() {
        assertNotNull("Save must return not null like", testLike);
        assertNotNull("Saved like id must be not null", testLike.getId());
    }

    @Test
    public void findAll_mustReturnNotNullList() {
        final List<Like> allLikes = testDao.findAll();
        assertNotNull("All likes list must be not null", allLikes);
    }

    @Test
    public void findById_mustReturnNotNullOptional() {
        final Optional<Like> optionalLike = testDao.findById(testLike.getId());
        assertNotNull("Return value must be not null", optionalLike);
    }

    @Test
    public void findById_mustReturnSavedLike_whenSavedLikeIdPassed() {
        final Optional<Like> optionalLike = testDao.findById(testLike.getId());
        assertTrue("Optional like must be not empty", optionalLike.isPresent());
        assertEquals("Found like must be equal to saved like", testLike, optionalLike.get());
    }

    @Test
    public void findById_mustReturnEmptyOptionalLike_whenThereIsNoLikeWithPassedId() {
        testDao.delete(testLike.getId());
        final Optional<Like> optionalLike = testDao.findById(testLike.getId());
        assertFalse("Optional like must be empty", optionalLike.isPresent());
    }

    @Test
    public void update() {
        final Book newBook = MySQLBookDao.getInstance().findAll().stream().findAny().get();
        testLike = new Like(testLike.getId(), testLike.getUser(), newBook);
        final Like updatedLike = testDao.update(testLike);
        assertNotNull("Updated like must be not null", updatedLike);
        assertEquals("Updated like book must be equal to new book", newBook, updatedLike.getBook());
        assertEquals("Updated like must be equal to saved like", testLike, updatedLike);
    }

    @Test
    public void delete_mustDeleteTestLike() {
        testDao.delete(testLike.getId());
        final List<Like> allLikes = testDao.findAll();
        assertFalse("All likes list must not contain deleted like", allLikes.contains(testLike));
    }

    @Test
    public void findPage_mustReturnNotNullPage() {
        final int pagesAmount = testDao.getPagesAmount();
        final List<Like> foundPage = testDao.findPage(pagesAmount);
        assertNotNull("Found page must be not null", foundPage);
    }

    @Test
    public void getRowsAmount_mustReturnNotNegativeNumber() {
        final int rowsAmount = testDao.getRowsAmount();
        assertTrue("Returned value must be not negative", rowsAmount >= 0);
    }

    @Test
    public void getPagesAmount_musReturnNotNegativeNumber() {
        final int pagesAmount = testDao.getPagesAmount();
        assertTrue("Returned value must be not negative", pagesAmount >= 0);
    }

    @Test
    public void findByUserAndBook_mustReturnSavedTestLike_whenSavedTestLikeUserAndBookPassed() {
        final Optional<Like> optionalLike = testDao.findByUserAndBook(testUser, testBook);
        assertNotNull("Optional like must be not null", optionalLike);
        assertTrue("Optional like must have like", optionalLike.isPresent());
        assertEquals("Found like must be equal to test like", testLike, optionalLike.get());
    }

    @Test
    public void findByUserAndBook_mustReturnEmptyOptionalInstance_whenThereIsNoLikeWithPassedUserAndBook() {
        testDao.delete(testLike.getId());
        final Optional<Like> optionalLike = testDao.findByUserAndBook(testUser, testBook);
        assertNotNull("Optional like must be not null", optionalLike);
        assertFalse("Optional like must be empty", optionalLike.isPresent());
    }
}