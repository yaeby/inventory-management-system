package gui;

import commands.Command;
import commands.product.AddProductCommand;
import commands.product.UpdateProductCommand;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.control.Alert;
import model.Product;
import service.ProductService;

public class ProductDialogController {
    @FXML
    private TextField codeField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField brandField;
    @FXML
    private TextField quantityField;
    @FXML
    private TextField costPriceField;
    @FXML
    private TextField sellPriceField;

    private Product product;
    private ProductService productService;
    private boolean isNew = true;

    public void setProduct(Product product) {
        this.product = product;
        if (product != null) {
            isNew = false;
            fillFields();
        }
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }

    private void fillFields() {
        codeField.setText(product.getProductCode());
        nameField.setText(product.getProductName());
        brandField.setText(product.getBrand());
        quantityField.setText(String.valueOf(product.getQuantity()));
        costPriceField.setText(String.valueOf(product.getCostPrice()));
        sellPriceField.setText(String.valueOf(product.getSellPrice()));
    }

    @FXML
    private void handleSave() {
        try {
            if (validateInput()) {
                Product updatedProduct = isNew ? new Product() : product;
                updateProductFromFields(updatedProduct);
                Command command;
                if (isNew) {
                    command = new AddProductCommand(productService, updatedProduct);
                } else {
                    command = new UpdateProductCommand(productService, updatedProduct);
                }
                command.execute();
                closeDialog();
            }
        } catch (Exception e) {
            showError("Error saving product", e.getMessage());
        }
    }

    private void updateProductFromFields(Product product) {
        product.setProductCode(codeField.getText());
        product.setProductName(nameField.getText());
        product.setBrand(brandField.getText());
        product.setQuantity(Integer.parseInt(quantityField.getText()));
        product.setCostPrice(Double.parseDouble(costPriceField.getText()));
        product.setSellPrice(Double.parseDouble(sellPriceField.getText()));
    }

    private boolean validateInput() {
        if (codeField.getText().isEmpty() || nameField.getText().isEmpty()) {
            showError("Validation Error", "Code and Name are required fields");
            return false;
        }

        try {
            Integer.parseInt(quantityField.getText());
            Double.parseDouble(costPriceField.getText());
            Double.parseDouble(sellPriceField.getText());
        } catch (NumberFormatException e) {
            showError("Validation Error", "Please enter valid numbers for Quantity and Prices");
            return false;
        }

        return true;
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) codeField.getScene().getWindow();
        stage.close();
    }

    private void showError(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}