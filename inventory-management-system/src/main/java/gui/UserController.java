package gui;

import commands.Command;
import commands.user.DeleteUserCommand;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.Product;
import model.User;
import repository.UserRepository;
import service.UserService;

import java.io.IOException;
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
    private TableColumn<User, Void> actionsColumn;

    @FXML
    private TextField searchField;

    private UserService userService;
    private ObservableList<User> userList;

    public void initialize() {
        userService = new UserService(new UserRepository());
        setupColumns();
        loadUsers();
        setupSearch();
    }

    private void setupColumns() {
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        roleColumn.setCellValueFactory(new PropertyValueFactory<>("role"));

        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox buttons = new HBox(5, editBtn, deleteBtn);

            {
                editBtn.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    handleEditUser(user);
                });

                deleteBtn.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    handleDeleteUser(user);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(buttons);
                }
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
    private void handleAddUser(){
        showUserDialog(null);
    }

    private void handleEditUser(User user){
        showUserDialog(user);
    }

    private void handleDeleteUser(User user) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete User");
        alert.setHeaderText("Delete User");
        alert.setContentText("Are you sure you want to delete " + user.getUsername() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Command command = new DeleteUserCommand(userService, user);
                command.execute();
                loadUsers();
            } catch (Exception e) {
                DisplayAlert.showError("Error deleting user", e.getMessage());
            }
        }
    }

    private void showUserDialog(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/user-dialog.fxml"));
            Parent root = loader.load();

            UserDialogController dialogController = loader.getController();
            dialogController.setUser(user);
            dialogController.setUserService(userService);

            Stage dialogStage = new Stage();
            dialogStage.setTitle(user == null ? "Add User" : "Edit User");
            dialogStage.setScene(new Scene(root));

            dialogStage.showAndWait();

            loadUsers();
        } catch (IOException e) {
            DisplayAlert.showError("Error", "Could not load user dialog: " + e.getMessage());
        }
    }
}
