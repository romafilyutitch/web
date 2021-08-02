package by.epam.jwd.web.validation;

import by.epam.jwd.web.model.DbEntity;

public class Validator<T extends DbEntity> {
    private ValidationStrategy<T> strategy;

    public Validator(ValidationStrategy<T> strategy) {
        this.strategy = strategy;
    }

    public void setStrategy(ValidationStrategy<T> strategy) {
        this.strategy = strategy;
    }

    public boolean validate(T entity) {
        return strategy.validate(entity);
    }
}
