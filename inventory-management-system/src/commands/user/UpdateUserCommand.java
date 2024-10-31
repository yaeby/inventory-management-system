package commands.user;

import commands.Command;
import service.IUserService;
import service.UserService;

import java.util.Scanner;

public class UpdateUserCommand implements Command {

    @Override
    public void execute() {
        IUserService userService = UserService.getInstance();
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the user ID: ");
        Long userId = scanner.nextLong();
        userService.updateUser(userId);
    }
}
