package by.epam.jwd.web.exception;

/**
 * Service layer exception.
 * Throws when user wants to login but there is not user with entered login.
 * That means tha user must to enter write password to login.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class WrongLoginException extends Exception {
}
