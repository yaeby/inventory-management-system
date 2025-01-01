package gui;

import builder.GenericBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Purchase;
import model.Product;
import model.Supplier;
import repository.ProductRepository;
import repository.SupplierRepository;
import service.*;

import java.time.LocalDateTime;

public class PurchaseDialogController {
    @FXML
    private ComboBox<Product> productComboBox;
    @FXML
    private TextField quantityField;
    @FXML
    private ComboBox<Supplier> supplierComboBox;
    
    private PurchaseService purchaseService;
    private ProductService productService;

    public void initialize() {
        productService = new ProductService(new ProductRepository());
        SupplierService supplierService = new SupplierService(new SupplierRepository());
        supplierComboBox.getItems().addAll(supplierService.findAll());
        productComboBox.getItems().addAll(productService.findAll());
    }

    public void setPurchaseService(PurchaseService purchaseService) {
        this.purchaseService = purchaseService;
    }

    @FXML
    private void handleAdd() {
        try {
            if(validateInput()){
                Product product = productComboBox.getValue();
                int quantity = Integer.parseInt(quantityField.getText());
                product.setQuantity(product.getQuantity() + quantity);
                productService.update(product);
                Purchase purchase = GenericBuilder.of(Purchase::new)
                        .with(Purchase::setSupplier, supplierComboBox.getValue())
                        .with(Purchase::setProduct, product)
                        .with(Purchase::setQuantity, quantity)
                        .with(Purchase::setPurchaseDate, LocalDateTime.now())
                        .build();
                purchaseService.add(purchase);
            }
            closeDialog();
        } catch (Exception e) {
            DisplayAlert.showError("Error making purchase", e.getMessage());
        }
    }

    private boolean validateInput(){
        if(supplierComboBox.getValue() == null) {
            DisplayAlert.showError("Validation Error", "Please select a supplier");
            return false;
        }
        if (productComboBox.getValue() == null) {
            DisplayAlert.showError("Validation Error", "Please select a product");
            return false;
        }
        try {
            int quantity = Integer.parseInt(quantityField.getText());
            if(quantity <= 0){
                DisplayAlert.showError("Validation Error", "Quantity must be a positive number");
                return false;
            }
        } catch (NumberFormatException e) {
            DisplayAlert.showError("Validation Error", "Please enter a valid quantity");
            return false;
        }
        return true;
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private void closeDialog(){
        Stage stage = (Stage) supplierComboBox.getScene().getWindow();
        stage.close();
    }
}
