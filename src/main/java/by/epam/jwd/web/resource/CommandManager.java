package by.epam.jwd.web.resource;

import java.util.ResourceBundle;

/**
 * Utility class that finds commands properties file and
 * returns commands by passed key.
 * @author roma0.
 * @version 1.0.
 * @since 1.0.
 */
public class CommandManager {
    private static final String COMMAND_PROPERTIES_NAME = "commands";
    private static final ResourceBundle commandsBundle = ResourceBundle.getBundle(COMMAND_PROPERTIES_NAME);
    private static final String MAIN_COMMAND_KEY = "main";
    private static final String SHOW_BOOKS_COMMAND_KEY = "show.books";
    private static final String SHOW_ORDERS_COMMAND_KEY = "show.orders";
    private static final String SHOW_USERS_COMMAND_KEY = "show.users";
    private static final String SHOW_USER_ORDERS_COMMAND_KEY = "show.userOrders";

    private CommandManager() {}

    /**
     * Finds in properties file main command string and
     * returns it to make forward on that command.
     * @return main command string.
     */
    public static String getMainCommand() {
        return commandsBundle.getString(MAIN_COMMAND_KEY);
    }

    /**
     * Finds in properties file show books command string and
     * returns it to make forward on that command.
     * @return show books command.
     */
    public static String getShowBooksCommand() {
        return commandsBundle.getString(SHOW_BOOKS_COMMAND_KEY);
    }

    /**
     * Finds in properties file show orders command string and
     * returns it to make forward on that command.
     * @return show orders command.
     */
    public static String getShowOrdersCommand() {
        return commandsBundle.getString(SHOW_ORDERS_COMMAND_KEY);
    }

    /**
     * Finds in properties file show users command string and
     * returns it to make forward on that command.
     * @return show users command.
     */
    public static String getShowUsersCommand() {
        return commandsBundle.getString(SHOW_USERS_COMMAND_KEY);
    }

    /**
     * Finds in properties file show user's orders command string
     * and returns it to make forward on that command.
     * @return show user's orders command.
     */
    public static String getShowUsersOrdersCommand() {
        return commandsBundle.getString(SHOW_USER_ORDERS_COMMAND_KEY);
    }
}
