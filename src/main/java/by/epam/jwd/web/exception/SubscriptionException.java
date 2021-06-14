package by.epam.jwd.web.exception;

public class SubscriptionException extends Exception {
    public SubscriptionException() {}

    public SubscriptionException(String message, Throwable cause) {
        super(message, cause);
    }

    public SubscriptionException(String message) {
        super(message);
    }

    public SubscriptionException(Throwable cause) {
        super(cause);
    }
}
