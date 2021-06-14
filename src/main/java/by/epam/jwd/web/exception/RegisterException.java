package by.epam.jwd.web.exception;

public class RegisterException extends Exception {
    public RegisterException() {}

    public RegisterException(String message, Throwable cause) {
        super(message, cause);
    }

    public RegisterException(String message) {
        super(message);
    }

    public RegisterException(Throwable cause) {
        super(cause);
    }
}
