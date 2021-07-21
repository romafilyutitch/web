package by.epam.jwd.web.exception;

public class DAOException extends RuntimeException{

    public DAOException(Throwable cause) {
        super(cause);
    }

    public DAOException(String message) {
        super(message);
    }

}
