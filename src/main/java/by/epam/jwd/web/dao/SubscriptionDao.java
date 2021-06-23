package by.epam.jwd.web.dao;



import by.epam.jwd.web.model.Subscription;

import java.time.LocalDate;
import java.util.List;

public interface SubscriptionDao extends Dao<Subscription> {
    List<Subscription> findByStartDate(LocalDate startDate);

    List<Subscription> findByEndDate(LocalDate endDate);

}
