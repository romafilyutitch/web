package by.epam.jwd.web.service;

import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.connectionPool.ConnectionPoolInitializationException;
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
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class SimpleLikeServiceTest {
    private final LikeService testService = LikeService.getInstance();
    private User testUser = new User("login", "user");
    private Book testBook = new Book("test book", "test book", Genre.FANTASY, 1, "text");
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
        testUser = SimpleUserService.getInstance().save(testUser);
        testBook = SimpleBookService.getInstance().save(testBook);
        testLike = new Like(testUser, testBook);
        testLike = testService.save(testLike);
    }

    @After
    public void tearDown() {
        testService.delete(testLike);
        SimpleUserService.getInstance().delete(testUser);
        SimpleBookService.getInstance().delete(testBook);
    }

    @Test
    public void getInstance_mustReturnSameInstanceAsTestService() {
        final SimpleLikeService instance = SimpleLikeService.getInstance();
        assertNotNull(instance);
        assertSame(testService, instance);
    }

    @Test
    public void findByUserAndBook_mustReturnTestLike_whenTestLikeUserAndBookPassed() {
        final Optional<Like> optionalLike = testService.findByUserAndBook(testLike.getUser(), testLike.getBook());
        assertNotNull(optionalLike);
        assertTrue(optionalLike.isPresent());
        assertEquals(testLike, optionalLike.get());
    }

    @Test
    public void findByUserAndBook_mustReturnEmptyOptional_whenThereIsNoLikeWithPassedBookAndUser() {
        testService.delete(testLike);
        final Optional<Like> optionalLike = testService.findByUserAndBook(testLike.getUser(), testLike.getBook());
        assertNotNull(optionalLike);
        assertFalse(optionalLike.isPresent());
    }

    @Test
    public void findAll_mustReturnNotNullLikeList() {
        final List<Like> allLikes = testService.findAll();
        assertNotNull(allLikes);
    }

    @Test
    public void findPage_mustReturnNotNullLikePageList() {
        final int pagesAmount = testService.getPagesAmount();
        final List<Like> likesPage = testService.findPage(pagesAmount);
        assertNotNull(likesPage);
    }

    @Test
    public void findById_mustReturnTestLike_whenTestLikeIdPassed() {
        final Like foundLike = testService.findById(testLike.getId());
        assertNotNull(foundLike);
        assertEquals(testLike, foundLike);
    }

    @Test(expected = ServiceException.class)
    public void findById_mustThrowException_whenThereIsNotLikeWithPassedId() {
        testService.delete(testLike);
        testService.findById(testLike.getId());
    }

    @Test
    public void register_mustAssignIdToTestLike() {
        assertNotNull(testLike);
        assertNotNull(testLike.getId());
    }

    @Test
    public void delete_mustDeleteTestLike() {
        testService.delete(testLike);
        final List<Like> allLikes = testService.findAll();
        assertNotNull(allLikes);
        assertFalse(allLikes.contains(testLike));
    }

    @Test
    public void getPagesAmount_mustReturnNotNegativeNumber() {
        final int pagesAmount = testService.getPagesAmount();
        assertTrue(pagesAmount >= 0);
    }
}