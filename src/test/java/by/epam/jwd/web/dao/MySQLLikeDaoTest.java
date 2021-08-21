package by.epam.jwd.web.dao;

import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.dao.api.LikeDao;
import by.epam.jwd.web.dao.mysql.MySQLBookDao;
import by.epam.jwd.web.dao.mysql.MySQLLikeDao;
import by.epam.jwd.web.dao.mysql.MySQLUserDao;
import by.epam.jwd.web.exception.ConnectionPoolInitializationException;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;
import by.epam.jwd.web.model.Like;
import by.epam.jwd.web.model.User;
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

public class MySQLLikeDaoTest {
    private final LikeDao testDao = MySQLLikeDao.getInstance();
    private User testUser = new User("test user", "test user");
    private Book testBook = new Book("Test book", "test book", Genre.FANTASY, 1, "text");
    private Like testLike;

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
        testUser = MySQLUserDao.getInstance().save(testUser);
        testBook = MySQLBookDao.getInstance().save(testBook);
        testLike = new Like(testUser, testBook);
        testLike = testDao.save(testLike);
    }

    @After
    public void tearDown() {
        testDao.delete(testLike.getId());
        MySQLUserDao.getInstance().delete(testUser.getId());
        MySQLBookDao.getInstance().delete(testBook.getId());
    }


    @Test
    public void save_mustAssignIdToSavedLike() {
        assertNotNull(testLike);
        assertNotNull(testLike.getId());
    }

    @Test
    public void findAll_mustReturnNotNullList() {
        final List<Like> allLikes = testDao.findAll();
        assertNotNull(allLikes);
    }

    @Test
    public void findById_mustReturnNotNullOptional() {
        final Optional<Like> optionalLike = testDao.findById(testLike.getId());
        assertNotNull(optionalLike);
    }

    @Test
    public void findById_mustReturnSavedLike_whenSavedLikeIdPassed() {
        final Optional<Like> optionalLike = testDao.findById(testLike.getId());
        assertTrue(optionalLike.isPresent());
        assertEquals(testLike, optionalLike.get());
    }

    @Test
    public void findById_mustReturnEmptyOptionalLike_whenThereIsNoLikeWithPassedId() {
        testDao.delete(testLike.getId());
        final Optional<Like> optionalLike = testDao.findById(testLike.getId());
        assertFalse(optionalLike.isPresent());
    }

    @Test
    public void update() {
        final Book newBook = new Book("B", "B", Genre.FICTION, 1, "B");
        final Book savedBook = MySQLBookDao.getInstance().save(newBook);
        testLike = new Like(testLike.getId(), testLike.getUser(), savedBook);
        final Like updatedLike = testDao.update(testLike);
        assertNotNull(updatedLike);
        assertEquals(savedBook, updatedLike.getBook());
        assertEquals(testLike, updatedLike);
    }

    @Test
    public void delete_mustDeleteTestLike() {
        testDao.delete(testLike.getId());
        final List<Like> allLikes = testDao.findAll();
        assertFalse(allLikes.contains(testLike));
    }

    @Test
    public void findPage_mustReturnNotNullPage() {
        final int pagesAmount = testDao.getPagesAmount();
        final List<Like> foundPage = testDao.findPage(pagesAmount);
        assertNotNull(foundPage);
    }

    @Test
    public void getRowsAmount_mustReturnNotNegativeNumber() {
        final int rowsAmount = testDao.getRowsAmount();
        assertTrue(rowsAmount >= 0);
    }

    @Test
    public void getPagesAmount_musReturnNotNegativeNumber() {
        final int pagesAmount = testDao.getPagesAmount();
        assertTrue(pagesAmount >= 0);
    }

    @Test
    public void findByUserAndBook_mustReturnSavedTestLike_whenSavedTestLikeUserAndBookPassed() {
        final Optional<Like> optionalLike = testDao.findByUserAndBook(testUser, testBook);
        assertNotNull(optionalLike);
        assertTrue(optionalLike.isPresent());
        assertEquals(testLike, optionalLike.get());
    }

    @Test
    public void findByUserAndBook_mustReturnEmptyOptionalInstance_whenThereIsNoLikeWithPassedUserAndBook() {
        testDao.delete(testLike.getId());
        final Optional<Like> optionalLike = testDao.findByUserAndBook(testUser, testBook);
        assertNotNull(optionalLike);
        assertFalse(optionalLike.isPresent());
    }
}