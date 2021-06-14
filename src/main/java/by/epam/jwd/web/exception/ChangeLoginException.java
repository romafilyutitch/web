package by.epam.jwd.web.exception;

public class ChangeLoginException extends Exception {
    public ChangeLoginException() {}

    public ChangeLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChangeLoginException(String message) {
        super(message);
    }

    public ChangeLoginException(Throwable cause) {
        super(cause);
    }
}
