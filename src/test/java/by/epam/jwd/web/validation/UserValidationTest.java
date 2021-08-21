package by.epam.jwd.web.validation;

import by.epam.jwd.web.model.User;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class UserValidationTest {
    private final UserValidation testValidation = UserValidation.getInstance();

    @Test
    public void getInstance_mustReturnSameInstance() {
        final UserValidation instance = UserValidation.getInstance();
        assertSame(testValidation, instance);
    }

    @Test
    public void validate_returnsEmptyList_forValidUser() {
        final User validUser = new User("login", "password");
        final List<String> validateResult = testValidation.validate(validUser);
        assertTrue(validateResult.isEmpty());
    }

    @Test
    public void validate_returnsNotEmptyList_ifUserWithInvalidLoginPassed() {
        final String invalidString = "invalidinvalidinvalidString";
        final User invalidUser = new User(invalidString, "password");
        final List<String> validateResult = testValidation.validate(invalidUser);
        assertFalse(validateResult.isEmpty());
    }

    @Test
    public void validate_returnsNotEmptyList_ifUserWithInvalidPasswordPassed() {
        final String invalidPassword = "invalidinvalidinvalidPassword";
        final User invalidUser = new User("login", invalidPassword);
        final List<String> validateResult = testValidation.validate(invalidUser);
        assertFalse(validateResult.isEmpty());
    }
}