package by.epam.jwd.web.command;

import by.epam.jwd.web.model.UserRole;

import java.util.Arrays;
import java.util.List;

public enum CommandEnum {
    SHOW_LOGIN(new ShowLoginCommand(), UserRole.UNAUTHORIZED),
    LOGIN(new LoginCommand(), UserRole.UNAUTHORIZED),
    LOGOUT(new LogoutCommand(), UserRole.READER, UserRole.ADMIN, UserRole.LIBRARIAN),
    SHOW_USERS(new ShowUsersListCommand(), UserRole.ADMIN),
    SHOW_BOOKS(new ShowBooksListCommand(), UserRole.ADMIN),
    SHOW_ORDERS(new ShowOrdersListCommand(), UserRole.ADMIN),
    MAIN(new MainCommand(), UserRole.UNAUTHORIZED, UserRole.READER, UserRole.LIBRARIAN, UserRole.ADMIN),
    SHOW_REGISTER(new ShowRegisterCommand(), UserRole.UNAUTHORIZED),
    REGISTER(new RegisterCommand(), UserRole.UNAUTHORIZED),
    SHOW_ERROR_PAGE(new ShowErrorCommand(), UserRole.UNAUTHORIZED, UserRole.READER, UserRole.LIBRARIAN, UserRole.ADMIN),
    ADD_COPY(new AddCopyCommand(), UserRole.ADMIN),
    REMOVE_COPY(new RemoveCopyCommand(), UserRole.ADMIN),
    DELETE_BOOK(new DeleteBookCommand(), UserRole.ADMIN),
    ADD_BOOK(new AddBookCommand(), UserRole.ADMIN),
    PROMOTE_ROLE(new PromoteRoleCommand(), UserRole.ADMIN),
    DEMOTE_ROLE(new DemoteRoleCommand(), UserRole.ADMIN),
    DELETE_USER(new DeleteUserCommand(), UserRole.ADMIN),
    SET_SUBSCRIPTION(new SetSubscriptionCommand(), UserRole.ADMIN),
    SHOW_SET_SUBSCRIPTION_PAGE(new ShowSetSubscriptionPageCommand(), UserRole.ADMIN),
    READ(new ReadCommand(), UserRole.ADMIN, UserRole.LIBRARIAN, UserRole.READER),
    RETURN(new ReturnCommand(), UserRole.ADMIN, UserRole.LIBRARIAN, UserRole.READER),
    SHOW_ADD_BOOK_PAGE(new ShowAddBookPageCommand(), UserRole.ADMIN);


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
