package commands.user;

import commands.Command;
import service.IUserService;
import service.UserService;

public class AddUserCommand implements Command {

    @Override
    public void execute() {
        IUserService userService = UserService.getInstance();
        userService.addUser();
    }
}
