package by.epam.jwd.web.service;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Like;
import by.epam.jwd.web.model.User;

import java.util.Optional;

public interface LikeService extends Service<Like> {
    Optional<Like> findByUserAndBook(User user, Book book);
}
