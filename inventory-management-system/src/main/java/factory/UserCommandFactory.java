package factory;

import commands.Command;
import commands.user.*;

public class UserCommandFactory implements CommandFactory {

    @Override
    public Command createCommand(String commandType) {
        return switch (commandType) {
            case "1" -> new GetAllUsersCommand();
            case "2" -> new GetUserByIdCommand();
            case "3" -> new AddUserCommand();
            case "4" -> new UpdateUserCommand();
//            case "5" -> new DeleteUserCommand();
            default -> null;
        };
    }
}
