package gui;

import builder.GenericBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Role;
import model.User;
import repository.UserRepository;
import service.UserService;

import java.util.List;
import java.util.Optional;

public class UserController {
    @FXML
    private TableView<User> userTable;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> passwordColumn;
    @FXML
    private TableColumn<User, String> roleColumn;
    @FXML
    private TextField searchField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private ComboBox<Role> roleComboBox;

    private UserService userService;
    private ObservableList<User> userList;
    private User selectedUser;

    public void initialize() {
        userService = new UserService(new UserRepository());
        setupColumns();
        loadUsers();
        setupSearch();
        setupTableSelection();
        roleComboBox.getItems().addAll(Role.values());
    }

    private void setupColumns() {
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));
    }

    private void setupTableSelection() {
        userTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedUser = newSelection;
                usernameField.setText(newSelection.getUsername());
                passwordField.setText(newSelection.getPassword());
                roleComboBox.setValue(newSelection.getRole());
            }
        });
    }

    private void loadUsers() {
        try {
            List<User> users = userService.findAll();
            userList = FXCollections.observableArrayList(users);
            userTable.setItems(userList);
        } catch (Exception e) {
            DisplayAlert.showError("Error loading users", "The server couldn't load the data");
        }
    }

    private void setupSearch() {
        FilteredList<User> filteredData = new FilteredList<>(userList, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(user -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                return user.getUsername().toLowerCase().contains(lowerCaseFilter)
                        || user.getRole().name().toLowerCase().contains(lowerCaseFilter);
            });
        });

        userTable.setItems(filteredData);
    }

    @FXML
    private void handleSave() {
        try {
            if (selectedUser == null) {
                User newUser = GenericBuilder.of(User::new)
                        .with(User::setUsername, usernameField.getText())
                        .with(User::setPassword, passwordField.getText())
                        .with(User::setRole, roleComboBox.getValue())
                        .build();
                userService.add(newUser);
            } else {
                selectedUser.setUsername(usernameField.getText());
                selectedUser.setPassword(passwordField.getText());
                selectedUser.setRole(roleComboBox.getValue());
                userService.update(selectedUser);
            }
            loadUsers();
            clearFields();
        } catch (Exception e) {
            DisplayAlert.showError("Error", "Could not save user: " + e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedUser != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete User");
            alert.setHeaderText("Delete User");
            alert.setContentText("Are you sure you want to delete " + selectedUser.getUsername() + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    userService.delete(selectedUser.getId());
                    loadUsers();
                    clearFields();
                } catch (Exception e) {
                    DisplayAlert.showError("Error deleting user", e.getMessage());
                }
            }
        }
    }

    @FXML
    private void clearFields() {
        selectedUser = null;
        usernameField.clear();
        passwordField.clear();
        roleComboBox.setValue(null);
        userTable.getSelectionModel().clearSelection();
    }

}
