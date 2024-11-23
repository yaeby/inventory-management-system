package gui;

import handler.LoginHandler;
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
import model.User;
import repository.UserRepository;
import service.UserService;


import java.io.IOException;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    @FXML
    private void handleLogin() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        UserService userService = new UserService(new UserRepository());
        LoginHandler userExistsHandler = new UserExistsHandler(userService);
        LoginHandler validPasswordHandler = new ValidPasswordHandler(userService);

        userExistsHandler.setNext(validPasswordHandler);

        try {
//            User user = userService.findById(2L);
            boolean loginSuccess = userExistsHandler.handle(username, password);
            if (loginSuccess) {
                Stage stage = (Stage) usernameField.getScene().getWindow();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/base.fxml"));
                Parent root = loader.load();
                stage.setScene(new Scene(root));
                stage.setTitle("Inventory Management System - Dashboard");
            } else {
                errorLabel.setText("Invalid username or password");
            }
        } catch (IOException e) {
            errorLabel.setText("Error loading dashboard");
            e.printStackTrace();
        }
    }
}