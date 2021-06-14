package by.epam.jwd.web.validator;

import by.epam.jwd.web.exception.ValidationException;

public interface Validator<T> {
    void validate(T validatedObject) throws ValidationException;
}
