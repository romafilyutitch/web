package by.epam.jwd.web.validation;

import by.epam.jwd.web.model.Comment;
import by.epam.jwd.web.resource.MessageManager;

import java.util.ArrayList;
import java.util.List;

class CommentValidation implements Validation<Comment> {

    private static final String INVALID_COMMENT_TEXT_MESSAGE_KEY = "comment.validation.text.invalid";

    private CommentValidation() {
    }

    static CommentValidation getInstance() {
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
