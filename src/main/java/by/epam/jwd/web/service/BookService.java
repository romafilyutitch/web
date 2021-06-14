package by.epam.jwd.web.service;

import by.epam.jwd.web.exception.RegisterException;
import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.model.Book;

import java.util.List;

public interface BookService {

    List<Book> findAllBooks();

    Book registerBook(Book book) throws RegisterException ;

    void deleteBook(Long bookId);

    Book findById(Long bookId);

    Book addOneCopy(Long bookId);

    Book removeOneCopy(Long bookId);

    Book findByName(String name);

    List<Book> findByGenre(String genre);

    List<Book> findByAuthor(String author);


}
