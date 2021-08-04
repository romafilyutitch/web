package by.epam.jwd.web.validation;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Comment;
import by.epam.jwd.web.model.DbEntity;
import by.epam.jwd.web.model.Subscription;
import by.epam.jwd.web.model.User;

import java.util.List;

public interface Validation<T extends DbEntity> {

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
