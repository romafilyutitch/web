package by.epam.jwd.web.dao;



import by.epam.jwd.web.model.Author;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;

import java.util.List;
import java.util.Optional;

public interface BookDao extends Dao<Book> {
    Optional<Book> findByName(String name);

    List<Book> findByAuthor(Author author);

    List<Book> findByGenre(Genre genre);
}
