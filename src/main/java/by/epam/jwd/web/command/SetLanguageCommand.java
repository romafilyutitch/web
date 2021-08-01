package by.epam.jwd.web.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Locale;

public class SetLanguageCommand implements ActionCommand {

    private static final String REQUEST_LANGUAGE_PARAMETER_KEY = "language";
    private static final String SESSION_LANGUAGE_ATTRIBUTE_KEY = "language";

    private static final String RESULT_PATH = "controller?command=main";

    private SetLanguageCommand() {
    }

    public static SetLanguageCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        final HttpSession currentSession = request.getSession();
        final String languageName = request.getParameter(REQUEST_LANGUAGE_PARAMETER_KEY);
        final Locale currentLocale = new Locale(languageName);
        Locale.setDefault(currentLocale);
        currentSession.setAttribute(SESSION_LANGUAGE_ATTRIBUTE_KEY, languageName);
        return RESULT_PATH;
    }

    private static class Singleton {
        private static final SetLanguageCommand INSTANCE = new SetLanguageCommand();
    }
}
