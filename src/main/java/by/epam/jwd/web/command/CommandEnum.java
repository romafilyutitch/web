package by.epam.jwd.web.command;

import by.epam.jwd.web.command.action.book.AddBookCommand;
import by.epam.jwd.web.command.action.book.AddCopyCommand;
import by.epam.jwd.web.command.action.book.DeleteBookCommand;
import by.epam.jwd.web.command.action.book.find.FindBookByNameCommand;
import by.epam.jwd.web.command.action.book.find.FindFantasyCommand;
import by.epam.jwd.web.command.action.book.find.FindFictionCommand;
import by.epam.jwd.web.command.action.book.find.FindScienceCommand;
import by.epam.jwd.web.command.action.book.RemoveCopyCommand;
import by.epam.jwd.web.command.action.comment.AddCommentCommand;
import by.epam.jwd.web.command.action.comment.DeleteCommentCommand;
import by.epam.jwd.web.command.action.like.AddLikeCommand;
import by.epam.jwd.web.command.action.order.ApproveOrderCommand;
import by.epam.jwd.web.command.action.order.DeleteOrderCommand;
import by.epam.jwd.web.command.action.order.OrderBookCommand;
import by.epam.jwd.web.command.action.order.ReturnBookCommand;
import by.epam.jwd.web.command.action.subscription.SetSubscriptionCommand;
import by.epam.jwd.web.command.action.user.ChangeLoginCommand;
import by.epam.jwd.web.command.action.user.ChangePasswordCommand;
import by.epam.jwd.web.command.action.user.DeleteAccountCommand;
import by.epam.jwd.web.command.action.user.DemoteRoleCommand;
import by.epam.jwd.web.command.action.user.LoginCommand;
import by.epam.jwd.web.command.action.user.LogoutCommand;
import by.epam.jwd.web.command.action.user.PromoteRoleCommand;
import by.epam.jwd.web.command.action.user.RegisterCommand;
import by.epam.jwd.web.command.language.SetLanguageCommand;
import by.epam.jwd.web.command.navigation.MainCommand;
import by.epam.jwd.web.command.navigation.ShowAccountCommand;
import by.epam.jwd.web.command.navigation.ShowBooksListCommand;
import by.epam.jwd.web.command.navigation.ShowLoginCommand;
import by.epam.jwd.web.command.navigation.ShowOrdersListCommand;
import by.epam.jwd.web.command.navigation.ShowRegisterCommand;
import by.epam.jwd.web.command.navigation.ShowUserOrdersPageCommand;
import by.epam.jwd.web.command.navigation.ShowUsersListCommand;
import by.epam.jwd.web.model.UserRole;

import java.util.Arrays;
import java.util.List;

/**
 * Enumeration that contains all commands implementations.
 * Used by {@link CommandFactory} to choose needed command to
 * perform command.
 * Also permission based of current user role is defined for
 * each command.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
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
    SET_LANGUAGE(SetLanguageCommand.getInstance(), UserRole.UNAUTHORIZED, UserRole.ADMIN, UserRole.LIBRARIAN, UserRole.READER),
    ADD_LIKE(AddLikeCommand.getInstance(), UserRole.READER, UserRole.LIBRARIAN, UserRole.ADMIN),
    DELETE_COMMENT(DeleteCommentCommand.getInstance(), UserRole.READER, UserRole.LIBRARIAN, UserRole.ADMIN),
    ADD_COMMENT(AddCommentCommand.getInstance(), UserRole.READER, UserRole.LIBRARIAN, UserRole.ADMIN);

    private final ActionCommand command;
    private final List<UserRole> allowedRoles;

    /**
     * Constructor that defines command and role of user
     * that may use that command.
     * @param command request command
     * @param roles role of users that may to use this command
     */
    CommandEnum(ActionCommand command, UserRole... roles) {
        this.command = command;
        this.allowedRoles = roles != null ? Arrays.asList(roles) : UserRole.rolesAsList();
    }

    /**
     * Returns all user roles that may use current command.
     * Used to define whether user with its role execute current command.
     * @return user roles that may use current command.
     */
    public List<UserRole> getAllowedRoles() {
        return allowedRoles;
    }

    /**
     * Returns current {@link ActionCommand} instance.
     * Used by {@link CommandFactory} to get {@link ActionCommand} instance for
     * command pattern to execute command.
     * @return current enum command.
     */
    public ActionCommand getCurrentCommand() {
        return command;
    }
}
