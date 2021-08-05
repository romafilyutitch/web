package by.epam.jwd.web.validation;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Comment;
import by.epam.jwd.web.model.Subscription;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.validation.impl.EntitiesValidationFactory;

/**
 * Validation factory interface defines validation factory behavior.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public interface ValidationFactory {
    /**
     * Returns for user validation interface implementation.
     * @return user validation instance.
     */
    Validation<User> getUserValidation();

    /**
     * Returns book validation interface implementation.
     * @return book validation instance.
     */
    Validation<Book> getBookValidation();

    /**
     * Returns comment validation interface implementation.
     * @return comment validation instance.
     */
    Validation<Comment> getCommentValidation();

    /**
     * Returns subscription validation interface implementation.
     * @return subscription validation instance.
     */
    Validation<Subscription> getSubscriptionValidation();

    /**
     * Returns validation factory interface implementation.
     * @return validation factory implementation.
     */
    static ValidationFactory getInstance() {
        return EntitiesValidationFactory.getInstance();
    }
}
