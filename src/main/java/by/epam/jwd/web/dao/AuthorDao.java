package by.epam.jwd.web.dao;



import by.epam.jwd.web.model.BookAuthor;

import java.util.Optional;

public interface AuthorDao extends Dao<BookAuthor>{
    Optional<BookAuthor> getByName(String authorName);
}
