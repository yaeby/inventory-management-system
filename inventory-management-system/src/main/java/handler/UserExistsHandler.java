package handler;

import model.User;
import service.UserService;

public class UserExistsHandler extends LoginHandler{
    private final UserService userService;

    public UserExistsHandler(UserService userService) {
        this.userService = userService;
    }

    @Override
    public LoginResult handle(String username, String password) {
        User user = userService.findByName(username);
        if(user == null) {
            return new LoginResult(false, "User does not exist");
        }
        setUser(user);
        return handleNext(username, password);
    }
}
