package by.epam.jwd.web.resource;

import java.util.ResourceBundle;

public class MessageManager {
    private static final String MESSAGES_BUNDLE_BASENAME = "messages";
    private MessageManager() {}

    public static String getMessage(String key) {
        ResourceBundle resourceBundle = ResourceBundle.getBundle(MESSAGES_BUNDLE_BASENAME);
        return resourceBundle.getString(key);
    }
}
