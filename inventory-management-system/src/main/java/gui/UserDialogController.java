package gui;

import commands.Command;
import commands.user.AddUserCommand;
import commands.user.UpdateUserCommand;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Role;
import model.User;
import service.UserService;

public class UserDialogController {
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField roleField;
    @FXML
    private ComboBox<Role> roleComboBox;

    private User user;
    private UserService userService;
    private boolean isNew = true;

    public void initialize() {
        roleComboBox.getItems().addAll(Role.values());
    }

    public void setUser(User User) {
        this.user = User;
        if (User != null) {
            isNew = false;
            fillFields();
        }
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    private void fillFields() {
        usernameField.setText(user.getUsername());
        passwordField.setText(user.getPassword());
        roleComboBox.setValue(user.getRole());
    }

    @FXML
    private void handleSave() {
        try {
            if (validateInput()) {
                User updatedUser = isNew ? new User() : user;
                updateUserFromFields(updatedUser);
                Command command;
                if (isNew) {
                    command = new AddUserCommand(userService, updatedUser);
                } else {
                    command = new UpdateUserCommand(userService, updatedUser);
                }
                command.execute();
                closeDialog();
            }
        } catch (Exception e) {
            DisplayAlert.showError("Error saving User", e.getMessage());
        }
    }

    private void updateUserFromFields(User user) {
        user.setUsername(usernameField.getText());
        user.setPassword(passwordField.getText());
        user.setRole(roleComboBox.getValue());
    }

    private boolean validateInput() {
        if (usernameField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            DisplayAlert.showError("Validation Error", "Please enter valid username and password");
            return false;
        }
        if (roleComboBox.getValue() == null) {
            DisplayAlert.showError("Validation Error", "Please select a role");
            return false;
        }
        return true;
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) usernameField.getScene().getWindow();
        stage.close();
    }
}
