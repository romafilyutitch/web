package by.epam.jwd.web.service;

import by.epam.jwd.web.service.impl.AppServiceFactory;

/**
 * Service factory interface that defines
 * service factory behavior.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public interface ServiceFactory {

    /**
     * Returns book service instance that factory produces.
     * @return book service instance.
     */
    BookService getBookService();

    /**
     * Returns order service instance that factory produces.
     * @return order service instance.
     */
    OrderService getOrderService();

    /**
     * Returns user service instance that factory produces.
     * @return user service instance.
     */
    UserService getUserService();

    /**
     * Returns comment service that factory produces.
     * @return comments service.
     */
    CommentService getCommentService();

    /**
     * Returns like service that factory produces.
     * @return like service.
     */
    LikeService getLikeService();

    /**
     * Returns Service factory implementation.
     * @return interface factory implementation.
     */
    static ServiceFactory getInstance() {
        return AppServiceFactory.getInstance();
    }
}
