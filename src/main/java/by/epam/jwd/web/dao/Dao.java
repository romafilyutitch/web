package by.epam.jwd.web.dao;



import by.epam.jwd.web.model.DbEntity;

import java.util.List;
import java.util.Optional;

public interface Dao<T extends DbEntity> {
    T save(T entity);

    List<T> findAll();

    Optional<T> findById(Long id);

    List<T> findPage(int pageNumber);

    int getRowsAmount();

    int getPagesAmount();

    T update(T entity);

    void delete(Long id);

}
