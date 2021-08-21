package by.epam.jwd.web.dao.api;



import by.epam.jwd.web.dao.DAOException;
import by.epam.jwd.web.model.Subscription;

import java.time.LocalDate;
import java.util.List;

/**
 * Subscription data access object interface for dao layer. Extends {@link Dao} base interface
 * @author roma0
 * @version 1.0
 * @since 1.0
 * @see "Date access object pattern"
 */
public interface SubscriptionDao extends Dao<Subscription> {
    /**
     * Find and returns result of find {@link Subscription} that has specified {@link LocalDate} start date
     * @throws DAOException when exception in dao layer occurs.
     * @param startDate {@link LocalDate} find subscription start date
     * @return subscriptions that have specified start date
     */
    List<Subscription> findByStartDate(LocalDate startDate);

    /**
     * Find and returns result of find {@link Subscription} that has specified {@link LocalDate} end date
     * @throws DAOException when exception in dao layer occurs
     * @param endDate {@link LocalDate} find Subscription end date
     * @return subscriptions that have specified end date
     */
    List<Subscription> findByEndDate(LocalDate endDate);

}
