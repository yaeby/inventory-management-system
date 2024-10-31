package commands.user;

import commands.Command;
import model.User;
import service.IUserService;
import service.UserService;

import java.util.List;

public class GetAllUsersCommand implements Command {

    @Override
    public void execute() {
        IUserService userService = UserService.getInstance();
        List<User> users = userService.getAllUsers();
        for (User user : users) {
            System.out.println(user.toString());
        }
    }
}
