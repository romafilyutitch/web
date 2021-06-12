package by.epam.jwd.web.service;

public interface ServiceFactory {

    BookService getBookService();

    OrderService getOrderService();

    UserService getUserService();

    static ServiceFactory getInstance() {
        return AppServiceFactory.getInstance();
    }
}
