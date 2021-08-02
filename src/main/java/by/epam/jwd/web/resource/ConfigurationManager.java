package by.epam.jwd.web.resource;

import java.util.ResourceBundle;

public class ConfigurationManager {
    private static final String CONFIG_BUNDLE_NAME = "config";
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle(CONFIG_BUNDLE_NAME);
    private static final String MAIN_PAGE_PATH_KEY = "path.main";
    private static final String LOGIN_PAGE_PATH_KEY = "path.login";
    private static final String REGISTER_PAGE_PATH_KEY = "path.register";
    private static final String ACCOUNT_PAGE_PATH_KEY = "path.account";
    private static final String BOOKS_PAGE_PATH_KEY = "path.books";
    private static final String ORDERS_PAGE_PATH_KEY = "path.orders";
    private static final String USER_ORDERS_PAGE_PATH_KEY = "path.userOrders";
    private static final String USERS_PAGE_PATH_KEY = "path.users";
    private static final String MAIN_COMMAND_KEY = "command.main";
    private static final String SHOW_BOOKS_COMMAND_KEY = "command.show.books";
    private static final String SHOW_USERS_COMMAND_KEY = "command.show.users";
    private static final String SHOW_ORDERS_COMMAND_KEY = "command.show.orders";
    private static final String SHOW_USER_ORDERS_COMMAND_KEY = "command.show.userOrders";

    private ConfigurationManager() {}

    public static String getMainPagePath() {
        return resourceBundle.getString(MAIN_PAGE_PATH_KEY);
    }

    public static String getLoginPagePath() {
        return resourceBundle.getString(LOGIN_PAGE_PATH_KEY);
    }

    public static String getRegisterPagePath() {
        return resourceBundle.getString(REGISTER_PAGE_PATH_KEY);
    }

    public static String getAccountPagePath() {
        return resourceBundle.getString(ACCOUNT_PAGE_PATH_KEY);
    }

    public static String getBooksPagePath() {
        return resourceBundle.getString(BOOKS_PAGE_PATH_KEY);
    }

    public static String getOrdersPagePath() {
        return resourceBundle.getString(ORDERS_PAGE_PATH_KEY);
    }

    public static String getUserOrdersPagePath() {
        return resourceBundle.getString(USER_ORDERS_PAGE_PATH_KEY);
    }

    public static String getUsersPagePath() {
        return resourceBundle.getString(USERS_PAGE_PATH_KEY);
    }

    public static String getMainCommand() {return resourceBundle.getString(MAIN_COMMAND_KEY);}

    public static String getShowBooksCommand() {
        return resourceBundle.getString(SHOW_BOOKS_COMMAND_KEY);
    }

    public static String getShowUsersCommand() {
        return resourceBundle.getString(SHOW_USERS_COMMAND_KEY);
    }

    public static String getShowOrdersCommand() {
        return resourceBundle.getString(SHOW_ORDERS_COMMAND_KEY);
    }

    public static String getShowUserOrdersCommand() {
        return resourceBundle.getString(SHOW_USER_ORDERS_COMMAND_KEY);
    }
}
