package gui;

import builder.GenericBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;
import repository.CustomerRepository;
import service.CustomerService;

import java.util.List;
import java.util.Optional;

public class CustomerController {
    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, String> nameColumn;
    @FXML
    private TableColumn<Customer, String> emailColumn;
    @FXML
    private TableColumn<Customer, String> phoneColumn;
    @FXML
    private TableColumn<Customer, String> addressColumn;
    @FXML
    private TextField searchField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField emailField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField addressField;

    private CustomerService customerService;
    private ObservableList<Customer> customerList;
    private Customer selectedCustomer;

    public void initialize() {
        customerService = new CustomerService(new CustomerRepository());
        setupColumns();
        loadCustomers();
        setupSearch();
        setupTableSelection();
    }

    private void setupColumns() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        addressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
    }

    private void setupTableSelection() {
        customerTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedCustomer = newSelection;
                nameField.setText(newSelection.getName());
                emailField.setText(newSelection.getEmail());
                phoneField.setText(newSelection.getPhone());
                addressField.setText(newSelection.getAddress());
            }
        });
    }

    private void loadCustomers() {
        try {
            List<Customer> customers = customerService.findAll();
            customerList = FXCollections.observableArrayList(customers);
            customerTable.setItems(customerList);
        } catch (Exception e) {
            DisplayAlert.showError("Error loading customers", "The server couldn't load the data");
        }
    }

    private void setupSearch() {
        FilteredList<Customer> filteredData = new FilteredList<>(customerList, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> filteredData.setPredicate(customer -> {
            if (newValue == null || newValue.isEmpty()) {
                return true;
            }
            String lowerCaseFilter = newValue.toLowerCase();

            return customer.getName().toLowerCase().contains(lowerCaseFilter)
                    || customer.getEmail().toLowerCase().contains(lowerCaseFilter)
                    || customer.getPhone().toLowerCase().contains(lowerCaseFilter)
                    || customer.getAddress().toLowerCase().contains(lowerCaseFilter);
        }));

        customerTable.setItems(filteredData);
    }

    @FXML
    private void handleSave() {
        try {
            if (selectedCustomer == null) {
                Customer newCustomer = GenericBuilder.of(Customer::new)
                        .with(Customer::setName, nameField.getText())
                        .with(Customer::setEmail, emailField.getText())
                        .with(Customer::setPhone, phoneField.getText())
                        .with(Customer::setAddress, addressField.getText())
                        .build();
                customerService.add(newCustomer);
            } else {
                selectedCustomer.setName(nameField.getText());
                selectedCustomer.setEmail(emailField.getText());
                selectedCustomer.setPhone(phoneField.getText());
                selectedCustomer.setAddress(addressField.getText());
                customerService.update(selectedCustomer);
            }
            loadCustomers();
            clearFields();
        } catch (Exception e) {
            DisplayAlert.showError("Error", "Could not save customer: " + e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedCustomer != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Customer");
            alert.setHeaderText("Delete Customer");
            alert.setContentText("Are you sure you want to delete " + selectedCustomer.getName() + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    customerService.delete(selectedCustomer.getId());
                    loadCustomers();
                    clearFields();
                } catch (Exception e) {
                    DisplayAlert.showError("Error deleting customer", e.getMessage());
                }
            }
        }
    }

    private void clearFields() {
        selectedCustomer = null;
        nameField.clear();
        emailField.clear();
        phoneField.clear();
        addressField.clear();
        customerTable.getSelectionModel().clearSelection();
    }
}