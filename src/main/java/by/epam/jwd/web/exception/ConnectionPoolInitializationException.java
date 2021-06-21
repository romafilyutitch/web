package by.epam.jwd.web.exception;

public class ConnectionPoolInitializationException extends Exception {
    public ConnectionPoolInitializationException() {}

    public ConnectionPoolInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConnectionPoolInitializationException(String message) {
        super(message);
    }

    public ConnectionPoolInitializationException(Throwable cause) {
        super(cause);
    }
}
