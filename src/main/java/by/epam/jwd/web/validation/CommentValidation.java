package by.epam.jwd.web.validation.impl;

import by.epam.jwd.web.model.Comment;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.validation.Validation;

import java.util.ArrayList;
import java.util.List;

/**
 * Validation of comment class than makes validation that comment not empty.
 * @author roma0
 * @version 1.0
 * @since 1.0
 */
public class CommentValidation implements Validation<Comment> {

    private static final String INVALID_COMMENT_TEXT_MESSAGE_KEY = "comment.validation.text.invalid";

    private CommentValidation() {
    }

    /**
     * Gets single class instance from nested class.
     * @return class instance.
     */
    public static CommentValidation getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * Makes comment validation. Comment text must be not empty.
     * Forms invalid message list.
     * @param comment comment that need to be validated.
     * @return validation message list. If list is empty that means
     * that entity is valid.
     */
    @Override
    public List<String> validate(Comment comment) {
        final List<String> messages = new ArrayList<>();
        final String commentText = comment.getText();
        if (commentText == null || commentText.isEmpty()) {
            messages.add(MessageManager.getMessage(INVALID_COMMENT_TEXT_MESSAGE_KEY));
        }
        return messages;
    }

    /**
     * Nested class that encapsulates single {@link CommentValidation} instance.
     * Singleton pattern variation.
     * @see "Singleton pattern"
     */
    private static class Singleton {
        private static final CommentValidation INSTANCE = new CommentValidation();
    }
}
