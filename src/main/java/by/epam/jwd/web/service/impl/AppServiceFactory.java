package by.epam.jwd.web.service.impl;

import by.epam.jwd.web.service.BookService;
import by.epam.jwd.web.service.CommentService;
import by.epam.jwd.web.service.LikeService;
import by.epam.jwd.web.service.OrderService;
import by.epam.jwd.web.service.ServiceFactory;
import by.epam.jwd.web.service.UserService;

/**
 * Service factory interface implementation. Produces services
 * implementation.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class AppServiceFactory implements ServiceFactory {

    private AppServiceFactory() {}

    /**
     * Gets single class instance from nested class.
     * @return class instance.
     */
    public static AppServiceFactory getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Returns book service produced by factory.
     * @return book service.
     */
    @Override
    public BookService getBookService() {
        return SimpleBookService.getInstance();
    }

    /**
     * Returns order service produced by factory.
     * @return order service.
     */
    @Override
    public OrderService getOrderService() {
        return SimpleOrderService.getInstance();
    }

    /**
     * Returns user service produced by factory.
     * @return user service.
     */
    @Override
    public UserService getUserService() {
        return SimpleUserService.getInstance();
    }

    /**
     * Returns comment service produced by factory.
     * @return comment service.
     */
    @Override
    public CommentService getCommentService() {
        return SimpleCommentService.getInstance();
    }

    /**
     * Returns like service produced by factory.
     * @return like service.
     */
    @Override
    public LikeService getLikeService() {
        return SimpleLikeService.getInstance();
    }

    /**
     * Nested class that encapsulates single {@link AppServiceFactory} instance.
     * Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final AppServiceFactory INSTANCE = new AppServiceFactory();
    }
}
