package by.epam.jwd.web.service;

import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.connectionPool.ConnectionPoolInitializationException;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Comment;
import by.epam.jwd.web.model.Genre;
import by.epam.jwd.web.model.User;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class SimpleCommentServiceTest {
    private final CommentService testService = CommentService.getInstance();
    private User testUser = new User("login", "login");
    private Book testBook = new Book("book", "book", Genre.FANTASY, 1, "text");
    private Comment testComment;

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
        testComment = new Comment(testUser, testBook, "text text");
        testComment = testService.save(testComment);
    }

    @After
    public void tearDown() {
        testService.delete(testComment);
        SimpleUserService.getInstance().delete(testUser);
        SimpleBookService.getInstance().delete(testBook);
    }

    @Test
    public void getInstance_mustReturnSameInstanceAsTestService() {
        final SimpleCommentService instance = SimpleCommentService.getInstance();
        assertNotNull(instance);
        assertSame(testService, instance);
    }

    @Test
    public void findByBook_mustReturnCommentsList_whenTestCommentBookPassed() {
        final List<Comment> foundComments = testService.findByBook(testComment.getBook());
        assertNotNull(foundComments);
        assertTrue(foundComments.contains(testComment));
    }

    @Test
    public void findByBook_mustReturnCommentsListWithoutTestComment_whenThereIsNoTestComment() {
        testService.delete(testComment);
        final List<Comment> foundComments = testService.findByBook(testComment.getBook());
        assertNotNull(foundComments);
        assertFalse(foundComments.contains(testComment));
    }

    @Test
    public void findByBooks_mustReturnCommentsListWithTestComment() {
        List<Book> books = Collections.singletonList(testComment.getBook());
        final List<Comment> foundComments = testService.findByBooks(books);
        assertTrue(foundComments.contains(testComment));
    }

    @Test
    public void findAll_mustReturnNotNullCommentsList() {
        final List<Comment> allComments = testService.findAll();
        assertNotNull(allComments);
    }

    @Test
    public void findPage_mustReturnNotNullPage() {
        final int pagesAmount = testService.getPagesAmount();
        final List<Comment> commentsPageList = testService.findPage(pagesAmount);
        assertNotNull(commentsPageList);
    }

    @Test
    public void findById_mustReturnTestComment_whenTestCommentIdPassed() {
        final Comment foundComment = testService.findById(testComment.getId());
        assertNotNull(foundComment);
        assertEquals(testComment, foundComment);
    }

    @Test(expected = ServiceException.class)
    public void findById_mustThrowException_whenThereIsNoComment() {
        testService.delete(testComment);
        testService.findById(testComment.getId());
    }

    @Test
    public void register_mustAssignIdToTestComment() {
        assertNotNull(testComment);
        assertNotNull(testComment.getId());
    }

    @Test
    public void delete_mustDeleteTestComment_whenTestCommentIdPassed() {
        testService.delete(testComment);
        final List<Comment> allComments = testService.findAll();
        assertFalse(allComments.contains(testComment));
    }

    @Test
    public void getPagesAmount_mustReturnNotNegativeNumber() {
        final int pagesAmount = testService.getPagesAmount();
        assertTrue(pagesAmount >= 0);
    }
}