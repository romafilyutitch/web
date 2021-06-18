package by.epam.jwd.web.exception;

public class ConnectionPoolActionException extends RuntimeException {
    public ConnectionPoolActionException() {}

    public ConnectionPoolActionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectionPoolActionException(String message) {
        super(message);
    }

    public ConnectionPoolActionException(Throwable cause) {
        super(cause);
    }
}
