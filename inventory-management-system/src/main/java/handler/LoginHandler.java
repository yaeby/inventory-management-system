package handler;

import model.User;

public abstract class LoginHandler {
    private LoginHandler next;
    private static User user;

    public void setNext(LoginHandler next) {
        this.next = next;
    }

    public abstract LoginResult handle(String username, String password);

    protected LoginResult handleNext(String username, String password) {
        if(next == null) {
            return new LoginResult(true, null);
        }
        return next.handle(username, password);
    }

    public User getUser() {
        return user;
    }

    protected void setUser(User user) {
        LoginHandler.user = user;
    }
}
