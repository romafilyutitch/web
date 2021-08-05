package by.epam.jwd.web.filter;

import by.epam.jwd.web.command.CommandEnum;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.model.UserRole;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

/**
 * Filter make command permission checking.
 * If user don't have permitted role to make command then forbidden
 * page is shown to user. If user allowed role that command is executed.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
@WebFilter(urlPatterns = "/controller")
public class PermissionFilter implements Filter {
    private static final Map<UserRole, Set<CommandEnum>> commandsByRole = new EnumMap<>(UserRole.class);
    public static final String COMMAND = "command";
    public static final String USER = "user";

    /**
     * Build role and commands that allowed by role map.
     * @param filterConfig filter configuration
     * @throws ServletException when servlet exception occurs.
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        for (CommandEnum command : CommandEnum.values()) {
            for (UserRole allowedRole : command.getAllowedRoles()) {
                Set<CommandEnum> commands = commandsByRole.get(allowedRole);
                if (commands == null) {
                    commands = EnumSet.noneOf(CommandEnum.class);
                    commandsByRole.put(allowedRole, commands);
                }
                commands.add(command);
            }
        }
    }

    /**
     * Check if command name presents. If command name is absent then send  error not found with message.
     * Then if command can be executed by user with allowed role filter passed request.
     * If user have not allowed role then filter send error to forbidden page.
     * @param servletRequest request by client.
     * @param servletResponse client response
     * @param filterChain chain to link some filters together
     * @throws IOException when exception in servlet io occurs.
     * @throws ServletException when exception in servlet occurs.
     */
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        final HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        final String commandName = httpRequest.getParameter(COMMAND);
        if (commandName == null) {
            httpResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
        } else {
            try {
                final CommandEnum command = CommandEnum.valueOf(commandName.toUpperCase());
                final HttpSession session = httpRequest.getSession(false);
                UserRole userRole = session != null && session.getAttribute(USER) != null ? ((User) session.getAttribute(USER)).getRole() : UserRole.UNAUTHORIZED;
                final Set<CommandEnum> allowedCommands = commandsByRole.get(userRole);
                if (allowedCommands.contains(command)) {
                    filterChain.doFilter(servletRequest, servletResponse);
                } else {
                    httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
                }
            } catch (IllegalArgumentException e) {
                httpResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        }
    }
}
