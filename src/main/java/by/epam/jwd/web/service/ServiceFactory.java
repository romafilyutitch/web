package by.epam.jwd.web.service;

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
