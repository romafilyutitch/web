package by.epam.jwd.web.exception;

public class ConnectionPoolActionException extends RuntimeException {
    public ConnectionPoolActionException(String message, Throwable cause) {
        super(message, cause);
    }
}
