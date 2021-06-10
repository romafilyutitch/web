package by.epam.jwd.web.service;

import by.epam.jwd.web.model.User;

import java.time.LocalDate;
import java.util.List;

public interface UserService {

    List<User> findAll();

    User createUser(String login, String password) throws ServiceException;

    void deleteUser(Long userId) throws ServiceException;

    User login(String login, String password) throws ServiceException;

    User update(User user) throws ServiceException;

    User findById(Long id) throws ServiceException;

    User promoteUserRole(Long userId) throws ServiceException;

    User demoteUserRole(Long userId) throws ServiceException;

    User setSubscription(Long userId, String startDate, String endDate) throws ServiceException;
}
