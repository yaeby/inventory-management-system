package gui;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.OrderItem;
import model.Product;
import repository.OrderItemRepository;
import repository.ProductRepository;
import service.OrderItemService;
import service.ProductService;

public class OrderDialogController {
    @FXML
    private ComboBox<Product> productComboBox;
    @FXML
    private TextField quantityField;

    private OrdersController orderController;
    private ObservableList<OrderItem> orderItems;

    public void setOrderController(OrdersController controller, ObservableList<OrderItem> items) {
        this.orderController = controller;
        this.orderItems = items;
    }

    public void initialize() {
        ProductService productService = new ProductService(new ProductRepository());
        productComboBox.getItems().addAll(productService.findAll());
    }

    @FXML
    private void handleAdd() {
        try {
            Product selectedProduct = productComboBox.getValue();
            if (selectedProduct == null) {
                DisplayAlert.showError("Validation Error", "Please select a product");
                return;
            }

            int quantity;
            try {
                quantity = Integer.parseInt(quantityField.getText());
                if (quantity <= 0) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                DisplayAlert.showError("Validation Error", "Please enter a valid quantity");
                return;
            }

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(selectedProduct);
            orderItem.setQuantity(quantity);

            orderItems.add(orderItem);

            Stage stage = (Stage) productComboBox.getScene().getWindow();
            stage.close();
        } catch (Exception e) {
            DisplayAlert.showError("Error adding product", e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        Stage stage = (Stage) productComboBox.getScene().getWindow();
        stage.close();
    }

}
