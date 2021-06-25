package by.epam.jwd.web.service;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class AppServiceFactoryTest {
    private final AppServiceFactory testFactory = AppServiceFactory.getInstance();

    @Test
    public void getInstance_mustReturnSameInstance() {
        final AppServiceFactory instance = AppServiceFactory.getInstance();
        Assert.assertSame("Get instance must be the same with testFactory", testFactory, instance);
    }

    @Test
    public void getBookService_mustReturnNotNullInstance() {
        final BookService bookService = testFactory.getBookService();
        Assert.assertNotNull("Returned book service must be not null", bookService);
    }

    @Test
    public void getOrderService_mustReturnNotNullInstance() {
        final OrderService orderService = testFactory.getOrderService();
        Assert.assertNotNull("Returned order service must be not null", orderService);
    }

    @Test
    public void getUserService_mustReturnNotNullInstance() {
        final UserService userService = testFactory.getUserService();
        Assert.assertNotNull("Returned user service must be not null", userService);
    }
}