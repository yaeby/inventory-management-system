package gui;

import builder.GenericBuilder;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Product;
import model.Purchase;
import model.Supplier;
import repository.ProductRepository;
import repository.PurchaseRepository;
import repository.SupplierRepository;
import service.ProductService;
import service.PurchaseService;
import service.SupplierService;

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
    private ComboBox<Product> productComboBox;
    @FXML 
    private TextField quantityTextField;
    @FXML
    private ComboBox<Supplier> supplierComboBox;
    @FXML
    private TextField searchField;
    
    private PurchaseService purchaseService;
    private ObservableList<Purchase> purchaseList;
    private Purchase selectedPurchase;
    
    public void initialize() {
        purchaseService = new PurchaseService(new PurchaseRepository());
        ProductService productService = new ProductService(new ProductRepository());
        SupplierService supplierService = new SupplierService(new SupplierRepository());
        setupColumns();
        loadPurchases();
        setupSearch();
        setupTableSelection();
        productComboBox.getItems().addAll(productService.findAll());
        supplierComboBox.getItems().addAll(supplierService.findAll());
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
                productComboBox.setValue(newSelection.getProduct());
                quantityTextField.setText(Integer.toString(newSelection.getQuantity()));
                supplierComboBox.setValue(newSelection.getSupplier());
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
    private void handleSave() {
        try {
            if (selectedPurchase == null) {
                Purchase newPurchase = GenericBuilder.of(Purchase::new)
                        .with(Purchase::setProduct, productComboBox.getValue())
                        .with(Purchase::setQuantity, Integer.parseInt(quantityTextField.getText()))
                        .with(Purchase::setSupplier, supplierComboBox.getValue())
                        .build();
                purchaseService.add(newPurchase);
            } else {
                selectedPurchase.setProduct(productComboBox.getValue());
                selectedPurchase.setQuantity(Integer.parseInt(quantityTextField.getText()));
                selectedPurchase.setSupplier(supplierComboBox.getValue());
                purchaseService.update(selectedPurchase);
            }
            loadPurchases();
            clearFields();
        } catch (Exception e) {
            DisplayAlert.showError("Error", "Could not save Purchase: " + e.getMessage());
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
        productComboBox.setValue(null);
        quantityTextField.setText("");
        supplierComboBox.setValue(null);
    }
}
