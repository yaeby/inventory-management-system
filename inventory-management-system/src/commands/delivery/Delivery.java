package commands.delivery;

import commands.Command;
import decorator.concrete.ExpressDeliveryDecorator;
import decorator.concrete.InsuranceDecorator;
import model.CompositeOrder;
import model.IOrder;
import model.Order;
import model.Product;
import service.DeliveryService;

public class Delivery implements Command {
    @Override
    public void execute() {
        Product laptop = new Product();
        laptop.setProductName("Laptop");
        laptop.setSellPrice(999.99);

        Product mouse = new Product();
        mouse.setProductName("Mouse");
        mouse.setSellPrice(29.99);

        Order order1 = new Order("ORD001");
        order1.addProduct(laptop, 1);
        order1.addProduct(mouse, 2);

        Order order2 = new Order("ORD002");
        order2.addProduct(mouse, 3);

        CompositeOrder cityOrders = new CompositeOrder("NYC", "City");
        cityOrders.addOrder(order1);
        cityOrders.addOrder(order2);

        DeliveryService deliveryService = new DeliveryService();
        deliveryService.processOrder(cityOrders);

        System.out.println();
        // Scenario 2: Order with Express Delivery
        System.out.println("=== Scenario 2: Order with Express Delivery ===");
        IOrder expressOrder = new ExpressDeliveryDecorator(new Order("ORD-002"), "24h");
        expressOrder.addProduct(laptop, 1);
        expressOrder.display();
        System.out.println();
        // Scenario 3: Order with Insurance
        System.out.println("=== Scenario 3: Order with Insurance ===");
        IOrder insuredOrder = new InsuranceDecorator(new Order("ORD-003"));
        insuredOrder.addProduct(laptop, 2);  // Adding multiple laptops to see insurance scale
        insuredOrder.display();
        System.out.println();

        // Scenario 4: Order with both Express Delivery and Insurance
        System.out.println("=== Scenario 4: Order with Express Delivery and Insurance ===");
        IOrder complexOrder = new InsuranceDecorator(
                new ExpressDeliveryDecorator(new Order("ORD-004"), "24h")
        );
        complexOrder.addProduct(laptop, 1);
        complexOrder.addProduct(mouse, 1);
        complexOrder.display();
        System.out.println();

        // Scenario 5: Adding products after decoration
        System.out.println("=== Scenario 5: Adding products after decoration ===");
        IOrder dynamicOrder = new ExpressDeliveryDecorator(
                new InsuranceDecorator(new Order("ORD-005")), "24h"
        );
        dynamicOrder.addProduct(laptop, 1);
        dynamicOrder.display();
        System.out.println("\nAdding more products...\n");
        dynamicOrder.addProduct(mouse, 2);
        dynamicOrder.display();
    }
}
