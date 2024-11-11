package commands.user;

import commands.Command;
import model.User;
import exceptions.ResourceNotFoundException;
import repository.UserRepository;
import service.UserService;

import java.util.Scanner;

public class GetUserByIdCommand implements Command {

    @Override
    public void execute() {
        UserService userService = new UserService(new UserRepository());
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the user ID: ");
        Long userId = scanner.nextLong();
        try {
            User user = userService.findById(userId);
            System.out.println(user);
        } catch (ResourceNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("An unexpected error occurred: " + e.getMessage());
        }
    }
}
