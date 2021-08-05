package by.epam.jwd.web.command.language;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.resource.ConfigurationManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Locale;

/**
 * Executes command that is set user language (english, russian, chinese, indian or arabic).
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class SetLanguageCommand implements ActionCommand {

    private static final String REQUEST_LANGUAGE_PARAMETER_KEY = "language";
    private static final String SESSION_LANGUAGE_ATTRIBUTE_KEY = "language";

    private SetLanguageCommand() {
    }

    /**
     * Gets single class instance from nested class.
     * @return class instance.
     */
    public static SetLanguageCommand getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Set current language.
     * Request must have language value.
     * @param request request that need to be execute.
     * @return main command for forward.
     */
    @Override
    public String execute(HttpServletRequest request) {
        final HttpSession currentSession = request.getSession();
        final String languageName = request.getParameter(REQUEST_LANGUAGE_PARAMETER_KEY);
        final Locale currentLocale = new Locale(languageName);
        Locale.setDefault(currentLocale);
        currentSession.setAttribute(SESSION_LANGUAGE_ATTRIBUTE_KEY, languageName);
        return ConfigurationManager.getMainCommand();
    }

    /**
     * Nested class that encapsulates single {@link SetLanguageCommand} instance.
     * Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final SetLanguageCommand INSTANCE = new SetLanguageCommand();
    }
}
