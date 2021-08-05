package by.epam.jwd.web.command;

import javax.servlet.http.HttpServletRequest;

/**
 * Web action command interface. Implementation of
 * command pattern. Interface is implemented commands class
 * to define any command that make some actions.
 * @author roma0
 * @version 1.0
 * @since 1.0
 * @see "Command pattern"
 */
public interface ActionCommand {
    /**
     * Executes command with help of services and makes request to client and returns result path or command
     * to make forward.
     * @param request request that need to be execute
     * @return command execution result path or command to make forward in {@link by.epam.jwd.web.servlet.Controller}.
     */
    String execute(HttpServletRequest request);
}
