package by.epam.jwd.web.validation.impl;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Comment;
import by.epam.jwd.web.model.Subscription;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.validation.Validation;
import by.epam.jwd.web.validation.ValidationFactory;

/**
 * Validation Factory implementation. Produces validation implementations for definite entities.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class EntitiesValidationFactory implements ValidationFactory {

    private EntitiesValidationFactory() {}

    /**
     * Get single class instance from nested class.
     * @return class instance.
     */
    public static EntitiesValidationFactory getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Produces user validation implementation.
     * @return user validation instance.
     */
    @Override
    public Validation<User> getUserValidation() {
        return UserValidation.getInstance();
    }

    /**
     * Produces comment validation implementation.
     * @return comment validation instance.
     */
    @Override
    public Validation<Comment> getCommentValidation() {
        return CommentValidation.getInstance();
    }

    /**
     * Produces book validation implementation.
     * @return book validation instance.
     */
    @Override
    public Validation<Book> getBookValidation() {
        return BookValidation.getInstance();
    }

    /**
     * Produces subscription implementation.
     * @return subscription validation instance.
     */
    @Override
    public Validation<Subscription> getSubscriptionValidation() {
        return SubscriptionValidation.getInstance();
    }

    /**
     * Nested class that encapsulates single {@link EntitiesValidationFactory} instance.
     * Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final EntitiesValidationFactory INSTANCE = new EntitiesValidationFactory();
    }
}
