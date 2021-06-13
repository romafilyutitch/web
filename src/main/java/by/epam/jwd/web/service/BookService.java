package by.epam.jwd.web.service;

import by.epam.jwd.web.exception.ServiceException;
import by.epam.jwd.web.model.Book;

import java.util.List;

public interface BookService {

    List<Book> findAll();

    Book createBook(String name, String author, String genre, String date, String pageAmount, String description, String text) throws ServiceException;

    void deleteBook(Long bookId) throws ServiceException;

    Book update(Book book) throws ServiceException;

    Book findById(Long id) throws ServiceException;

    Book addOneCopy(Long id) throws ServiceException;

    Book removeOneCopy(Long id) throws ServiceException;

    Book findByName(String name) throws ServiceException;

    List<Book> findByGenre(String genre) throws ServiceException;

    List<Book> findByAuthor(String author) throws ServiceException;


}
