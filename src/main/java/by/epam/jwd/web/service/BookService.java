package by.epam.jwd.web.service;

import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Comment;
import by.epam.jwd.web.model.Genre;
import by.epam.jwd.web.model.User;

import java.util.List;
import java.util.Optional;

public interface BookService extends Service<Book> {

    void addOneCopy(Book book);

    void removeOneCopy(Book book);

    Optional<Book> findByName(String name);

    List<Book> findByGenre(Genre genre);

    void addComment(Comment comment);

    void addLike(Book book, User user);

    void removeLike(Book book, User user);

    boolean isLikedByUser(Book book, User user);
}
