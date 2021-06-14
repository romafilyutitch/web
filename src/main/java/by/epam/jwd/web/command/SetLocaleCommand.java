package by.epam.jwd.web.command;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public class SetLocaleCommand implements ActionCommand {
    private SetLocaleCommand() {}
    
    public static SetLocaleCommand getInstance() {
        return Singleton.INSTANCE;
    }
    
    @Override
    public String execute(HttpServletRequest request) {
        final String locale = request.getParameter("locale");
        request.getSession().setAttribute("locale", locale);
        return null;
    }
    
    private static class Singleton {
        private static final SetLocaleCommand INSTANCE = new SetLocaleCommand();
    }
}
