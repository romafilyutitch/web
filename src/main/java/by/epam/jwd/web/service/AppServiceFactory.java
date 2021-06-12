package by.epam.jwd.web.service;

class AppServiceFactory implements ServiceFactory {

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

    private static class Singleton {
        private static final AppServiceFactory INSTANCE = new AppServiceFactory();
    }
}
