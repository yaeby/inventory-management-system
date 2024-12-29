package gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Purchase;
import repository.ProductRepository;
import repository.PurchaseRepository;
import repository.SupplierRepository;
import service.ProductService;
import service.PurchaseService;
import service.SupplierService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class PurchasesController {
    @FXML
    private TableView<Purchase> purchasesTable;
    @FXML
    private TableColumn<Purchase, String> productCodeColumn;
    @FXML
    private TableColumn<Purchase, String> productNameColumn;
    @FXML
    private TableColumn<Purchase, String> quantityColumn;
    @FXML
    private TableColumn<Purchase, String> supplierColumn;
    @FXML
    private Label productCodeLabel;
    @FXML
    private Label productNameLabel;
    @FXML
    private Label quantityLabel;
    @FXML
    private Label supplierLabel;
    @FXML
    private TextField searchField;

    private PurchaseService purchaseService;
    private ObservableList<Purchase> purchaseList;
    private Purchase selectedPurchase;
    
    public void initialize() {
        purchaseService = new PurchaseService(new PurchaseRepository());
        SupplierService supplierService = new SupplierService(new SupplierRepository());
        setupColumns();
        loadPurchases();
        setupSearch();
        setupTableSelection();
    }

    private void setupColumns() {
        productCodeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getProduct().getProductCode()));

        productNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getProduct().getProductName()));

        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        supplierColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getSupplier().getName()));
    }

    private void loadPurchases() {
        try {
            List<Purchase> purchases = purchaseService.findAll();
            purchaseList = FXCollections.observableArrayList(purchases);
            purchasesTable.setItems(purchaseList);
        } catch (Exception e) {
            DisplayAlert.showError("Error loading data", "The server couldn't load the data");
        }
    }
    
    private void setupTableSelection() {
        purchasesTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedPurchase = newSelection;
                supplierLabel.setText(selectedPurchase.getSupplier().getName());
                productCodeLabel.setText(selectedPurchase.getProduct().getProductCode());
                productNameLabel.setText(selectedPurchase.getProduct().getProductName());
                quantityLabel.setText(String.valueOf(selectedPurchase.getQuantity()));
            }
        });
    }
    
    private void setupSearch() {
        FilteredList<Purchase> filteredData = new FilteredList<>(purchaseList, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(purchase -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                return purchase.getProduct().getProductCode().toLowerCase().contains(lowerCaseFilter)
                        || purchase.getProduct().getProductName().toLowerCase().contains(lowerCaseFilter)
                        || purchase.getSupplier().getName().toLowerCase().contains(lowerCaseFilter);
            });
        });
        purchasesTable.setItems(filteredData);
    }

    @FXML
    private void handleCreatePurchase(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/purchase-dialog.fxml"));
            Parent root = loader.load();

            PurchaseDialogController dialogController = loader.getController();
            dialogController.setPurchaseService(purchaseService);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Supply products");
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

            loadPurchases();
        } catch (IOException e) {
            DisplayAlert.showError("Error", "Could not load purchase dialog: " + e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedPurchase != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Purchase");
            alert.setHeaderText("Delete Purchase");
            alert.setContentText("Are you sure you want to delete " + selectedPurchase.getProduct().getProductCode() + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    purchaseService.delete(selectedPurchase.getId());
                    loadPurchases();
                    clearFields();
                } catch (Exception e) {
                    DisplayAlert.showError("Error deleting Purchase", e.getMessage());
                }
            }
        }
    }

    @FXML
    private void clearFields() {
        selectedPurchase = null;
        productCodeLabel.setText("");
        productNameLabel.setText("");
        quantityLabel.setText("");
        supplierLabel.setText("");
        purchasesTable.getSelectionModel().clearSelection();
    }
}
