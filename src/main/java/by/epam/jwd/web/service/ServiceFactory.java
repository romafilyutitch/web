package by.epam.jwd.web.service;

import by.epam.jwd.web.service.impl.AppServiceFactory;

public interface ServiceFactory {

    BookService getBookService();

    OrderService getOrderService();

    UserService getUserService();

    CommentService getCommentService();

    LikeService getLikeService();

    static ServiceFactory getInstance() {
        return AppServiceFactory.getInstance();
    }
}
