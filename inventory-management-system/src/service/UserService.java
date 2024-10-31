package service;

import exceptions.ResourceNotFoundException;
import model.User;
import repository.IUserRepository;
import repository.UserRepository;

import java.util.List;
import java.util.Scanner;

public class UserService implements IUserService{

    private static volatile UserService instance;
    private final IUserRepository userRepository;

    private UserService() {
        this.userRepository = UserRepository.getInstance();
    }

    public static UserService getInstance() {
        UserService result = instance;
        if (result == null) {
            synchronized (UserService.class) {
                result = instance;
                if (result == null) {
                    instance = result = new UserService();
                }
            }
        }
        return result;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(Long id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new ResourceNotFoundException("User with id" + id + " not found");
        }
        return user;
    }

    @Override
    public void addUser() {
        Scanner scanner = new Scanner(System.in);
        User user = new User();
        System.out.print("Enter the product code: ");
        user.setUsername(scanner.nextLine());
        System.out.print("Enter the product name: ");
        user.setPassword(scanner.nextLine());
        userRepository.addUser(user);
        System.out.println("Product added successfully");
        scanner.close();
    }

    @Override
    public void updateUser(Long id) {
        User user = userRepository.findById(id);
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

            userRepository.updateUser(user);
            System.out.println("User updated successfully");
        } else {
            System.out.println("User with ID " + id + " not found.");
        }
    }


    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id);
        if (user != null) {
            userRepository.deleteUser(id);
            System.out.println("User deleted successfully.");
        } else {
            System.out.println("User with ID " + id + " not found.");
        }
    }

}
