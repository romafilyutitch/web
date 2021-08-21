package by.epam.jwd.web.validation;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;
import by.epam.jwd.web.resource.MessageManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Book validation class. Validates add book form data
 * when user adds new book. Uses regular expression language to
 * validation book name and author name.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
class BookValidation implements Validation<Book> {

    private static final String INVALID_BOOK_NAME_MESSAGE_KEY = "book.validation.name.invalid";
    private static final String INVALID_AUTHOR_NAME_MESSAGE_KEY = "book.validation.author.invalid";
    private static final String INVALID_TEXT_MESSAGE_KEY = "book.validation.text.invalid";
    private static final String INVALID_GENRE_MESSAGE_KEY = "book.validation.genre.invalid";
    private static final String INVALID_PAGES_AMOUNT_MESSAGE_KEY = "book.validation.pages.invalid";
    private static final String REGEX_PATTERN = "\\w+(\\w|\\s)*";

    private BookValidation() {
    }

    /**
     * Gets single class instance from nested class.
     * @return class instance.
     */
    public static BookValidation getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Makes book validation and form invalid messages.
     * Book is valid if has name and author that consists of 10 or less characters,
     * has Definite genre, has positive pages amount and not empty text.
     * @param book book instance that need to be validated.
     * @return validation messages list. If list is empty that means
     * that entity is valid.
     */
    @Override
    public List<String> validate(Book book) {
        final List<String> messages = new ArrayList<>();
        final String name = book.getName();
        final String authorName = book.getAuthor();
        final Genre genre = book.getGenre();
        final int pagesAmount = book.getPagesAmount();
        final String text = book.getText();
        if (name == null || name.isEmpty() || !name.matches(REGEX_PATTERN)) {
            messages.add(MessageManager.getMessage(INVALID_BOOK_NAME_MESSAGE_KEY));
        }
        if (authorName == null || authorName.isEmpty() || !authorName.matches(REGEX_PATTERN)) {
            messages.add(MessageManager.getMessage(INVALID_AUTHOR_NAME_MESSAGE_KEY));
        }
        if (text == null || text.isEmpty()) {
            messages.add(MessageManager.getMessage(INVALID_TEXT_MESSAGE_KEY));
        }
        if (genre == null) {
            messages.add(MessageManager.getMessage(INVALID_GENRE_MESSAGE_KEY));
        }
        if (pagesAmount <= 0) {
            messages.add(MessageManager.getMessage(INVALID_PAGES_AMOUNT_MESSAGE_KEY));
        }
        return messages;
    }

    /**
     * Nested class that encapsulates single {@link BookValidation} instance.
     * Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final BookValidation INSTANCE = new BookValidation();
    }
}
