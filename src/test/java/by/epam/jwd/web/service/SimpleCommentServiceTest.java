package by.epam.jwd.web.service;

import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.exception.ConnectionPoolInitializationException;
import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Comment;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.service.impl.SimpleCommentService;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class SimpleCommentServiceTest {
    private static final ConnectionPool POOL = ConnectionPool.getConnectionPool();
    private User testUser = ServiceFactory.getInstance().getUserService().findAll().stream().findAny().get();
    private Book testBook = ServiceFactory.getInstance().getBookService().findAll().stream().findAny().get();
    private Comment testComment = new Comment(testUser, testBook, LocalDate.now(), "Test text for test");
    private final CommentService testService = ServiceFactory.getInstance().getCommentService();

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
        testComment = testService.save(testComment);
    }

    @After
    public void tearDown() throws Exception {
        testService.delete(testComment.getId());
    }

    @Test
    public void getInstance_mustReturnSameInstanceAsTestService() {
        final SimpleCommentService instance = SimpleCommentService.getInstance();
        assertNotNull("Returned instance must be not null", instance);
        assertSame("Returned instance must be same as test service", testService, instance);
    }

    @Test
    public void findByBook_mustReturnCommentsList_whenTestCommentBookPassed() {
        final List<Comment> foundComments = testService.findByBook(testComment.getBook());
        assertNotNull("Found comments list must be not null", foundComments);
        assertTrue("Found comments list must contain test comment", foundComments.contains(testComment));
    }

    @Test
    public void findByBook_mustReturnCommentsListWithoutTestComment_whenThereIsNoTestComment() {
        testService.delete(testComment.getId());
        final List<Comment> foundComments = testService.findByBook(testComment.getBook());
        assertNotNull("Found comments list must be not null", foundComments);
        assertFalse("Found comments list must not contain test comment", foundComments.contains(testComment));
    }

    @Test
    public void findAll_mustReturnNotNullCommentsList() {
        final List<Comment> allComments = testService.findAll();
        assertNotNull("All comments list must be not null", allComments);
    }

    @Test
    public void findPage_mustReturnNotNullPage() {
        final int pagesAmount = testService.getPagesAmount();
        final List<Comment> commentsPageList = testService.findPage(pagesAmount);
        assertNotNull("Comments page list must be not null", commentsPageList);
    }

    @Test
    public void findById_mustReturnTestComment_whenTestCommentIdPassed() {
        final Comment foundComment = testService.findById(testComment.getId());
        assertNotNull("Found comment must be not null", foundComment);
        assertEquals("Found comment must be equal to test comment", testComment, foundComment);
    }

    @Test(expected = ServiceException.class)
    public void findById_mustThrowException_whenThereIsNoComment() {
        testService.delete(testComment.getId());
        final Comment foundComment = testService.findById(testComment.getId());
    }

    @Test
    public void register_mustAssignIdToTestComment() {
        assertNotNull("Registered comment must be not null", testComment);
        assertNotNull("Registered comment id must be not null", testComment.getId());
    }

    @Test
    public void delete_mustDeleteTestComment_whenTestCommentIdPassed() {
        testService.delete(testComment.getId());
        final List<Comment> allComments = testService.findAll();
        assertFalse("All comments list must not contain deleted comment", allComments.contains(testComment));
    }

    @Test
    public void getPagesAmount_mustReturnNotNegativeNumber() {
        final int pagesAmount = testService.getPagesAmount();
        assertTrue("Pages amount must be not negative", pagesAmount >= 0);
    }
}