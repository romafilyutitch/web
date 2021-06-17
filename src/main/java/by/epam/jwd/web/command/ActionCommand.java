package by.epam.jwd.web.command;

import javax.servlet.http.HttpServletRequest;

public interface ActionCommand {
    CommandResult execute(HttpServletRequest request);
}
