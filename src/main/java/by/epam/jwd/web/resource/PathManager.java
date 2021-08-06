package by.epam.jwd.web.resource;

import java.util.ResourceBundle;

/**
 * Utility class that finds resource file that contains all page paths
 * and returns them by passed key.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class PathManager {
    private static final String PATH_PROPERTIES_NAME = "paths";
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle(PATH_PROPERTIES_NAME);
    private static final String MAIN_PAGE_PATH_KEY = "main";
    private static final String LOGIN_PAGE_PATH_KEY = "login";
    private static final String REGISTER_PAGE_PATH_KEY = "register";
    private static final String ACCOUNT_PAGE_PATH_KEY = "account";
    private static final String BOOKS_PAGE_PATH_KEY = "books";
    private static final String ORDERS_PAGE_PATH_KEY = "orders";
    private static final String USER_ORDERS_PAGE_PATH_KEY = "userOrders";
    private static final String USERS_PAGE_PATH_KEY = "users";

    private PathManager() {}

    /**
     * Returns page path by passed key.
     * @param key page key.
     * @return page path by passed key.
     */
    public static String getPath(String key) {
        return resourceBundle.getString(key);
    }

    /**
     * Finds in properties file main page path and returns
     * it to make forward to that page.
     * @return main page path.
     */
    public static String getMainPagePath() {
        return resourceBundle.getString(MAIN_PAGE_PATH_KEY);
    }

    /**
     * Finds in properties file login page path and returns
     * it to make forward to that page.
     * @return login page path.
     */
    public static String getLoginPagePath() {
        return resourceBundle.getString(LOGIN_PAGE_PATH_KEY);
    }

    /**
     * Finds in properties file register page path and returns
     * it to make forward to that page.
     * @return register page path.
     */
    public static String getRegisterPagePath() {
        return resourceBundle.getString(REGISTER_PAGE_PATH_KEY);
    }

    /**
     * Finds in properties file account page path and returns
     * it to make forward to that page.
     * @return account page path
     */
    public static String getAccountPagePath() {
        return resourceBundle.getString(ACCOUNT_PAGE_PATH_KEY);
    }

    /**
     * Finds in properties file books page path and returns
     * it to make forward to that path.
     * @return book page path.
     */
    public static String getBooksPagePath() {
        return resourceBundle.getString(BOOKS_PAGE_PATH_KEY);
    }

    /**
     * Find in properties file orders page path and returns
     * it to make forward to that path.
     * @return orders page path.
     */
    public static String getOrdersPagePath() {
        return resourceBundle.getString(ORDERS_PAGE_PATH_KEY);
    }

    /**
     * Find in properties file user's orders page path and
     * returns it to make forward to that path.
     * @return user's orders page path
     */
    public static String getUserOrdersPagePath() {
        return resourceBundle.getString(USER_ORDERS_PAGE_PATH_KEY);
    }

    /**
     * Finds in properties file users page path and
     * returns it to make forward to that path.
     * @return users page path.
     */
    public static String getUsersPagePath() {
        return resourceBundle.getString(USERS_PAGE_PATH_KEY);
    }
}
