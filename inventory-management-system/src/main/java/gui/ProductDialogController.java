package gui;

import commands.Command;
import commands.product.AddProductCommand;
import commands.product.UpdateProductCommand;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Category;
import model.Product;
import service.ProductService;

import java.util.List;

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
    @FXML
    private ComboBox<Category> categoryComboBox;

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

    public void setCategories(List<Category> categories) {
        categoryComboBox.getItems().addAll(categories);
    }

    private void fillFields() {
        codeField.setText(product.getProductCode());
        nameField.setText(product.getProductName());
        brandField.setText(product.getBrand());
        quantityField.setText(String.valueOf(product.getQuantity()));
        costPriceField.setText(String.valueOf(product.getCostPrice()));
        sellPriceField.setText(String.valueOf(product.getSellPrice()));
        categoryComboBox.setValue(product.getCategory());
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
            DisplayAlert.showError("Error saving product", e.getMessage());
        }
    }

    private void updateProductFromFields(Product product) {
        product.setProductCode(codeField.getText());
        product.setProductName(nameField.getText());
        product.setBrand(brandField.getText());
        product.setQuantity(Integer.parseInt(quantityField.getText()));
        product.setCostPrice(Double.parseDouble(costPriceField.getText()));
        product.setSellPrice(Double.parseDouble(sellPriceField.getText()));
        product.setCategory(categoryComboBox.getValue());
    }

    private boolean validateInput() {
        if (codeField.getText().isEmpty() || nameField.getText().isEmpty()) {
            DisplayAlert.showError("Validation Error", "Code and Name are required fields");
            return false;
        }

        if(categoryComboBox.getValue() == null){
            DisplayAlert.showError("Validation Error", "Category is required");
            return false;
        }

        try {
            Integer.parseInt(quantityField.getText());
            Double.parseDouble(costPriceField.getText());
            Double.parseDouble(sellPriceField.getText());
        } catch (NumberFormatException e) {
            DisplayAlert.showError("Validation Error", "Please enter valid numbers for Quantity and Prices");
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
}