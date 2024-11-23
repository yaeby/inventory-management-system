package handler;

import model.User;
import service.UserService;

public class ValidPasswordHandler extends LoginHandler {

    public ValidPasswordHandler(UserService userService) {
        super(userService);
    }

    @Override
    public boolean handle(String username, String password) {
            User user = userService.findByName(username);
            if (user != null) {
                return password.equals(user.getPassword()) && handleNext(username, password);
            }
            return false;
        }
}

