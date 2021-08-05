package by.epam.jwd.web.resource;

import java.util.ResourceBundle;

/**
 * Utility class that finds commands properties and
 * returns commands them by passed key.
 * @author roma0.
 * @version 1.0.
 * @since 1.0.
 */
public class CommandManager {
    private static final String COMMAND_PROPERTIES_NAME = "commands";
    private static final ResourceBundle commandsBundle = ResourceBundle.getBundle(COMMAND_PROPERTIES_NAME);

    private CommandManager() {}

    /**
     * Returns command by passed key.
     * @param key command key.
     * @return command by passed key.
     */
    public static String getCommand(String key) {
        return commandsBundle.getString(key);
    }
}
