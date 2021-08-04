package by.epam.jwd.web.service.impl;

import by.epam.jwd.web.service.BookService;
import by.epam.jwd.web.service.CommentService;
import by.epam.jwd.web.service.LikeService;
import by.epam.jwd.web.service.OrderService;
import by.epam.jwd.web.service.ServiceFactory;
import by.epam.jwd.web.service.UserService;

public class AppServiceFactory implements ServiceFactory {

    private AppServiceFactory() {}

    public static AppServiceFactory getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public BookService getBookService() {
        return SimpleBookService.getInstance();
    }

    @Override
    public OrderService getOrderService() {
        return SimpleOrderService.getInstance();
    }

    @Override
    public UserService getUserService() {
        return SimpleUserService.getInstance();
    }

    @Override
    public CommentService getCommentService() {
        return SimpleCommentService.getInstance();
    }

    @Override
    public LikeService getLikeService() {
        return SimpleLikeService.getInstance();
    }

    private static class Singleton {
        private static final AppServiceFactory INSTANCE = new AppServiceFactory();
    }
}
