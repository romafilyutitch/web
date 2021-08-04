package by.epam.jwd.web.dao;

import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.dao.mysql.MySQLBookDao;
import by.epam.jwd.web.dao.mysql.MySQLCommentDao;
import by.epam.jwd.web.dao.mysql.MySQLUserDao;
import by.epam.jwd.web.exception.ConnectionPoolInitializationException;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Comment;
import by.epam.jwd.web.model.User;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

public class MySQLCommentDaoTest {
    private static final ConnectionPool POOL = ConnectionPool.getConnectionPool();
    private final MySQLCommentDao testDao = MySQLCommentDao.getInstance();
    private final User testUser = MySQLUserDao.getInstance().findAll().stream().findAny().get();
    private final Book testBook = MySQLBookDao.getInstance().findAll().stream().findAny().get();
    private Comment testComment = new Comment(testUser, testBook, LocalDate.now(), "Test text for comment dao test");

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
        testComment = testDao.save(testComment);
    }

    @After
    public void tearDown() throws Exception {
        testDao.delete(testComment.getId());
    }

    @Test
    public void save_mustAssignIdToSavedComment() {
        assertNotNull("Save must return not null comment instance", testComment);
        assertNotNull("Saved comment must have not null id", testComment.getId());
    }

    @Test
    public void findAll_mustReturnNotNullList() {
        final List<Comment> allComments = testDao.findAll();
        assertNotNull("All comments list must be not null", allComments);
    }

    @Test
    public void findById_mustReturnNotNullOptional() {
        final Optional<Comment> optionalComment = testDao.findById(testComment.getId());
        assertNotNull("Returned instance must be not null", optionalComment);
    }

    @Test
    public void findById_mustReturnSavedComment_whenSavedCommentIdPassed() {
        final Optional<Comment> optionalComment = testDao.findById(testComment.getId());
        assertTrue("Optional instance must be not empty", optionalComment.isPresent());
        assertEquals("Found comment must be equal to testComment", testComment, optionalComment.get());
    }

    @Test
    public void findById_mustReturnEmptyOptionalComment_whenThereIsNoCommentWithPassedId() {
        testDao.delete(testComment.getId());
        final Optional<Comment> optionalComment = testDao.findById(testComment.getId());
        assertFalse("Optional comment must be empty", optionalComment.isPresent());
    }

    @Test
    public void update_mustUpdateCommentText() {
        String newText = "Updated text for testComment";
        testComment = new Comment(testComment.getId(), testComment.getUser(), testComment.getBook(), testComment.getDate(), newText);
        final Comment updatedComment = testDao.update(testComment);
        assertNotNull("Update comment must be not null", updatedComment);
        assertEquals("Updated comment text must be equal to newText", newText, updatedComment.getText());
        assertEquals("Updated comment must be equal to testComment", testComment, updatedComment);
    }

    @Test
    public void delete_mustDeleteTestComment() {
        testDao.delete(testComment.getId());
        final List<Comment> allComments = testDao.findAll();
        assertFalse("All comments list must not contain deleted comment", allComments.contains(testComment));
    }

    @Test
    public void findPage_mustReturnNotNullPage() {
        final int pagesAmount = testDao.getPagesAmount();
        final List<Comment> foundPage = testDao.findPage(pagesAmount);
        assertNotNull("Found page list must be not null", foundPage);
    }

    @Test
    public void getRowsAmount_mustReturnNotNegativeNumber() {
        final int rowsAmount = testDao.getRowsAmount();
        assertTrue("Returned value must be not negative", rowsAmount >= 0);
    }

    @Test
    public void getPagesAmount_mustReturnNotNegativeNumber() {
        final int pagesAmount = testDao.getPagesAmount();
        assertTrue("Returned value must be not negative", pagesAmount >= 0);
    }

    @Test
    public void getInstance_mustReturnSameInstanceAsTestInstance() {
        final MySQLCommentDao instance = MySQLCommentDao.getInstance();
        assertSame("Returned instance must be same as test instance", testDao, instance);
    }

    @Test
    public void findByBookId_mustReturnNotNullListOfComments_whenTestCommentBookIdPassed() {
        final List<Comment> commentsByBookId = testDao.findByBook(testComment.getBook());
        assertNotNull("Found comments list must be not null", commentsByBookId);
        assertTrue("Found comments list must contain test comment", commentsByBookId.contains(testComment));
    }
}