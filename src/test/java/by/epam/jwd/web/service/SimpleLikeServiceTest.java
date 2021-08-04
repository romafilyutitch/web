package by.epam.jwd.web.service;

import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.exception.ConnectionPoolInitializationException;
import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Like;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.impl.SimpleLikeService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class SimpleLikeServiceTest {
    private static final ConnectionPool POOL = ConnectionPool.getConnectionPool();
    private final User testUser = ServiceFactory.getInstance().getUserService().findAll().stream().findAny().get();
    private final Book testBook = ServiceFactory.getInstance().getBookService().findAll().stream().findAny().get();
    private Like testLike = new Like(testUser, testBook);
    private final LikeService testService = ServiceFactory.getInstance().getLikeService();

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
        testLike = testService.save(testLike);
    }

    @After
    public void tearDown() throws Exception {
        testService.delete(testLike.getId());
    }

    @Test
    public void getInstance_mustReturnSameInstanceAsTestService() {
        final SimpleLikeService instance = SimpleLikeService.getInstance();
        assertNotNull("Returned instance must be not null", instance);
        assertSame("Returned instance must be same as test service", testService, instance);
    }

    @Test
    public void findByUserAndBook_mustReturnTestLike_whenTestLikeUserAndBookPassed() {
        final Optional<Like> optionalLike = testService.findByUserAndBook(testLike.getUser(), testLike.getBook());
        assertNotNull("Optional like must be not null", optionalLike);
        assertTrue("Optional like must be not empty", optionalLike.isPresent());
        assertEquals("Returned like must be equal to test like", testLike, optionalLike.get());
    }

    @Test
    public void findByUserAndBook_mustReturnEmptyOptional_whenThereIsNoLikeWithPassedBookAndUser() {
        testService.delete(testLike.getId());
        final Optional<Like> optionalLike = testService.findByUserAndBook(testLike.getUser(), testLike.getBook());
        assertNotNull("Optional like must be not null", optionalLike);
        assertFalse("Optional like must be empty", optionalLike.isPresent());
    }

    @Test
    public void findAll_mustReturnNotNullLikeList() {
        final List<Like> allLikes = testService.findAll();
        assertNotNull("All likes list must be not null", allLikes);
    }

    @Test
    public void findPage_mustReturnNotNullLikePageList() {
        final int pagesAmount = testService.getPagesAmount();
        final List<Like> likesPage = testService.findPage(pagesAmount);
        assertNotNull("Likes page list must be not null", likesPage);
    }

    @Test
    public void findById_mustReturnTestLike_whenTestLikeIdPassed() {
        final Like foundLike = testService.findById(testLike.getId());
        assertNotNull("Found like must be not null", foundLike);
        assertEquals("Found like must be equal to test like", testLike, foundLike);
    }

    @Test(expected = ServiceException.class)
    public void findById_mustThrowException_whenThereIsNotLikeWithPassedId() {
        testService.delete(testLike.getId());
        final Like foundLike = testService.findById(testLike.getId());
    }

    @Test
    public void register_mustAssignIdToTestLike() {
        assertNotNull("Test like must be not null", testLike);
        assertNotNull("Test like id must be not null", testLike.getId());
    }

    @Test
    public void delete_mustDeleteTestLike() {
        testService.delete(testLike.getId());
        final List<Like> allLikes = testService.findAll();
        assertNotNull("All likes list must be not null", allLikes);
        assertFalse("All likes list must not contain deleted like", allLikes.contains(testLike));
    }

    @Test
    public void getPagesAmount_mustReturnNotNegativeNumber() {
        final int pagesAmount = testService.getPagesAmount();
        assertTrue("Pages amount must be not negative", pagesAmount >= 0);
    }
}