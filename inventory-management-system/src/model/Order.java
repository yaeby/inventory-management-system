package model;

import java.util.ArrayList;
import java.util.List;

public class Order implements IOrder {
    private final String orderId;
    private List<OrderItem> items;

    public Order(String orderId) {
        this.orderId = orderId;
        this.items = new ArrayList<>();
    }

    private class OrderItem {
        Product product;
        int quantity;

        OrderItem(Product product, int quantity) {
            this.product = product;
            this.quantity = quantity;
        }
    }

    @Override
    public void addProduct(Product product, int quantity) {
        items.add(new OrderItem(product, quantity));
    }

    @Override
    public double getTotalCost() {
        return items.stream()
                .mapToDouble(item -> item.product.getSellPrice() * item.quantity)
                .sum();
    }

    @Override
    public void display() {
        System.out.println("Order ID: " + orderId);
        items.forEach(item ->
                System.out.println("- " + item.product.getProductName() +
                        " x" + item.quantity +
                        " ($" + item.product.getSellPrice() * item.quantity + ")")
        );
        System.out.println("Total: $" + getTotalCost());
    }

    @Override
    public String getOrderInfo() {
        return "Single Order #" + orderId + " - Items: " + items.size() +
                " - Total: $" + getTotalCost();
    }
}
