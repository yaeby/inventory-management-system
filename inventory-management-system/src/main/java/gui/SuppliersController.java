package gui;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;

public class SuppliersController {
    @FXML private TableView<?> suppliersTable;
    @FXML private TableColumn<?, ?> idColumn;
    @FXML private TableColumn<?, ?> nameColumn;
    @FXML private TableColumn<?, ?> contactColumn;
    @FXML private TableColumn<?, ?> emailColumn;

    @FXML
    private void handleAddSupplier() {
        // TODO: Implement add supplier logic
        System.out.println("Add Supplier clicked");
    }

    @FXML
    private void handleEditSupplier() {
        // TODO: Implement edit supplier logic
        System.out.println("Edit Supplier clicked");
    }

    @FXML
    private void handleDeleteSupplier() {
        // TODO: Implement delete supplier logic
        System.out.println("Delete Supplier clicked");
    }
}