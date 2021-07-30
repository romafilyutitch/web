package by.epam.jwd.web.command;

import by.epam.jwd.web.model.UserRole;

import java.util.Arrays;
import java.util.List;

public enum CommandEnum {
    SHOW_LOGIN(ShowLoginCommand.getInstance(), UserRole.UNAUTHORIZED),
    SHOW_USERS(ShowUsersListCommand.getInstance(), UserRole.ADMIN),
    SHOW_BOOKS(ShowBooksListCommand.getInstance(), UserRole.ADMIN, UserRole.LIBRARIAN),
    SHOW_ORDERS(ShowOrdersListCommand.getInstance(), UserRole.ADMIN, UserRole.LIBRARIAN),
    SHOW_REGISTER(ShowRegisterCommand.getInstance(), UserRole.UNAUTHORIZED),
    SHOW_ACCOUNT(ShowAccountCommand.getInstance(), UserRole.ADMIN, UserRole.LIBRARIAN, UserRole.READER),
    SHOW_USER_ORDERS(ShowUserOrdersPageCommand.getInstance(), UserRole.ADMIN, UserRole.LIBRARIAN, UserRole.READER),
    MAIN(MainCommand.getInstance(), UserRole.UNAUTHORIZED, UserRole.READER, UserRole.LIBRARIAN, UserRole.ADMIN),
    LOGIN(LoginCommand.getInstance(), UserRole.UNAUTHORIZED),
    LOGOUT(LogoutCommand.getInstance(), UserRole.READER, UserRole.ADMIN, UserRole.LIBRARIAN),
    REGISTER(RegisterCommand.getInstance(), UserRole.UNAUTHORIZED),
    FIND_BOOK_BY_NAME(FindBookByNameCommand.getInstance(), UserRole.UNAUTHORIZED, UserRole.READER, UserRole.LIBRARIAN, UserRole.ADMIN),
    FIND_FICTION(FindFictionCommand.getInstance(), UserRole.UNAUTHORIZED, UserRole.READER, UserRole.LIBRARIAN, UserRole.ADMIN),
    FIND_FANTASY(FindFantasyCommand.getInstance(), UserRole.UNAUTHORIZED, UserRole.READER, UserRole.LIBRARIAN, UserRole.ADMIN),
    FIND_SCIENCE(FindScienceCommand.getInstance(), UserRole.UNAUTHORIZED, UserRole.READER, UserRole.LIBRARIAN, UserRole.ADMIN),
    ADD_COPY(AddCopyCommand.getInstance(), UserRole.ADMIN, UserRole.LIBRARIAN),
    REMOVE_COPY(RemoveCopyCommand.getInstance(), UserRole.ADMIN, UserRole.LIBRARIAN),
    ADD_BOOK(AddBookCommand.getInstance(), UserRole.ADMIN, UserRole.LIBRARIAN),
    PROMOTE_ROLE(PromoteRoleCommand.getInstance(), UserRole.ADMIN),
    DEMOTE_ROLE(DemoteRoleCommand.getInstance(), UserRole.ADMIN),
    DELETE_BOOK(DeleteBookCommand.getInstance(), UserRole.ADMIN, UserRole.LIBRARIAN),
    CHANGE_LOGIN(ChangeLoginCommand.getInstance(), UserRole.READER, UserRole.LIBRARIAN, UserRole.ADMIN),
    CHANGE_PASSWORD(ChangePasswordCommand.getInstance(), UserRole.READER, UserRole.LIBRARIAN, UserRole.ADMIN),
    DELETE_ORDER(DeleteOrderCommand.getInstance(), UserRole.ADMIN, UserRole.LIBRARIAN),
    DELETE_ACCOUNT(DeleteAccountCommand.getInstance(), UserRole.ADMIN, UserRole.LIBRARIAN, UserRole.READER),
    SET_SUBSCRIPTION(SetSubscriptionCommand.getInstance(), UserRole.ADMIN),
    ORDER_BOOK(OrderBookCommand.getInstance(), UserRole.ADMIN, UserRole.LIBRARIAN, UserRole.READER),
    APPROVE_ORDER(ApproveOrderCommand.getInstance(), UserRole.ADMIN, UserRole.LIBRARIAN),
    RETURN_BOOK(ReturnBookCommand.getInstance(), UserRole.ADMIN, UserRole.LIBRARIAN, UserRole.READER),
    SET_LOCALE(SetLocaleCommand.getInstance(), UserRole.UNAUTHORIZED, UserRole.ADMIN, UserRole.LIBRARIAN, UserRole.READER),
    ADD_LIKE(AddLikeCommand.getInstance(), UserRole.READER, UserRole.LIBRARIAN, UserRole.ADMIN),
    DELETE_COMMENT(DeleteCommentCommand.getInstance(), UserRole.READER, UserRole.LIBRARIAN, UserRole.ADMIN),
    ADD_COMMENT(AddCommentCommand.getInstance(), UserRole.READER, UserRole.LIBRARIAN, UserRole.ADMIN);

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
