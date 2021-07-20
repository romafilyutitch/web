package by.epam.jwd.web.command;

import javax.servlet.http.HttpServletRequest;

public class SetLocaleCommand implements ActionCommand {

    private static final String REQUEST_LOCALE_PARAMETER_KEY = "locale";
    private static final String SESSION_LOCALE_ATTRIBUTE_KEY = "locale";

    private static final String RESULT_PATH = "index.jsp";

    private SetLocaleCommand() {
    }

    public static SetLocaleCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public CommandResult execute(HttpServletRequest request) {
        final String locale = request.getParameter(REQUEST_LOCALE_PARAMETER_KEY);
        request.getSession().setAttribute(SESSION_LOCALE_ATTRIBUTE_KEY, locale);
        return new CommandResult() {
            @Override
            public String getResultPath() {
                return RESULT_PATH;
            }

            @Override
            public boolean isRedirect() {
                return true;
            }
        };
    }

    private static class Singleton {
        private static final SetLocaleCommand INSTANCE = new SetLocaleCommand();
    }
}
