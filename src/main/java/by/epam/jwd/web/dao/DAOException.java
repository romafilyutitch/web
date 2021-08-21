package by.epam.jwd.web.dao;

/**
 * Exception for Data access object layer.
 * Throws when {@link java.sql.SQLException} occurs during dao execution.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class DAOException extends RuntimeException{

    /**
     * Exception constructor.
     * @param cause exception cause.
     */
    public DAOException(Throwable cause) {
        super(cause);
    }

    /**
     * Exception constructor.
     * @param message exception message.
     */
    public DAOException(String message) {
        super(message);
    }

}
