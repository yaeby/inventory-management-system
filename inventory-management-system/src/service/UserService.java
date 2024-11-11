package service;

import exceptions.ResourceNotFoundException;
import model.User;
import repository.IRepository;

import java.util.List;
import java.util.Scanner;

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
    public void add() {
        Scanner scanner = new Scanner(System.in);
        User user = new User();
        System.out.print("Enter the username: ");
        user.setUsername(scanner.nextLine());
        System.out.print("Enter the password: ");
        user.setPassword(scanner.nextLine());
        repository.add(user);
        System.out.println("User added successfully");
    }

    @Override
    public void update(Long id) {
        User user = repository.findById(id);
        if (user != null) {
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter the new username (leave blank to keep current): ");
            String username = scanner.nextLine();
            if (!username.isEmpty()) {
                user.setUsername(username);
            }

            System.out.print("Enter the new password (leave blank to keep current): ");
            String password = scanner.nextLine();
            if (!password.isEmpty()) {
                user.setPassword(password);
            }

            repository.update(user);
            System.out.println("User updated successfully");
        } else {
            System.out.println("User with ID " + id + " not found.");
        }
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
}
