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
        throw new UnsupportedOperationException(
                "Cannot add products directly to a composite order. Add to individual orders instead.");
    }

    @Override
    public double getTotalCost() {
        return orders.stream()
                .mapToDouble(IOrder::getTotalCost)
                .sum();
    }

    @Override
    public void display() {
        System.out.println(groupType + " Group: " + groupId);
        System.out.println("Contains " + orders.size() + " orders:");
        orders.forEach(order -> {
            System.out.println("  " + order.getOrderInfo());
        });
        System.out.println("Group Total: $" + getTotalCost());
    }

    @Override
    public String getOrderInfo() {
        return groupType + " Group " + groupId + " - Orders: " +
                orders.size() + " - Total: $" + getTotalCost();
    }
}
