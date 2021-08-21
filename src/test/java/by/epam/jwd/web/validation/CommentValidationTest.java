package by.epam.jwd.web.validation;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Comment;
import by.epam.jwd.web.model.Genre;
import by.epam.jwd.web.model.User;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class CommentValidationTest {
    private final CommentValidation testValidation = CommentValidation.getInstance();

    @Test
    public void getInstance_mustReturnSameInstance() {
        final CommentValidation instance = CommentValidation.getInstance();
        assertSame(testValidation, instance);
    }

    @Test
    public void validate_mustReturnEmptyList_whenValidCommentPassed() {
        final User user = new User("login", "password");
        final Book book = new Book("name", "author", Genre.SCIENCE, 1, "text");
        final Comment validComment = new Comment(user, book, "text");
        final List<String> validateResult = testValidation.validate(validComment);
        assertTrue(validateResult.isEmpty());
    }

    @Test
    public void validate_mustReturnNotEmptyList_whenCommentWithInvalidTextPassed() {
        final String invalidText = "";
        final User user = new User("login", "password");
        final Book book = new Book("name", "author", Genre.SCIENCE, 1, "text");
        final Comment invalidComment = new Comment(user, book, invalidText);
        final List<String> validateResult = testValidation.validate(invalidComment);
        assertFalse(validateResult.isEmpty());
    }

}