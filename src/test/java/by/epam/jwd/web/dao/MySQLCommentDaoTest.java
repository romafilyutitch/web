package by.epam.jwd.web.dao;

import by.epam.jwd.web.connectionPool.ConnectionPool;
import by.epam.jwd.web.dao.mysql.MySQLBookDao;
import by.epam.jwd.web.dao.mysql.MySQLCommentDao;
import by.epam.jwd.web.dao.mysql.MySQLUserDao;
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

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class MySQLCommentDaoTest {
    private final MySQLCommentDao testDao = MySQLCommentDao.getInstance();
    private User testUser = new User("test user", "test user");
    private Book testBook = new Book("test book", "test book", Genre.FANTASY, 1, "text");
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
    public void setUp() throws Exception {
        testUser = MySQLUserDao.getInstance().save(testUser);
        testBook = MySQLBookDao.getInstance().save(testBook);
        testComment = new Comment(testUser, testBook, "test text");
        testComment = testDao.save(testComment);
    }

    @After
    public void tearDown() throws Exception {
        testDao.delete(testComment.getId());
        MySQLUserDao.getInstance().delete(testUser.getId());
        MySQLBookDao.getInstance().delete(testBook.getId());
    }

    @Test
    public void save_mustAssignIdToSavedComment() {
        assertNotNull(testComment);
        assertNotNull(testComment.getId());
    }

    @Test
    public void findAll_mustReturnNotNullList() {
        final List<Comment> allComments = testDao.findAll();
        assertNotNull(allComments);
    }

    @Test
    public void findById_mustReturnNotNullOptional() {
        final Optional<Comment> optionalComment = testDao.findById(testComment.getId());
        assertNotNull(optionalComment);
    }

    @Test
    public void findById_mustReturnSavedComment_whenSavedCommentIdPassed() {
        final Optional<Comment> optionalComment = testDao.findById(testComment.getId());
        assertTrue(optionalComment.isPresent());
        assertEquals(testComment, optionalComment.get());
    }

    @Test
    public void findById_mustReturnEmptyOptionalComment_whenThereIsNoCommentWithPassedId() {
        testDao.delete(testComment.getId());
        final Optional<Comment> optionalComment = testDao.findById(testComment.getId());
        assertFalse(optionalComment.isPresent());
    }

    @Test
    public void update_mustUpdateCommentText() {
        String newText = "Updated text for testComment";
        testComment = new Comment(testComment.getId(), testComment.getUser(), testComment.getBook(), testComment.getDate(), newText);
        final Comment updatedComment = testDao.update(testComment);
        assertNotNull(updatedComment);
        assertEquals(newText, updatedComment.getText());
        assertEquals(testComment, updatedComment);
    }

    @Test
    public void delete_mustDeleteTestComment() {
        testDao.delete(testComment.getId());
        final List<Comment> allComments = testDao.findAll();
        assertFalse(allComments.contains(testComment));
    }

    @Test
    public void findPage_mustReturnNotNullPage() {
        final int pagesAmount = testDao.getPagesAmount();
        final List<Comment> foundPage = testDao.findPage(pagesAmount);
        assertNotNull(foundPage);
    }

    @Test
    public void getRowsAmount_mustReturnNotNegativeNumber() {
        final int rowsAmount = testDao.getRowsAmount();
        assertTrue(rowsAmount >= 0);
    }

    @Test
    public void getPagesAmount_mustReturnNotNegativeNumber() {
        final int pagesAmount = testDao.getPagesAmount();
        assertTrue(pagesAmount >= 0);
    }

    @Test
    public void getInstance_mustReturnSameInstanceAsTestInstance() {
        final MySQLCommentDao instance = MySQLCommentDao.getInstance();
        assertSame(testDao, instance);
    }

    @Test
    public void findByBookId_mustReturnNotNullListOfComments_whenTestCommentBookIdPassed() {
        final List<Comment> commentsByBookId = testDao.findByBook(testComment.getBook());
        assertNotNull(commentsByBookId);
        assertTrue(commentsByBookId.contains(testComment));
    }
}