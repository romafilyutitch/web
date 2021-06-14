package by.epam.jwd.web.validator;

import by.epam.jwd.web.exception.ValidationException;
import by.epam.jwd.web.model.Book;

public class BookValidator implements Validator<Book> {

    private BookValidator() {}

    public static BookValidator getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public void validate(Book validatedObject) throws ValidationException {
        if (validatedObject.getName().isEmpty()) {
            throw new ValidationException("Name is empty");
        }
        if (validatedObject.getAuthor().getName().isEmpty()) {
            throw new ValidationException("Author is empty");
        }
        if (validatedObject.getGenre().getName().isEmpty()) {
            throw new ValidationException("Genre is empty");
        }
        if (validatedObject.getDate() == null) {
            throw new ValidationException("Date id empty");
        }
        if (validatedObject.getPagesAmount() <= 0) {
            throw new ValidationException("Pages amount is negative");
        }
        if (validatedObject.getCopiesAmount() < 0) {
            throw new ValidationException("Copies amount is negative");
        }
        if (validatedObject.getDescription().isEmpty()) {
            throw new ValidationException("Description is empty");
        }
        if (validatedObject.getText().isEmpty()) {
            throw new ValidationException("Text is empty");
        }
    }

    private static class Singleton {
        private static final BookValidator INSTANCE = new BookValidator();
    }
}
