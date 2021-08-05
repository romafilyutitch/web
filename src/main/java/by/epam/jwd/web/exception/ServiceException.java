package by.epam.jwd.web.exception;

/**
 * Service layer exception.
 * Throws when problems in services occur (Saved entity was not found by id for example).
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class ServiceException extends RuntimeException {

    /**
     * Exception constructor.
     * @param message exception message.
     */
    public ServiceException(String message) {
        super(message);
    }
}
