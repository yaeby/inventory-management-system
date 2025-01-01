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
import model.Order;
import repository.OrderRepository;
import service.OrderService;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class OrdersController {
    @FXML
    private TableView<Order> orderTable;
    @FXML
    private TableColumn<Order, String> customerColumn;
    @FXML
    private TableColumn<Order, String> productCodeColumn;
    @FXML
    private TableColumn<Order, String> productNameColumn;
    @FXML
    private TableColumn<Order, String> quantityColumn;
    @FXML
    private Label customerLabel;
    @FXML
    private Label productCodeLabel;
    @FXML
    private Label productNameLabel;
    @FXML
    private Label quantityLabel;
    @FXML
    private TextField searchField;
    
    private OrderService orderService;
    private ObservableList<Order> orderList;
    private Order selectedOrder;

    public void initialize() {
        orderService = new OrderService(new OrderRepository());
        setupColumns();
        loadOrders();
        setupSearch();
        setupTableSelection();
    }

    private void setupColumns() {
        customerColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getCustomer().getName()));
        productCodeColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getProduct().getProductCode()));
        productNameColumn.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getProduct().getProductName()));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
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
                customerLabel.setText(selectedOrder.getCustomer().getName());
                productCodeLabel.setText(selectedOrder.getProduct().getProductCode());
                productNameLabel.setText(selectedOrder.getProduct().getProductName());
                quantityLabel.setText(String.valueOf(selectedOrder.getQuantity()));
            }
        });
    }

    private void setupSearch(){
        FilteredList<Order> filteredData = new FilteredList<>(orderList, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate(order -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = newValue.toLowerCase();
            return order.getProduct().getProductCode().toLowerCase().contains(lowerCaseFilter)
                    || order.getProduct().getProductName().toLowerCase().contains(lowerCaseFilter)
                    || order.getCustomer().getName().toLowerCase().contains(lowerCaseFilter);
        }));
        orderTable.setItems(filteredData);
    }

    @FXML
    private void handleDelete(){
        if(selectedOrder != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Order");
            alert.setHeaderText("Delete Order");
            alert.setContentText("Deleting this order will not cancel it");

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

    private void clearFields(){
        selectedOrder = null;
        customerLabel.setText("");
        productCodeLabel.setText("");
        productNameLabel.setText("");
        quantityLabel.setText("");
        orderTable.getSelectionModel().clearSelection();
    }

    @FXML
    private void handleCreateOrder(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/order-dialog.fxml"));
            Parent root = loader.load();

            OrderDialogController dialogController = loader.getController();
            dialogController.setOrderService(orderService);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Create Order");
            dialogStage.setScene(new Scene(root));
            dialogStage.showAndWait();

            loadOrders();
        } catch (IOException e) {
            DisplayAlert.showError("Error", "Could not load order dialog: " + e.getMessage());
        }
    }
}