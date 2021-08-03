package by.epam.jwd.web.validator;

import by.epam.jwd.web.model.Book;

public class BookValidator implements Validator<Book> {
    private static final String BOOK_VALIDATION_REGEX_PATTERN = "\\w(\\w|\\s)+";

    private BookValidator() {
    }

    public static BookValidator getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public boolean validate(Book book) {
        final String bookName = book.getName();
        final String bookAuthorName = book.getAuthor().getName();
        final int copiesAmount = book.getCopiesAmount();
        final int pagesAmount = book.getPagesAmount();
        final int commentsAmount = book.getCommentsAmount();
        final int likesAmount = book.getLikesAmount();
        return bookName.matches(BOOK_VALIDATION_REGEX_PATTERN) && bookAuthorName.matches(BOOK_VALIDATION_REGEX_PATTERN)
                && isValidBookAmounts(copiesAmount, pagesAmount, commentsAmount, likesAmount);
    }

    private boolean isValidBookAmounts(int copiesAmount, int pagesAmount, int commentsAmount, int likesAmount) {
        return copiesAmount >= 0 && pagesAmount >= 0 && commentsAmount >= 0 && likesAmount >= 0;
    }

    private static class Singleton {
        private static final BookValidator INSTANCE = new BookValidator();
    }

}
