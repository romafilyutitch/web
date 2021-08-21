package by.epam.jwd.web.validation;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Comment;
import by.epam.jwd.web.model.DbEntity;
import by.epam.jwd.web.model.Subscription;
import by.epam.jwd.web.model.User;

import java.util.List;

/**
 * Validation interface that defines entity validation behavior.
 * Validate entity and forms invalid messages list. If
 * invalid message list is empty that means tha entity is valid.
 * @param <T> entity type that need to be validated.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public interface Validation<T extends DbEntity> {
    /**
     * Makes entity validation and forms invalid messages.
     * If invalid message list is empty thar means that entity is
     * valid. Empty messages list will be returned if validated entity
     * is valid or not empty message list will be returned otherwise.
     * @param entity entity that need to be validated.
     * @return list of invalid messages. If entity is valid then empty list will be returned.
     * Not empty list will be returned if validated entity is invalid.
     */
    List<String> validate(T entity);

    /**
     * Returns validation for user what makes possible to validate user
     * @return validation for user.
     */
    static Validation<User> getUserValidation() {
        return UserValidation.getInstance();
    }

    /**
     * Returns validation for book what makes possible to validate book
     * @return validation for book
     */
    static Validation<Book> getBookValidation() {
        return BookValidation.getInstance();
    }

    /**
     * Returns validation for subscription what makes possible to validate subscription
     * @return validation for subscription
     */
    static Validation<Subscription> getSubscriptionValidation() {
        return SubscriptionValidation.getInstance();
    }

    /**
     * Returns validation for comment what makes possible to validate comment
     * @return validation for comment
     */
    static Validation<Comment> getCommentValidation() {
        return CommentValidation.getInstance();
    }
}
