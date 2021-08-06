package by.epam.jwd.web.dao.api;

import java.sql.SQLException;

/**
 * Consumer interface. Invokes in {@link AbstractDao#findPreparedEntities(String, SQLConsumer)} method
 * and implements in {@link AbstractDao} implementations. Throws {@link SQLException}.
 * Template method variation.
 * @author roma0
 * @version 1.0
 * @since 1.0
 * @param <T> entity type that need to consume.
 */
@FunctionalInterface
public interface SQLConsumer<T> {
    /**
     * Function interface consumer method to perform consume operation
     * @param statement instance to which need make consume operation
     * @throws SQLException when SQLException occurs during consume operation execution
     */
    void accept(T statement) throws SQLException;
}
