package by.epam.jwd.web.resource;

import java.util.ResourceBundle;

/**
 * Utility class that finds messages class based on current default locale
 * and returns messages from it by passed key
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class MessageManager {
    private static final String MESSAGES_PROPERTIES_NAME = "messages";
    private MessageManager() {}

    /**
     * Finds properties file based on current default locale and
     * returns message by passed key.
     * @param key message key.
     * @return found message by passed key.
     */
    public static String getMessage(String key) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(MESSAGES_PROPERTIES_NAME);
        return resourceBundle.getString(key);
    }
}
