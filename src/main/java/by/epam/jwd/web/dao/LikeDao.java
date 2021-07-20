package by.epam.jwd.web.dao;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Like;
import by.epam.jwd.web.model.User;

import java.util.List;
import java.util.Optional;

public interface LikeDao extends Dao<Like> {
    Optional<Like> findByUserAndBook(User user, Book book);
}
