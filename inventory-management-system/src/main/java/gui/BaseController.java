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
import model.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class BaseController {

    @FXML
    Button productsButton;

    @FXML
    Button dashboardButton;

    @FXML
    Button ordersButton;

    @FXML
    Button usersButton;

    @FXML
    Button logoutButton;

    @FXML
    AnchorPane centerPane;

    @FXML
    private Label usernameLabel;

    private User currentUser;
    private final Map<String, String> urls = new HashMap<>();

    public BaseController() {
        urls.put("Products", "/view/products.fxml");
        urls.put("Orders", "/view/orders.fxml");
        urls.put("Dashboard", "/view/dashboard.fxml");
        urls.put("Users", "/view/users.fxml");
    }

    public void setUser(User user) {
        this.currentUser = user;
        usernameLabel.setText(user.getUsername());
    }

    @FXML
    void btnNavigators(ActionEvent event) {
        Button btn = (Button) event.getSource();
        String btnText = btn.getText();
        String url = urls.get(btnText);

        if (url == null) {
            System.err.println("No URL found for button: " + btnText);
            return;
        }

        try {
            ctrlRightPane(url);
        } catch (IOException e) {
            System.err.println("Error loading FXML: " + url);
            e.printStackTrace();
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
            System.err.println("Failed to load: " + url);
            System.err.println("Attempted to load from: " + getClass().getResource(url));
            e.printStackTrace();
            throw e;
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

            // Close current stage and show new one
            currentStage.close();
            newStage.show();
        } catch (IOException e) {
            System.err.println("Failed to load login view");
            e.printStackTrace();
        }
    }
}