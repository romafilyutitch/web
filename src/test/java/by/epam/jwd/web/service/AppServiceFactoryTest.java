package by.epam.jwd.web.service;

import by.epam.jwd.web.service.api.BookService;
import by.epam.jwd.web.service.api.CommentService;
import by.epam.jwd.web.service.api.LikeService;
import by.epam.jwd.web.service.api.OrderService;
import by.epam.jwd.web.service.api.UserService;
import by.epam.jwd.web.service.impl.AppServiceFactory;
import org.junit.Test;

import static org.junit.Assert.*;

public class AppServiceFactoryTest {
    private final AppServiceFactory testFactory = AppServiceFactory.getInstance();

    @Test
    public void getInstance_mustReturnSameInstance() {
        final AppServiceFactory instance = AppServiceFactory.getInstance();
        assertSame("Get instance must be the same with testFactory", testFactory, instance);
    }

    @Test
    public void getBookService_mustReturnNotNullInstance() {
        final BookService bookService = testFactory.getBookService();
        assertNotNull("Returned book service must be not null", bookService);
    }

    @Test
    public void getOrderService_mustReturnNotNullInstance() {
        final OrderService orderService = testFactory.getOrderService();
        assertNotNull("Returned order service must be not null", orderService);
    }

    @Test
    public void getUserService_mustReturnNotNullInstance() {
        final UserService userService = testFactory.getUserService();
        assertNotNull("Returned user service must be not null", userService);
    }

    @Test
    public void getCommentService_mustReturnNotNullInstance() {
        final CommentService commentService = testFactory.getCommentService();
        assertNotNull("Returned comment service must be not null", commentService);
    }

    @Test
    public void getLikeService_mustReturnNotNullInstance() {
        final LikeService likeService = testFactory.getLikeService();
        assertNotNull("Returned likeService must be not null", likeService);
    }
}