package by.epam.jwd.web.validation.impl;

import by.epam.jwd.web.model.Comment;
import by.epam.jwd.web.resource.MessageManager;
import by.epam.jwd.web.validation.Validation;

import java.util.ArrayList;
import java.util.List;

public class CommentValidation implements Validation<Comment> {

    private static final String INVALID_COMMENT_TEXT_MESSAGE_KEY = "comment.validation.text.invalid";

    private CommentValidation() {
    }

    public static CommentValidation getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public List<String> validate(Comment comment) {
        final List<String> messages = new ArrayList<>();
        final String commentText = comment.getText();
        if (commentText == null || commentText.isEmpty()) {
            messages.add(MessageManager.getMessage(INVALID_COMMENT_TEXT_MESSAGE_KEY));
        }
        return messages;
    }

    private static class Singleton {
        private static final CommentValidation INSTANCE = new CommentValidation();
    }
}
