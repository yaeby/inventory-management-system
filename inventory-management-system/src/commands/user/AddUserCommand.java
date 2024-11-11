package commands.user;

import commands.Command;
import repository.UserRepository;
import service.UserService;

public class AddUserCommand implements Command {

    @Override
    public void execute() {
        UserService userService = new UserService(new UserRepository());
        userService.add();
    }
}
