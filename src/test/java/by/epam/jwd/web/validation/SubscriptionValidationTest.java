package by.epam.jwd.web.validation;

import by.epam.jwd.web.model.Subscription;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class SubscriptionValidationTest {
    private final SubscriptionValidation testValidation = SubscriptionValidation.getInstance();


    @Test
    public void getInstance_mustReturnSameInstance() {
        final SubscriptionValidation instance = SubscriptionValidation.getInstance();
        assertSame(testValidation, instance);
    }

    @Test
    public void validate_mustReturnEmptyList_whenValidSubscriptionPassed() {
        final Subscription validSubscription = new Subscription(LocalDate.now().minusDays(10), LocalDate.now().plusDays(10));
        final List<String> validateResult = testValidation.validate(validSubscription);
        assertTrue(validateResult.isEmpty());
    }

    @Test
    public void validate_mustReturnNotEmptyList_whenInvalidSubscriptionPassed() {
        final LocalDate invalidStartDate = LocalDate.now();
        final LocalDate validEndDate = invalidStartDate.minusDays(10);
        final Subscription invalidSubscription = new Subscription(invalidStartDate, validEndDate);
        final List<String> validationResult = testValidation.validate(invalidSubscription);
        assertFalse(validationResult.isEmpty());
    }
}