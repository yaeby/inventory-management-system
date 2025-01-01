package gui;

import builder.GenericBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Customer;
import model.Supplier;
import repository.SupplierRepository;
import service.SupplierService;

import java.util.List;
import java.util.Optional;

public class SupplierController {
    @FXML
    private TableView<Supplier> supplierTable;
    @FXML
    private TableColumn<Supplier, String> nameColumn;
    @FXML
    private TableColumn<Supplier, String> emailColumn;
    @FXML
    private TableColumn<Supplier, String> phoneColumn;
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

    private SupplierService supplierService;
    private ObservableList<Supplier> supplierList;
    private Supplier selectedSupplier;

    public void initialize() {
        supplierService = new SupplierService(new SupplierRepository());
        setupColumns();
        loadSuppliers();
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
        supplierTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedSupplier = newSelection;
                nameField.setText(newSelection.getName());
                emailField.setText(newSelection.getEmail());
                phoneField.setText(newSelection.getPhone());
                addressField.setText(newSelection.getAddress());
            }
        });
    }

    private void loadSuppliers() {
        try {
            List<Supplier> suppliers = supplierService.findAll();
            supplierList = FXCollections.observableArrayList(suppliers);
            supplierTable.setItems(supplierList);
        } catch (Exception e) {
            DisplayAlert.showError("Error loading suppliers", "The server couldn't load the data");
        }
    }

    private void setupSearch() {
        FilteredList<Supplier> filteredData = new FilteredList<>(supplierList, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(supplier -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();

                return supplier.getName().toLowerCase().contains(lowerCaseFilter)
                        || supplier.getEmail().toLowerCase().contains(lowerCaseFilter)
                        || supplier.getPhone().toLowerCase().contains(lowerCaseFilter)
                        || supplier.getAddress().toLowerCase().contains(lowerCaseFilter);
            });
        });

        supplierTable.setItems(filteredData);
    }

    @FXML
    private void handleSave() {
        try {
            if (selectedSupplier == null) {
                Supplier newSupplier = GenericBuilder.of(Supplier::new)
                        .with(Supplier::setName, nameField.getText())
                        .with(Supplier::setEmail, emailField.getText())
                        .with(Supplier::setPhone, phoneField.getText())
                        .with(Supplier::setAddress, addressField.getText())
                        .build();
                supplierService.add(newSupplier);
            } else {
                selectedSupplier.setName(nameField.getText());
                selectedSupplier.setEmail(emailField.getText());
                selectedSupplier.setPhone(phoneField.getText());
                selectedSupplier.setAddress(addressField.getText());
                supplierService.update(selectedSupplier);
            }
            loadSuppliers();
            clearFields();
        } catch (Exception e) {
            DisplayAlert.showError("Error", "Could not save supplier: " + e.getMessage());
        }
    }

    @FXML
    private void handleDelete() {
        if (selectedSupplier != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Supplier");
            alert.setHeaderText("Delete Supplier");
            alert.setContentText("Are you sure you want to delete " + selectedSupplier.getName() + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    supplierService.delete(selectedSupplier.getId());
                    loadSuppliers();
                    clearFields();
                } catch (Exception e) {
                    DisplayAlert.showError("Error deleting supplier", e.getMessage());
                }
            }
        }
    }

    private void clearFields() {
        selectedSupplier = null;
        nameField.clear();
        emailField.clear();
        phoneField.clear();
        addressField.clear();
        supplierTable.getSelectionModel().clearSelection();
    }
}
