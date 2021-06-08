package by.epam.jwd.web.command;

import by.epam.jwd.web.command.ActionCommand;
import by.epam.jwd.web.command.LoginCommand;
import by.epam.jwd.web.command.LogoutCommand;

public enum CommandEnum {
    SHOW_LOGIN(new ShowLoginCommand()),
    LOGIN(new LoginCommand()),
    LOGOUT(new LogoutCommand()),
    SHOW_USERS(new ShowUsersListCommand()),
    MAIN(new MainCommand()),
    SHOW_REGISTER(new ShowRegisterCommand()),
    REGISTER(new RegisterCommand());

    ActionCommand command;

    CommandEnum(ActionCommand command) {
        this.command = command;
    }
    public ActionCommand getCurrentCommand() {
        return command;
    }
}
