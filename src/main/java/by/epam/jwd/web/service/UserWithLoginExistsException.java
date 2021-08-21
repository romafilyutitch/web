package by.epam.jwd.web.service;

/**
 * Exception for service layer.
 * Throws when user wants to register or change login and enters login that already
 * exists in database. That means that user cannot register with
 * entered login and must enter another login.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class UserWithLoginExistsException extends Exception {
}
