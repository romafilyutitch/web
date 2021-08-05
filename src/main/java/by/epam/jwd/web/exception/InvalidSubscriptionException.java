package by.epam.jwd.web.exception;

/**
 * Exception class to note that {@link by.epam.jwd.web.model.Subscription} is
 * invalid if subscription date range is invalid (if start date if after end date).
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class InvalidSubscriptionException extends Exception {
    /**
     * Exception constructor.
     * @param message exception message.
     */
    public InvalidSubscriptionException(String message) {
        super(message);
    }
}
