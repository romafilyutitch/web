package by.epam.jwd.web.validation;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Comment;
import by.epam.jwd.web.model.DbEntity;
import by.epam.jwd.web.model.Subscription;
import by.epam.jwd.web.model.User;
import by.epam.jwd.web.validation.impl.BookValidation;
import by.epam.jwd.web.validation.impl.CommentValidation;
import by.epam.jwd.web.validation.impl.SubscriptionValidation;
import by.epam.jwd.web.validation.impl.UserValidation;

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
     * valid.
     * @param entity entity that need to be validated.
     * @return list of invalid messages.
     */
    List<String> validate(T entity);

    static Validation<User> getUserValidation() {
        return UserValidation.getInstance();
    }

    static Validation<Subscription> getSubscriptionValidation() {
        return SubscriptionValidation.getInstance();
    }

    static Validation<Book> getBookValidation() {
        return BookValidation.getInstance();
    }

    static Validation<Comment> getCommentValidation() {
        return CommentValidation.getInstance();
    }
}
