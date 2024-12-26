package handler;

import model.User;

public class ValidPasswordHandler extends LoginHandler {

    @Override
    public LoginResult handle(String username, String password) {
        User user = getUser();
        if (user != null) {
            if (!password.equals(user.getPassword())) {
                return new LoginResult(false, "Password is incorrect");
            }
            return handleNext(username, password);
        }
        return new LoginResult(false, "Unexpected error: User not found");
    }
}

