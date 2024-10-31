package repository;

import model.User;

import java.util.List;

public interface IUserRepository {
    List<User> findAll();
    User findById(Long id);
    void addUser(User user);
    void updateUser(User user);
    void deleteUser(Long id);
}
