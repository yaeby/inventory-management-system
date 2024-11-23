package model;

import java.util.ArrayList;
import java.util.List;

public class CompositeOrder implements IOrder {
    private String groupId;
    private String groupType;
    private List<IOrder> orders;

    public CompositeOrder(String groupId, String groupType) {
        this.groupId = groupId;
        this.groupType = groupType;
        this.orders = new ArrayList<>();
    }

    public void addOrder(IOrder order) {
        orders.add(order);
    }

    public void removeOrder(IOrder order) {
        orders.remove(order);
    }

    @Override
    public void addProduct(Product product, int quantity) {
        throw new UnsupportedOperationException("Product cannot be added!");
    }

    @Override
    public double getTotalCost() {
        double total = orders.stream()
                .mapToDouble(IOrder::getTotalCost)
                .sum();
        return Math.round(total * 100.0) / 100.0;
    }

    @Override
    public void display() {
        System.out.println("Group: " + groupId + " Type: " + groupType);
        System.out.println("Contains " + orders.size() + " orders:");
        orders.forEach(order -> {
            System.out.println("  " + order.getOrderInfo());
        });
        System.out.println("Group Total: $" + getTotalCost());
    }

    @Override
    public String getOrderInfo() {
        return "Group: " + groupId + " Type: " + groupType + " - Orders: " +
                orders.size() + " - Total: $" + getTotalCost();
    }
}
