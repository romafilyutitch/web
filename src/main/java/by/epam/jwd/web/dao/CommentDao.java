package by.epam.jwd.web.dao;

import by.epam.jwd.web.model.Comment;

import java.util.List;

public interface CommentDao extends Dao<Comment> {
    List<Comment> findByBookId(Long bookId);
}
