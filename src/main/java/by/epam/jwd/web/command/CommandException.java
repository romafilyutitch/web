package by.epam.jwd.web.command;

public class CommandException extends Exception {
    public CommandException() {
    }

    public CommandException(String message, Throwable cause) {
        super(message, cause);
    }

    public CommandException(String message) {
        super(message);
    }

    public CommandException(Throwable cause) {
        super(cause);
    }
}
