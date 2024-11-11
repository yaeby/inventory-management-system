package commands.user;

import commands.Command;
import model.User;
import repository.UserRepository;
import service.IUserService;
import service.UserService;

import java.util.List;

public class GetAllUsersCommand implements Command {

    @Override
    public void execute() {
        UserService userService = new UserService(new UserRepository());
        List<User> users = userService.findAll();
        for (User user : users) {
            System.out.println(user.toString());
        }
    }
}
