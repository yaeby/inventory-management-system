package commands.user;

import commands.Command;
import model.User;
import service.UserService;

public class AddUserCommand implements Command {

    private final UserService userService;
    private final User user;

    public AddUserCommand(UserService userService, User user) {
        this.userService = userService;
        this.user = user;
    }

    @Override
    public void execute() {
        userService.add(user);
    }
}
