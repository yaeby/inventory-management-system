package gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.Product;
import repository.ProductRepository;
import service.ProductService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class InventoryManagerApp extends Application {
    private ProductService productService;
    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Inventory Manager");

        // Initialize services
        productService = new ProductService(new ProductRepository());

        // Create the main layout
        BorderPane mainLayout = createMainLayout();

        // Create the scene
        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private BorderPane createMainLayout() {
        BorderPane borderPane = new BorderPane();

        // Create menu bar
        HBox menuBar = createMenuBar();
        borderPane.setTop(menuBar);

        // Initial content (home page)
        VBox homeContent = createHomeContent();
        borderPane.setCenter(homeContent);

        return borderPane;
    }

    private HBox createMenuBar() {
        HBox menuBar = new HBox(10);
        menuBar.setStyle("-fx-background-color: #f0f0f0; -fx-padding: 10px;");

        Button homeButton = createMenuButton("Home", e -> showHomePage());
        Button productsButton = createMenuButton("Products", e -> showProductsPage());
        Button ordersButton = createMenuButton("Orders", e -> showOrdersPage());

        menuBar.getChildren().addAll(homeButton, productsButton, ordersButton);
        return menuBar;
    }

    private Button createMenuButton(String text, javafx.event.EventHandler<javafx.event.ActionEvent> handler) {
        Button button = new Button(text);
        button.setStyle(
                "-fx-background-color: #4CAF50;" +
                        "-fx-text-fill: white;" +
                        "-fx-min-width: 100px;"
        );
        button.setOnAction(handler);
        return button;
    }

    private VBox createHomeContent() {
        VBox homeContent = new VBox(20);
        homeContent.setStyle("-fx-padding: 20px;");

        Label welcomeLabel = new Label("Welcome to Inventory Manager");
        welcomeLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Label infoLabel = new Label("Select a menu option to get started");
        infoLabel.setStyle("-fx-font-size: 16px;");

        homeContent.getChildren().addAll(welcomeLabel, infoLabel);
        homeContent.setAlignment(javafx.geometry.Pos.CENTER);

        return homeContent;
    }

    private void showHomePage() {
        primaryStage.getScene().setRoot(createHomeContent());
    }

    private void showProductsPage() {
        VBox productsContent = new VBox(10);
        productsContent.setStyle("-fx-padding: 20px;");

        // Create TableView for products
        TableView<Product> productTable = new TableView<>();

        // Define columns
        TableColumn<Product, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("id"));

        TableColumn<Product, String> productCodeColumn = new TableColumn<>("Product Code");
        productCodeColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("productCode"));

        TableColumn<Product, String> productNameColumn = new TableColumn<>("Product Name");
        productNameColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("productName"));

        TableColumn<Product, String> brandColumn = new TableColumn<>("Brand");
        brandColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("brand"));

        TableColumn<Product, Integer> quantityColumn = new TableColumn<>("Quantity");
        quantityColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("quantity"));

        TableColumn<Product, Double> costPriceColumn = new TableColumn<>("Cost Price");
        costPriceColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("costPrice"));

        TableColumn<Product, Double> sellPriceColumn = new TableColumn<>("Sell Price");
        sellPriceColumn.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("sellPrice"));

        // Add columns to the table
        productTable.getColumns().addAll(
                idColumn, productCodeColumn, productNameColumn,
                brandColumn, quantityColumn, costPriceColumn, sellPriceColumn
        );

        // Load products
        ObservableList<Product> productList = FXCollections.observableArrayList(
                productService.findAll()
        );
        productTable.setItems(productList);

        productsContent.getChildren().add(productTable);

        primaryStage.getScene().setRoot(productsContent);
    }

    private void showOrdersPage() {
        VBox ordersContent = new VBox(10);
        ordersContent.setStyle("-fx-padding: 20px;");

        Label ordersLabel = new Label("Orders Page (Not Implemented)");
        ordersLabel.setStyle("-fx-font-size: 18px;");

        ordersContent.getChildren().add(ordersLabel);
        ordersContent.setAlignment(javafx.geometry.Pos.CENTER);

        primaryStage.getScene().setRoot(ordersContent);
    }

    public static void main(String[] args) {
        launch(args);
    }
}