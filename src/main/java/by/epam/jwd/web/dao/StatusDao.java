package by.epam.jwd.web.dao;

import by.epam.jwd.web.model.Status;

import java.util.Optional;

public interface StatusDao extends Dao<Status> {
    Optional<Status> findByName(String statusName);
}
