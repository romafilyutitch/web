package by.epam.jwd.web.service;

import by.epam.jwd.web.model.DbEntity;

import java.util.List;

public interface Service<T extends DbEntity> {

    List<T> findAll();

    List<T> findPage(int currentPage);

    T findById(Long entityId);

    T register(T entity);

    void delete(Long entityId);

    int getPagesAmount();

}
