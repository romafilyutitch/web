package by.epam.jwd.web.service;

import by.epam.jwd.web.model.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User save(User user) throws ServiceException;

    User login(User user) throws ServiceException;

    User update(User user) throws ServiceException;

    User findById(Long id) throws ServiceException;

}
