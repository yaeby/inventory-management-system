package commands.user;

import commands.Command;
import model.User;
import service.UserService;

public class DeleteUserCommand implements Command {

    private final UserService userService;
    private final User deleteUser;

    public DeleteUserCommand(UserService userService, User deleteUser) {
        this.userService = userService;
        this.deleteUser = deleteUser;
    }

    @Override
    public void execute() {
        userService.delete(deleteUser.getId());
    }
}
