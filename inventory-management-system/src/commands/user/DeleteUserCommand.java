package commands.user;

import commands.Command;
import repository.UserRepository;
import service.IUserService;
import service.ProductService;
import service.UserService;

import java.util.Scanner;

public class DeleteUserCommand implements Command {

    @Override
    public void execute() {
        UserService userService = new UserService(new UserRepository());
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the user ID: ");
        Long userId = scanner.nextLong();
        userService.delete(userId);
    }
}
