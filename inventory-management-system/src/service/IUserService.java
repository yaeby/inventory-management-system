package service;

import model.User;

import java.util.List;

public interface IUserService {
    List<User> getAllUsers();
    User getUserById(Long id);
    void addUser();
    void updateUser(Long id);
    void deleteUser(Long id);
}
