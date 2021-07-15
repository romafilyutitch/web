package by.epam.jwd.web.service;

import by.epam.jwd.web.exception.RegisterException;
import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.model.Author;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Comment;
import by.epam.jwd.web.model.Genre;
import by.epam.jwd.web.model.User;

import java.util.List;
import java.util.Optional;

public interface BookService extends Service<Book> {

    Book addOneCopy(Long bookId);

    Book removeOneCopy(Long bookId);

    Optional<Book> findByName(String name);

    List<Book> findByGenre(Genre genre);

    Book addComment(Comment comment);

    void addLike(Book book, User user);

    boolean isLikedByUser(Book book, User user);
}
