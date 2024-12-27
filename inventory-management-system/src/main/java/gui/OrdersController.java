package gui;

import builder.GenericBuilder;
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
import model.Customer;
import model.Order;
import model.OrderItem;
import repository.CustomerRepository;
import repository.OrderRepository;
import service.CustomerService;
import service.OrderService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OrdersController {
    @FXML
    private TableView<Order> orderTable;
    @FXML
    private TableColumn<Order, String> orderColumn;
    @FXML
    private TableColumn<Order, String> customerColumn;
    @FXML
    private TextField orderField;
    @FXML
    private ComboBox<Customer> customerComboBox;
    @FXML
    private TableView<OrderItem> productsTable;
    @FXML
    private TableColumn<OrderItem, String> productNameColumn;
    @FXML
    private TableColumn<OrderItem, Integer> quantityColumn;
    @FXML
    private TextField searchField;
    
    private OrderService orderService;
    private ObservableList<Order> orderList;
    private ObservableList<OrderItem> orderItemsList = FXCollections.observableArrayList();
    private Order selectedOrder;

    public void initialize() {
        orderService = new OrderService(new OrderRepository());
        CustomerService customerService = new CustomerService(new CustomerRepository());
        setupColumns();
        loadOrders();
        setupSearch();
        setupTableSelection();
        customerComboBox.getItems().addAll(customerService.findAll());
        setupOrderItemsColumns();
    }

    private void setupColumns(){
        orderColumn.setCellValueFactory(new PropertyValueFactory<>("orderNumber"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customer"));
    }

    private void loadOrders(){
        try {
            List<Order> orders = orderService.findAll();
            orderList = FXCollections.observableArrayList(orders);
            orderTable.setItems(orderList);
        } catch (Exception e) {
            DisplayAlert.showError("Error loading orders", "Internal Error");
        }
    }

    private void setupTableSelection(){
        orderTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newSelection) -> {
            if (newSelection != null) {
                selectedOrder = newSelection;
                orderField.setText(newSelection.getOrderNumber());
                customerComboBox.setValue(newSelection.getCustomer());
                orderItemsList = FXCollections.observableArrayList(selectedOrder.getOrderItems());
                productsTable.setItems(orderItemsList);
            }
        });
    }

    private void setupSearch(){
        FilteredList<Order> filteredData = new FilteredList<>(orderList, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(order -> order.getOrderNumber().toLowerCase().contains(newValue.toLowerCase()));
        });

        orderTable.setItems(filteredData);
    }

    private void setupOrderItemsColumns() {
        productNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getProduct().getProductName()));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    }

    @FXML
    private void handleSave(){
        try{
            if(selectedOrder == null){
                Order order = GenericBuilder.of(Order::new)
                        .with(Order::setOrderNumber, orderField.getText())
                        .with(Order::setCustomer, customerComboBox.getValue())
                        .with(Order::setOrderItems, new ArrayList<>(orderItemsList))
                        .build();
                orderService.add(order);
            } else {
                selectedOrder.setOrderNumber(orderField.getText());
                selectedOrder.setCustomer(customerComboBox.getValue());
                List<OrderItem> orderItems = new ArrayList<>(orderItemsList);
                orderItems.forEach(item -> item.setOrderId(selectedOrder.getId()));
                selectedOrder.setOrderItems(orderItems);
                orderService.update(selectedOrder);
            }
            loadOrders();
            clearFields();
        } catch (Exception e){
            DisplayAlert.showError("Error saving order", "Could not save order. " + e.getMessage());
        }
    }

    @FXML
    private void handleDelete(){
        if(selectedOrder != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Order");
            alert.setHeaderText("Delete Order");
            alert.setContentText("Are you sure you want to delete " + selectedOrder.getOrderNumber() + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    orderService.delete(selectedOrder.getId());
                    loadOrders();
                    clearFields();
                } catch (Exception e) {
                    DisplayAlert.showError("Error deleting order", e.getMessage());
                }
            }
        }
    }

    @FXML
    private void clearFields(){
        selectedOrder = null;
        orderField.clear();
        customerComboBox.setValue(null);
        orderItemsList.clear();
        orderTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleAddProduct(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/order-dialog.fxml"));
            Parent root = loader.load();

            OrderDialogController controller = loader.getController();
            controller.setOrderController(this, orderItemsList);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Add Product");
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

            loadOrders();
        } catch (IOException e) {
            DisplayAlert.showError("Error", "Could not load order dialog: " + e.getMessage());
        }
    }
}