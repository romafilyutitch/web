package by.epam.jwd.web.connectionPool;

/**
 * Exception for connection pool layer.
 * Throws when it is impossible to make connection pool initialization.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class ConnectionPoolInitializationException extends Exception {

    /**
     * Exception constructor.
     * @param message exception message.
     * @param cause exception cause.
     */
    public ConnectionPoolInitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Exception constructor.
     * @param cause exception cause.
     */
    public ConnectionPoolInitializationException(Throwable cause) {
        super(cause);
    }

}
