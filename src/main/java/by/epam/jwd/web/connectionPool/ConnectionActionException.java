package by.epam.jwd.web.connectionPool;

/**
 * Connection pool layer exception.
 * Throws when thread that takes free connection from pool is interrupted or
 * thread that puts used connection to poll is interrupted.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class ConnectionActionException extends RuntimeException {

    /**
     * Exception constructor.
     * @param cause exception cause.
     */
    public ConnectionActionException(Throwable cause) {
        super(cause);
    }

}
