package by.epam.jwd.web.validation;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class BookValidationTest {
    private final BookValidation testValidation = BookValidation.getInstance();

    @Test
    public void getInstance_mustReturnSameInstance() {
        final BookValidation instance = BookValidation.getInstance();
        assertSame(testValidation, instance);
    }

    @Test
    public void validate_mustReturnEmptyList_whenValidBookPassed() {
        final Book validBook = new Book("name", "author", Genre.FANTASY, 1, "text");
        final List<String> validateResult = testValidation.validate(validBook);
        assertTrue(validateResult.isEmpty());
    }

    @Test
    public void validate_mustReturnNotEmptyList_whenBookWithInvalidNamePassed() {
        final String invalidName = "";
        final Book invalidBook = new Book(invalidName, "author", Genre.FANTASY, 1, "text");
        final List<String> validateResult = testValidation.validate(invalidBook);
        assertFalse(validateResult.isEmpty());
    }

    @Test
    public void validate_mustReturnNotEmptyList_whenBookWithInvalidAuthorPassed() {
        final String invalidAuthor = "";
        final Book invalidBook = new Book("name", invalidAuthor, Genre.FANTASY, 1, "text");
        final List<String> validateResult = testValidation.validate(invalidBook);
        assertFalse(validateResult.isEmpty());
    }

    @Test
    public void validate_mustReturnNotEmptyList_whenBookWithInvalidGenrePassed() {
        final Genre invalidGenre = null;
        final Book invalidBook = new Book("name", "author", invalidGenre, 1, "text");
        final List<String> validateResult = testValidation.validate(invalidBook);
        assertFalse(validateResult.isEmpty());
    }

    @Test
    public void validate_mustReturnNotEmptyList_whenBookWithInvalidPagesAmountPassed() {
        final int invalidPagesAmount = 0;
        final Book invalidBook = new Book("name", "author", Genre.FANTASY, invalidPagesAmount, "text");
        final List<String> validateResult = testValidation.validate(invalidBook);
        assertFalse(validateResult.isEmpty());
    }

    @Test
    public void validate_mustReturnNotEmptyList_whenBookWithInvalidTextPassed() {
        final String invalidText = "";
        final Book invalidBook = new Book("name", "author", Genre.FANTASY, 1, invalidText);
        final List<String> validateResult = testValidation.validate(invalidBook);
        assertFalse(validateResult.isEmpty());
    }
}