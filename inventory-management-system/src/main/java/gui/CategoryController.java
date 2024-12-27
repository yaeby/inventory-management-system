package gui;

import builder.GenericBuilder;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Category;
import repository.CategoryRepository;
import service.CategoryService;

import java.util.List;
import java.util.Optional;

public class CategoryController {
    @FXML
    private TableView<Category> categoryTable;
    @FXML
    private TableColumn<Category, String> nameColumn;
    @FXML
    private TableColumn<Category, String> descriptionColumn;
    @FXML
    private TextField searchField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField descriptionField;

    private CategoryService categoryService;
    private ObservableList<Category> categoryList;
    private Category selectedCategory;

    public void initialize() {
        categoryService = new CategoryService(new CategoryRepository());
        setupColumns();
        loadCategories();
        setupSearch();
        setupTableSelection();
    }

    private void setupColumns(){
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
    }

    private void loadCategories(){
        try {
            List<Category> categories = categoryService.findAll();
            categoryList = FXCollections.observableArrayList(categories);
            categoryTable.setItems(categoryList);
        } catch (Exception e) {
            DisplayAlert.showError("Error loading categories", "Internal Error");
        }
    }

    private void setupTableSelection(){
        categoryTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newSelection) -> {
            if (newSelection != null) {
                selectedCategory = newSelection;
                nameField.setText(newSelection.getName());
                descriptionField.setText(newSelection.getDescription());
            }
        });
    }

    private void setupSearch(){
        FilteredList<Category> filteredData = new FilteredList<>(categoryList, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(category -> category.getName().toLowerCase().contains(newValue.toLowerCase()));
        });

        categoryTable.setItems(filteredData);
    }

    @FXML
    private void handleSave(){
        try{
            if(selectedCategory == null){
                Category newCategory = GenericBuilder.of(Category::new)
                        .with(Category::setName, nameField.getText())
                        .with(Category::setDescription, descriptionField.getText())
                        .build();
                categoryService.add(newCategory);
            } else {
                selectedCategory.setName(nameField.getText());
                selectedCategory.setDescription(descriptionField.getText());
                categoryService.update(selectedCategory);
            }
            loadCategories();
            clearFields();
        } catch (Exception e){
            DisplayAlert.showError("Error saving category", "Could not save category. " + e.getMessage());
        }
    }

    @FXML
    private void handleDelete(){
        if(selectedCategory != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Category");
            alert.setHeaderText("Delete Category");
            alert.setContentText("Are you sure you want to delete " + selectedCategory.getName() + "?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    categoryService.delete(selectedCategory.getId());
                    loadCategories();
                    clearFields();
                } catch (Exception e) {
                    DisplayAlert.showError("Error deleting category", e.getMessage());
                }
            }
        }
    }

    @FXML
    private void clearFields(){
        selectedCategory = null;
        nameField.clear();
        descriptionField.clear();
        categoryTable.getSelectionModel().clearSelection();
    }
}
