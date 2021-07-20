package by.epam.jwd.web.service;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Comment;

import java.util.List;

public interface CommentService extends Service<Comment>{
    List<Comment> findByBook(Book book);
}
