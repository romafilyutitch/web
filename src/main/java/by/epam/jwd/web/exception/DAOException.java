package by.epam.jwd.web.exception;

public class DAOException extends RuntimeException{

    public DAOException (String message, Throwable cause) {
        super(message, cause);
    }

    public DAOException(Throwable cause) {
        super(cause);
    }

    public DAOException(String message) {
        super(message);
    }

}
