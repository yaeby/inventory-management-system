package service;

import model.IOrder;

public class DeliveryService {
    public void processOrder(IOrder order) {
        System.out.println("\nProcessing delivery for:");
        order.display();
    }

    public void scheduleDelivery(IOrder order, String deliveryDate) {
        System.out.println("Scheduling delivery for " + deliveryDate);
        order.display();
    }
}
