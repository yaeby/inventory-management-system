package handler;

import repository.UserRepository;
import service.UserService;

public abstract class LoginHandler {
    private LoginHandler next;
    protected UserService userService;

    public LoginHandler(UserService userService) {
        this.userService = userService;
    }

    public LoginHandler setNext(LoginHandler next) {
        this.next = next;
        return next;
    }

    public abstract boolean handle(String username, String password);

    protected boolean handleNext(String username, String password) {
        if(next == null) {
            return true;
        }
        return next.handle(username, password);
    }
}
