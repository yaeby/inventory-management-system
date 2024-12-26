package commands.user;

import commands.Command;
import model.User;
import repository.UserRepository;
import service.UserService;

import java.util.Scanner;

public class UpdateUserCommand implements Command {

    private final UserService userService;
    private final User user;

    public UpdateUserCommand(UserService userService, User user) {
        this.userService = userService;
        this.user = user;
    }

    @Override
    public void execute() {
        userService.update(user);
    }
}
