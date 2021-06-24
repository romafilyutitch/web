package by.epam.jwd.web.exception;

public class DAOException extends RuntimeException{

    public DAOException (String message, Throwable cause) {
        super(message, cause);
    }

    public DAOException(String message) {
        super(message);
    }

}
