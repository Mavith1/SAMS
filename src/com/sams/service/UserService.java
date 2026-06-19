package com.sams.service;

import com.sams.dao.UserDAO;
import com.sams.model.User;
import java.util.List;

public class UserService {

    private final UserDAO userDAO = new UserDAO();

    public List<User> getAllUsers() {
        return userDAO.getAllUsers();
    }

    public boolean registerUser(User user) {
        if (isInvalid(user)) {
            return false;
        }
        return userDAO.addUser(user);
    }

    public boolean modifyUser(User user) {
        if (user.getUserId() <= 0 || isInvalid(user)) {
            return false;
        }
        return userDAO.updateUser(user);
    }

    public boolean removeUser(int userId) {
        return userDAO.deleteUser(userId);
    }

    private boolean isInvalid(User user) {
        return user.getUsername().isEmpty()
                || user.getPassword().isEmpty()
                || user.getRole().isEmpty();
    }
}
