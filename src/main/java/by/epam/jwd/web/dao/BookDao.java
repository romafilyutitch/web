package by.epam.jwd.web.dao;



import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;

import java.util.List;
import java.util.Optional;

public interface BookDao extends Dao<Book> {
    Optional<Book> findBookByName(String name);

    List<Book> findBooksByAuthorName(String authorName);

    List<Book> findBooksByGenre(Genre genre);

}
