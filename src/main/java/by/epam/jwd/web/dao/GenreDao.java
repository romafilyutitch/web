package by.epam.jwd.web.dao;



import by.epam.jwd.web.model.BookGenre;

import java.util.Optional;

public interface GenreDao extends Dao<BookGenre> {
    Optional<BookGenre> getByName(String genreName);
}
