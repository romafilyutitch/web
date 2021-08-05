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

    private PathManager() {}

    /**
     * Returns page path by passed key.
     * @param key page key.
     * @return page path by passed key.
     */
    public static String getPath(String key) {
        return resourceBundle.getString(key);
    }
}
