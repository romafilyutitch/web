package by.epam.jwd.web.command;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

public class SetLocaleCommand implements ActionCommand {
    private SetLocaleCommand() {}
    
    public static SetLocaleCommand getInstance() {
        return Singleton.INSTANCE;
    }
    
    @Override
    public CommandResult execute(HttpServletRequest request) {
        final String locale = request.getParameter("locale");
        request.getSession().setAttribute("locale", locale);
        return new CommandResult() {
            @Override
            public String getResultPath() {
                return "index.jsp";
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
