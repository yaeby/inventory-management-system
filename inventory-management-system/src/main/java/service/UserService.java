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
        return repository.findById(id);
    }

    @Override
    public void add(User entity) {
        super.add(entity);
    }

    @Override
    public void update(User entity) {
        super.update(entity);
    }

    @Override
    public void delete(Long id) {
        super.delete(id);
    }

    public User findByName(String username) {
        return repository.findByName(username);
    }
}
