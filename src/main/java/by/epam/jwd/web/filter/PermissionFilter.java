package by.epam.jwd.web.filter;

import by.epam.jwd.web.command.ActionCommand;
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
import java.util.Arrays;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@WebFilter(urlPatterns = "/controller")
public class PermissionFilter implements Filter {
    private static final Map<UserRole, Set<CommandEnum>> commandsByRole = new EnumMap<>(UserRole.class);

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

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        final HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        final HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
        final CommandEnum command = CommandEnum.valueOf(httpRequest.getParameter("command").toUpperCase());
        final HttpSession session = httpRequest.getSession(false);
        UserRole userRole = session != null && ((User) session.getAttribute("user")) != null ? ((User) session.getAttribute("user")).getRole() : UserRole.UNAUTHORIZED;
        final Set<CommandEnum> allowedCommands = commandsByRole.get(userRole);
        if (allowedCommands.contains(command)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
