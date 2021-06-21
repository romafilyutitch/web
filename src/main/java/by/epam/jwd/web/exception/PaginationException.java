package by.epam.jwd.web.exception;

public class PaginationException extends Exception {
    public PaginationException() {}

    public PaginationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PaginationException(String message) {
        super(message);
    }

    public PaginationException(Throwable cause) {
        super(cause);
    }
}
