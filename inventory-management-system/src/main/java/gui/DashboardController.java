package gui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.*;
import repository.CategoryRepository;
import repository.OrderRepository;
import repository.ProductRepository;
import repository.PurchaseRepository;
import service.*;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DashboardController  {
    @FXML private Label totalProductsLabel;
    @FXML private Label lowStockLabel;
    @FXML private Label totalOrdersLabel;
    @FXML private Label totalRevenueLabel;
    @FXML private PieChart categoriesChart;
    @FXML private BarChart<String, Number> salesChart;
    @FXML private VBox recentActivityContainer;

    private ProductService productService;
    private OrderService orderService;
    private CategoryService categoryService;
    private PurchaseService purchaseService;


    public void initialize() {
        productService = new ProductService(new ProductRepository());
        orderService = new OrderService(new OrderRepository());
        categoryService = new CategoryService(new CategoryRepository());
        purchaseService = new PurchaseService(new PurchaseRepository());

        loadStatistics();
        loadCategoriesChart();
        loadSalesChart();
        loadRecentActivity();
    }

    private void loadStatistics() {
        totalProductsLabel.setText(String.valueOf(productService.getTotalCount()));
        lowStockLabel.setText(String.valueOf(productService.getLowStockCount()));
        totalOrdersLabel.setText(String.valueOf(orderService.getTotalCount()));
        totalRevenueLabel.setText(String.format("$%.2f", orderService.getTotalRevenue() - purchaseService.getTotalSpending()));
    }

    private void loadCategoriesChart() {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        List<Category> categories = categoryService.findAll();

        for (Category category : categories) {
            int productCount = productService.getTotalCountByCategory(category.getId());
            pieChartData.add(new PieChart.Data(category.getName(), productCount));
        }
        categoriesChart.setData(pieChartData);
    }

    private void loadSalesChart() {
        salesChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("Monthly Sales");
        Map<String, Double> monthlySales = orderService.getMonthlySales();
        monthlySales.forEach((month, sales) -> {
            series.getData().add(new XYChart.Data<>(month, sales));
        });

        salesChart.getData().add(series);
    }

    private void loadRecentActivity() {
        recentActivityContainer.getChildren().clear();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd HH:mm");
        List<Object[]> activities = new ArrayList<>();

        List<Order> recentOrders = orderService.getRecentOrders(5);
        for (Order order : recentOrders) {
            activities.add(new Object[]{
                    order.getOrderDate(),
                    String.format(
                            "Order #%d - Customer: %s - Total: + $%.2f",
                            order.getId(),
                            order.getCustomer().getName(),
                            order.getProduct().getSellPrice() * order.getQuantity()
                    ),
                    true
            });
        }

        List<Purchase> recentPurchases = purchaseService.getRecentPurchases(5);
        for (Purchase purchase : recentPurchases) {
            activities.add(new Object[]{
                    purchase.getPurchaseDate(),
                    String.format(
                            "Purchase #%d - Supplier: %s - Total: - $%.2f",
                            purchase.getId(),
                            purchase.getSupplier().getName(),
                            purchase.getProduct().getCostPrice() * purchase.getQuantity()
                    ),
                    false
            });
        }

        activities.sort((a, b) -> ((LocalDateTime)b[0]).compareTo((LocalDateTime)a[0]));
        activities = activities.stream().limit(10).collect(Collectors.toList());

        for (Object[] activity : activities) {
            LocalDateTime dateTime = (LocalDateTime) activity[0];
            String description = (String) activity[1];
            boolean isOrder = (boolean) activity[2];

            Label activityLabel = new Label(
                    dateTime.format(formatter) + " - " + description
            );

            activityLabel.setStyle(String.format(
                    "-fx-text-fill: %s; -fx-padding: 5 0 5 0;",
                    isOrder ? "#27ae60" : "#c0392b"
            ));

            recentActivityContainer.getChildren().add(activityLabel);
        }
    }

    public void refreshDashboard() {
        loadStatistics();
        loadCategoriesChart();
        loadSalesChart();
        loadRecentActivity();
    }
}