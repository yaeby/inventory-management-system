package gui;

import commands.Command;
import commands.product.DeleteProductCommand;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import model.Product;
import repository.ProductRepository;
import service.ProductService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ProductController {

    @FXML
    private TableView<Product> productTable;

    @FXML
    private TableColumn<Product, String> codeColumn;

    @FXML
    private TableColumn<Product, String> nameColumn;

    @FXML
    private TableColumn<Product, String> brandColumn;

    @FXML
    private TableColumn<Product, Integer> quantityColumn;

    @FXML
    private TableColumn<Product, Double> costPriceColumn;

    @FXML
    private TableColumn<Product, Double> sellPriceColumn;

    @FXML
    private TableColumn<Product, Void> actionsColumn;

    @FXML
    private TextField searchField;

    private ProductService productService;
    private ObservableList<Product> productList;

    public void initialize() {
        productService = new ProductService(new ProductRepository());
        setupColumns();
        loadProducts();
        setupSearch();
    }

    private void setupColumns() {
        codeColumn.setCellValueFactory(new PropertyValueFactory<>("productCode"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        brandColumn.setCellValueFactory(new PropertyValueFactory<>("brand"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        costPriceColumn.setCellValueFactory(new PropertyValueFactory<>("costPrice"));
        sellPriceColumn.setCellValueFactory(new PropertyValueFactory<>("sellPrice"));

        actionsColumn.setCellFactory(param -> new TableCell<>() {
            private final Button editBtn = new Button("Edit");
            private final Button deleteBtn = new Button("Delete");
            private final HBox buttons = new HBox(5, editBtn, deleteBtn);

            {
                editBtn.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());
                    handleEditProduct(product);
                });

                deleteBtn.setOnAction(event -> {
                    Product product = getTableView().getItems().get(getIndex());
                    handleDeleteProduct(product);
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

    private void loadProducts() {
        try {
            List<Product> products = productService.findAll();
            productList = FXCollections.observableArrayList(products);
            productTable.setItems(productList);
        } catch (Exception e) {
            showError("Error loading products", e.getMessage());
        }
    }

    private void setupSearch() {
        FilteredList<Product> filteredData = new FilteredList<>(productList, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(product -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }

                String lowerCaseFilter = newValue.toLowerCase();

                return product.getProductCode().toLowerCase().contains(lowerCaseFilter)
                        || product.getProductName().toLowerCase().contains(lowerCaseFilter)
                        || product.getBrand().toLowerCase().contains(lowerCaseFilter);
            });
        });

        productTable.setItems(filteredData);
    }

    @FXML
    private void handleAddProduct() {
        showProductDialog(null);
    }

    private void handleEditProduct(Product product) {
        showProductDialog(product);
    }

    private void handleDeleteProduct(Product product) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Delete Product");
        alert.setHeaderText("Delete Product");
        alert.setContentText("Are you sure you want to delete " + product.getProductName() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                Command command = new DeleteProductCommand(productService, product);
                command.execute();
                loadProducts();
            } catch (Exception e) {
                showError("Error deleting product", e.getMessage());
            }
        }
    }

    private void showProductDialog(Product product) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/product-dialog.fxml"));
            Parent root = loader.load();

            ProductDialogController dialogController = loader.getController();
            dialogController.setProduct(product);
            dialogController.setProductService(productService);

            Stage dialogStage = new Stage();
            dialogStage.setTitle(product == null ? "Add Product" : "Edit Product");
            dialogStage.setScene(new Scene(root));

            dialogStage.showAndWait();

            loadProducts();
        } catch (IOException e) {
            showError("Error", "Could not load product dialog: " + e.getMessage());
        }
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}