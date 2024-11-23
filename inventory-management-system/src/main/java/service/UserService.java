package service;

import exceptions.ResourceNotFoundException;
import model.User;
import repository.IRepository;

import java.util.List;

public class UserService extends Service<User, Long>{

    public UserService(IRepository<User, Long> repository) {
        super(repository);
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public User findById(Long id) {
        User user = repository.findById(id);
        if (user == null) {
            throw new ResourceNotFoundException("User with id: " + id + " not found");
        }
        return user;
    }

    @Override
    public void add(User user) {
        repository.add(user);
    }

    @Override
    public void update(User user) {
        repository.update(user);
    }

    @Override
    public void delete(Long id) {
        User user = repository.findById(id);
        if (user != null) {
            repository.delete(id);
            System.out.println("User deleted successfully.");
        } else {
            System.out.println("User with ID " + id + " not found.");
        }
    }

    public User findByName(String username) {
        return repository.findByName(username);
    }
}
