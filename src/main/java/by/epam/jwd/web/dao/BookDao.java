package by.epam.jwd.web.dao;



import by.epam.jwd.web.model.Book;

import java.util.List;

public interface BookDao extends Dao<Book> {
    List<Book> findBooksByName(String name);

    List<Book> findBooksByAuthorName(String authorName);

    List<Book> findBooksByGenreName(String genreName);

    List<Book> findBooksByYear(int year);
}
