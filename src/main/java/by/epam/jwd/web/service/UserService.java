package by.epam.jwd.web.service;

import by.epam.jwd.web.model.User;

import java.util.List;

public interface UserService {

    List<User> findAll();

    User createUser(String login, String password) throws ServiceException;

    User login(String login, String password) throws ServiceException;

    User update(User user) throws ServiceException;

    User findById(Long id) throws ServiceException;

}
