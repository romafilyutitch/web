package by.epam.jwd.web.dao;


import by.epam.jwd.web.model.Author;

import java.util.Optional;

public interface AuthorDao extends Dao<Author> {
    Optional<Author> getByName(String authorName);
}
