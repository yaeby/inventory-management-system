package gui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Role;
import model.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BaseController {
    @FXML
    private Button logoutButton;
    @FXML
    private Button usersButton;
    @FXML
    private AnchorPane centerPane;
    @FXML
    private Label usernameLabel;

    private User currentUser;
    private final Map<String, String> urls;

    public BaseController() {
        urls = new HashMap<>();
        urls.put("Dashboard", "/view/dashboard.fxml");
        urls.put("Categories", "/view/categories.fxml");
        urls.put("Products", "/view/products.fxml");
        urls.put("Customers", "/view/customers.fxml");
        urls.put("Suppliers", "/view/suppliers.fxml");
        urls.put("Orders", "/view/orders.fxml");
        urls.put("Purchases", "/view/purchases.fxml");
        urls.put("Users", "/view/users.fxml");
    }

    public void setUser(User user) {
        this.currentUser = user;
        setupButtonVisibility();
        usernameLabel.setText(user.getUsername());
        try {
            ctrlRightPane(urls.get("Dashboard"));
        } catch (IOException e) {
            DisplayAlert.showError("Error", "Could not load dashboard");
        }
    }
    private void setupButtonVisibility() {
        if (currentUser != null && usersButton != null && currentUser.getRole() != Role.ADMIN) {
            usersButton.setVisible(false);
            usersButton.setManaged(false);
        }
    }

    @FXML
    void btnNavigators(ActionEvent event) {
        Button btn = (Button) event.getSource();
        String btnText = btn.getText();
        String url = urls.get(btnText);

        try {
            ctrlRightPane(url);
        } catch (IOException e) {
            DisplayAlert.showError("Error", "Error loading FXML: " + url);
        }
    }

    @FXML
    private void ctrlRightPane(String url) throws IOException {
        try {
            centerPane.getChildren().clear();
            FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
            AnchorPane newCenterPane = loader.load();
            newCenterPane.setPrefHeight(centerPane.getHeight());
            newCenterPane.setPrefWidth(centerPane.getWidth());
            centerPane.getChildren().add(newCenterPane);
        } catch (IOException e) {
            DisplayAlert.showError("Error", "Could not load FXML: " + url);;
        }
    }

    @FXML
    private void logout(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
            Scene scene = new Scene(root);
            Stage currentStage = (Stage) logoutButton.getScene().getWindow();
            Stage newStage = new Stage();
            newStage.setScene(scene);
            currentStage.close();
            newStage.show();
        } catch (IOException e) {
            DisplayAlert.showError("Error", "Could not load login view");
        }
    }
}