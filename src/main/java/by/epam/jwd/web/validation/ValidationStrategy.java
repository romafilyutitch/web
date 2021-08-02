package by.epam.jwd.web.validation;

import by.epam.jwd.web.model.DbEntity;

public interface ValidationStrategy<T extends DbEntity> {
    boolean validate(T entity);
}
