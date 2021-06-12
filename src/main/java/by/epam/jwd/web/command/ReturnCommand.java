package by.epam.jwd.web.command;

import javax.servlet.http.HttpServletRequest;

public class ReturnCommand implements ActionCommand {

    public static final String MAIN_CONTROLLER_COMMAND = "controller?command=main";

    private ReturnCommand() {
    }

    public static ReturnCommand getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public String execute(HttpServletRequest request) {
        return MAIN_CONTROLLER_COMMAND;
    }

    private static class Singleton {
        private static final ReturnCommand INSTANCE = new ReturnCommand();
    }
}
