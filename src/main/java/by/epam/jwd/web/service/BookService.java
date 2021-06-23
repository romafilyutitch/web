package by.epam.jwd.web.service;

import by.epam.jwd.web.exception.RegisterException;
import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.model.Author;
import by.epam.jwd.web.model.Book;
import by.epam.jwd.web.model.Genre;

import java.util.List;
import java.util.Optional;

public interface BookService extends Service<Book> {

    Book addOneCopy(Long bookId);

    Book removeOneCopy(Long bookId);

    Optional<Book> findByName(String name);

    List<Book> findByGenre(Genre genre);

    List<Book> findByAuthor(Author author);

}
