package by.epam.jwd.web.command;

import by.epam.jwd.web.model.UserRole;

import java.util.Arrays;
import java.util.List;

public enum CommandEnum {
    SHOW_LOGIN( ShowLoginCommand.getInstance(), UserRole.UNAUTHORIZED),
    LOGIN( LoginCommand.getInstance(), UserRole.UNAUTHORIZED),
    LOGOUT(LogoutCommand.getInstance(), UserRole.READER, UserRole.ADMIN, UserRole.LIBRARIAN),
    SHOW_USERS(ShowUsersListCommand.getInstance(), UserRole.ADMIN),
    SHOW_BOOKS(ShowBooksListCommand.getInstance(), UserRole.ADMIN),
    SHOW_ORDERS(ShowOrdersListCommand.getInstance(), UserRole.ADMIN),
    MAIN(MainCommand.getInstance(), UserRole.UNAUTHORIZED, UserRole.READER, UserRole.LIBRARIAN, UserRole.ADMIN),
    SHOW_REGISTER(ShowRegisterCommand.getInstance(), UserRole.UNAUTHORIZED),
    REGISTER(RegisterCommand.getInstance(), UserRole.UNAUTHORIZED),
    SHOW_ERROR_PAGE(ShowErrorCommand.getInstance(), UserRole.UNAUTHORIZED, UserRole.READER, UserRole.LIBRARIAN, UserRole.ADMIN),
    ADD_COPY(AddCopyCommand.getInstance(), UserRole.ADMIN),
    REMOVE_COPY(RemoveCopyCommand.getInstance(), UserRole.ADMIN),
    DELETE_BOOK(DeleteBookCommand.getInstance(), UserRole.ADMIN),
    ADD_BOOK(AddBookCommand.getInstance(), UserRole.ADMIN),
    PROMOTE_ROLE(PromoteRoleCommand.getInstance(), UserRole.ADMIN),
    DEMOTE_ROLE(DemoteRoleCommand.getInstance(), UserRole.ADMIN),
    DELETE_USER(DeleteUserCommand.getInstance(), UserRole.ADMIN),
    SET_SUBSCRIPTION(SetSubscriptionCommand.getInstance(), UserRole.ADMIN),
    SHOW_SET_SUBSCRIPTION_PAGE(ShowSetSubscriptionPageCommand.getInstance(), UserRole.ADMIN),
    READ(ReadCommand.getInstance(), UserRole.ADMIN, UserRole.LIBRARIAN, UserRole.READER),
    RETURN(ReturnCommand.getInstance(), UserRole.ADMIN, UserRole.LIBRARIAN, UserRole.READER),
    ORDER_BOOK(OrderBookCommand.getInstance(), UserRole.ADMIN, UserRole.LIBRARIAN, UserRole.READER),
    APPROVE(ApproveOrderCommand.getInstance(), UserRole.ADMIN, UserRole.LIBRARIAN),
    SHOW_ADD_BOOK_PAGE(ShowAddBookPageCommand.getInstance(), UserRole.ADMIN),
    RETURN_BOOK(ReturnBookCommand.getInstance(), UserRole.ADMIN, UserRole.LIBRARIAN, UserRole.READER),
    SHOW_ACCOUNT(ShowAccountCommand.getInstance(), UserRole.ADMIN, UserRole.LIBRARIAN, UserRole.READER),
    CHANGE_ACCOUNT(ChangeAccountCommand.getInstance(), UserRole.ADMIN, UserRole.LIBRARIAN, UserRole.READER),
    FIND(FindBooksCommand.getInstance(), UserRole.ADMIN, UserRole.LIBRARIAN, UserRole.READER),
    SHOW_USER_ORDERS(ShowUserOrdersPageCommand.getInstance(), UserRole.ADMIN, UserRole.LIBRARIAN, UserRole.READER);

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
