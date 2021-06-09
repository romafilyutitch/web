package by.epam.jwd.web.command;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.command.LoginCommand;
import by.epam.jwd.web.command.LogoutCommand;
import by.epam.jwd.web.model.UserRole;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum CommandEnum {
    SHOW_LOGIN(new ShowLoginCommand(), UserRole.UNAUTHORIZED),
    LOGIN(new LoginCommand(), UserRole.UNAUTHORIZED),
    LOGOUT(new LogoutCommand(), UserRole.READER, UserRole.ADMIN, UserRole.LIBRARIAN),
    SHOW_USERS(new ShowUsersListCommand(), UserRole.ADMIN),
    MAIN(new MainCommand(), UserRole.UNAUTHORIZED, UserRole.READER, UserRole.LIBRARIAN, UserRole.ADMIN),
    SHOW_REGISTER(new ShowRegisterCommand(), UserRole.UNAUTHORIZED),
    REGISTER(new RegisterCommand(), UserRole.UNAUTHORIZED),
    SHOW_ERROR_PAGE(new ShowErrorCommand(), UserRole.UNAUTHORIZED, UserRole.READER, UserRole.LIBRARIAN, UserRole.ADMIN);

    private final ActionCommand command;
    private final List<UserRole> allowedRoles;


    CommandEnum(ActionCommand command, UserRole... roles) {
        this.command = command;
        this.allowedRoles = roles != null ? Arrays.asList(roles) : UserRole.rolesAsList();
    }
    public List<UserRole> getAllowedRoles() {
        return allowedRoles;
    }
    public ActionCommand getCurrentCommand() {
        return command;
    }
}
