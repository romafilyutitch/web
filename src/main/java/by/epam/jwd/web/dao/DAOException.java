package by.epam.jwd.web.dao;

class DAOException extends RuntimeException{
    public DAOException() {

    }

    public DAOException (String message, Throwable cause) {
        super(message, cause);
    }

    public DAOException(String message) {
        super(message);
    }

    public DAOException(Throwable cause) {
        super(cause);
    }
}
