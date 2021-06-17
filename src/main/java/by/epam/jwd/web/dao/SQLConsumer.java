package by.epam.jwd.web.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;

@FunctionalInterface
public interface SQLConsumer<T> {
    void accept(T statement) throws SQLException;
}
