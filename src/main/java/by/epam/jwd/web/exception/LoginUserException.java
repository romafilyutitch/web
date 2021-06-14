package by.epam.jwd.web.exception;

public class LoginUserException extends Exception {
    public LoginUserException() {}

    public LoginUserException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginUserException(String message) {
        super(message);
    }

    public LoginUserException(Throwable cause) {
        super(cause);
    }
}
