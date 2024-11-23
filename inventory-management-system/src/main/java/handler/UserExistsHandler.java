package handler;

import model.User;
import repository.UserRepository;
import service.UserService;

public class UserExistsHandler extends LoginHandler{

    public UserExistsHandler(UserService userService) {
        super(userService);
    }

    @Override
    public boolean handle(String username, String password) {
        User user = userService.findByName(username);
        return user != null && handleNext(username, password);
    }
}
