package by.epam.jwd.web.dao;



import by.epam.jwd.web.model.Author;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;

import java.util.List;
import java.util.Optional;

public interface BookDao extends Dao<Book> {
    Optional<Book> findByName(String name);

    List<Book> findByAuthorId(Long authorId);

    List<Book> findByGenreId(Long genreId);

    void addLike(Long bookId, Long userId);

    void removeLike(Long bookId, Long userId);

    boolean isLikedByUserWithId(Long bookId, Long userId);

}
