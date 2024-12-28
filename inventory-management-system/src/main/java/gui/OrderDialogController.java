package gui;

import builder.GenericBuilder;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.Customer;
import model.Order;
import model.Product;
import repository.CustomerRepository;
import repository.ProductRepository;
import service.CustomerService;
import service.OrderService;
import service.ProductService;

public class OrderDialogController {
    @FXML
    private ComboBox<Customer> customerComboBox;
    @FXML
    private ComboBox<Product> productComboBox;
    @FXML
    private TextField quantityField;
    private OrderService orderService;

    public void initialize() {
        ProductService productService = new ProductService(new ProductRepository());
        CustomerService customerService = new CustomerService(new CustomerRepository());
        customerComboBox.getItems().addAll(customerService.findAll());
        productComboBox.getItems().addAll(productService.findAll());
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @FXML
    private void handleAdd() {
        try {
            if(validateInput()){
                Order order = GenericBuilder.of(Order::new)
                                .with(Order::setCustomer, customerComboBox.getValue())
                                .with(Order::setProduct, productComboBox.getValue())
                                .with(Order::setQuantity, Integer.parseInt(quantityField.getText()))
                                .build();
                orderService.add(order);
            }
            closeDialog();
        } catch (Exception e) {
            DisplayAlert.showError("Error adding product", e.getMessage());
        }
    }

    private boolean validateInput(){
        if(customerComboBox.getValue() == null) {
            DisplayAlert.showError("Validation Error", "Please select a customer");
            return false;
        }
        if (productComboBox.getValue() == null) {
            DisplayAlert.showError("Validation Error", "Please select a product");
            return false;
        }
        int quantity;
        try {
            quantity = Integer.parseInt(quantityField.getText());
            if (quantity <= 0) {
                throw new NumberFormatException();
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
        Stage stage = (Stage) customerComboBox.getScene().getWindow();
        stage.close();
    }

}
