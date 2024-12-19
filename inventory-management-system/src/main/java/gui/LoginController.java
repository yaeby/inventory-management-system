package gui;

import handler.LoginHandler;
import handler.LoginResult;
import handler.UserExistsHandler;
import handler.ValidPasswordHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import repository.UserRepository;
import service.UserService;

import java.io.IOException;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final UserService userService;

    public LoginController() {
        userService = new UserService(new UserRepository());
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        LoginHandler handler = new UserExistsHandler(userService);
        handler.setNext(new ValidPasswordHandler());

        try {
            LoginResult result = handler.handle(username, password);
            if (result.success()) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/base.fxml"));
                Parent root = loader.load();

                BaseController baseController = loader.getController();
                baseController.setUser(handler.getUser());

                Stage stage = (Stage) usernameField.getScene().getWindow();
                stage.setScene(new Scene(root));
            } else {
                errorLabel.setText(result.message());
            }
        } catch (IOException e) {
            errorLabel.setText("Error loading dashboard");
            e.printStackTrace();
        }
    }
}